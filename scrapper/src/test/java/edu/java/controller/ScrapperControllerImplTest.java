package edu.java.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.configuration.ApplicationConfig;
import edu.java.dto.dao.LinkDto;
import edu.java.exceptions.ChatNotExistException;
import edu.java.exceptions.LinkNotExistException;
import edu.java.rate_limiting.RateLimitBucketsCache;
import edu.java.scrapper.AddLinkRequest;
import edu.java.scrapper.LinkResponse;
import edu.java.scrapper.ListLinksResponse;
import edu.java.scrapper.RemoveLinkRequest;
import edu.java.service.LinkService;
import edu.java.service.TgChatService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScrapperControllerImpl.class)
class ScrapperControllerImplTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TgChatService tgChatService;
    @MockBean
    private LinkService linkService;

    @Test
    void givenValidId_whenRegisterChat_thenRegisterAndReturnOk() throws Exception {
        Long chatId = 1L;
        String successMessage = "Chat has been successfully registered";

        mockMvc
            .perform(post("/tg-chat/{id}", chatId))
            .andDo(print())
            .andExpectAll(
                status().isOk(),
                content().string(containsString(successMessage))
            );

        Mockito.verify(tgChatService).register(chatId);
    }

    @Test
    void givenInvalidId_whenRegisterChat_thenNotRegisterAndReturnBadRequest() throws Exception {
        Long chatId = -1L;

        mockMvc
            .perform(post("/tg-chat/{id}", chatId))
            .andDo(print())
            .andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON)
            );

        Mockito.verify(tgChatService, Mockito.times(0)).register(chatId);
    }

    @Test
    void givenValidId_whenDeleteChat_thenDeleteChatAndReturnOk() throws Exception {
        Long chatId = 1L;
        String successMessage = "Chat has been successfully removed";

        mockMvc
            .perform(delete("/tg-chat/{id}", chatId))
            .andDo(print())
            .andExpectAll(
                status().isOk(),
                content().string(containsString(successMessage))
            );

        Mockito.verify(tgChatService).unregister(chatId);
    }

    @Test
    void givenInvalidId_whenDeleteChat_thenNotDeleteAndReturnBadRequest() throws Exception {
        Long chatId = -1L;

        mockMvc
            .perform(delete("/tg-chat/{id}", chatId))
            .andDo(print())
            .andExpectAll(
                status().isBadRequest()
            );

        Mockito.verify(tgChatService, Mockito.times(0)).unregister(chatId);
    }

    @Test
    void givenNonExistentId_whenDeleteChat_thenNotDeleteReturnNotFound() throws Exception {
        Long chatId = 1L;

        Mockito.doThrow(ChatNotExistException.class).when(tgChatService).unregister(chatId);

        mockMvc
            .perform(delete("/tg-chat/{id}", chatId))
            .andDo(print())
            .andExpectAll(
                status().isNotFound()
            );

        Mockito.verify(tgChatService).unregister(chatId);
    }

    @Test
    void givenValidId_whenGetLinks_thenReturnOkAndListOfLinks() throws Exception {
        Long chatId = 1L;
        List<LinkDto> linkDtoList = List.of(
            new LinkDto(2L, URI.create("url"), OffsetDateTime.now(), OffsetDateTime.now())
        );
        ListLinksResponse expectedResponse = convertDtoListToResponse(linkDtoList);

        Mockito.when(linkService.getAllSubscriptions(chatId)).thenReturn(linkDtoList);

        mockMvc
            .perform(
                get("/links")
                    .header("Tg-Chat-Id", chatId)
            )
            .andDo(print())
            .andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json(objectMapper.writeValueAsString(expectedResponse))
            );

        Mockito.verify(linkService).getAllSubscriptions(chatId);
    }

    @Test
    void givenInvalidId_whenGetLinks_thenReturnBadRequest() throws Exception {
        Long chatId = -1L;

        mockMvc
            .perform(
                get("/links")
                    .header("Tg-Chat-Id", chatId)
            )
            .andDo(print())
            .andExpectAll(
                status().isBadRequest()
            );

        Mockito.verify(linkService, Mockito.times(0)).getAllSubscriptions(chatId);
    }

    @Test
    void givenNonExistentId_whenGetLinks_thenReturnEmptyList() throws Exception {
        Long chatId = 1L;
        List<LinkDto> linkDtoList = List.of();
        ListLinksResponse expectedResponse = convertDtoListToResponse(linkDtoList);

        Mockito.when(linkService.getAllSubscriptions(chatId)).thenReturn(linkDtoList);

        mockMvc
            .perform(
                get("/links")
                    .header("Tg-Chat-Id", chatId)
            )
            .andDo(print())
            .andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json(objectMapper.writeValueAsString(expectedResponse))
            );

        Mockito.verify(linkService).getAllSubscriptions(chatId);
    }

    @Test
    void givenValidIdAndBody_whenAddLink_thenAddLinkAndReturnOk() throws Exception {
        Long chatId = 1L;
        Long linkId = 2L;
        URI url = URI.create("url");
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckTime = OffsetDateTime.now();
        AddLinkRequest requestBody = new AddLinkRequest(url);
        LinkResponse expectedResponse = new LinkResponse(linkId, url);

        Mockito.when(linkService.add(chatId, requestBody.link())).thenReturn(
            new LinkDto(
                linkId,
                url,
                updatedAt,
                lastCheckTime
            )
        );

        mockMvc
            .perform(
                post("/links")
                    .header("Tg-Chat-Id", chatId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestBody))
            )
            .andDo(print())
            .andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json(objectMapper.writeValueAsString(expectedResponse))
            );

        Mockito.verify(linkService).add(chatId, requestBody.link());
    }

    @Test
    void givenInvalidIdAndBody_whenAddLink_thenNotAddAndReturnBadRequest() throws Exception {
        Long chatId = -1L;
        URI url = URI.create("url");
        AddLinkRequest requestBody = new AddLinkRequest(url);

        mockMvc
            .perform(
                post("/links")
                    .header("Tg-Chat-Id", chatId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestBody))
            )
            .andDo(print())
            .andExpectAll(
                status().isBadRequest()
            );

        Mockito.verify(linkService, Mockito.times(0)).add(chatId, requestBody.link());
    }

    @Test
    void givenValidIdAndBody_whenDeleteLink_thenDeleteAndReturnOk()
        throws Exception {
        Long chatId = 1L;
        Long linkId = 2L;
        URI url = URI.create("url");
        OffsetDateTime updatedAt = OffsetDateTime.now();
        OffsetDateTime lastCheckTime = OffsetDateTime.now();
        RemoveLinkRequest requestBody = new RemoveLinkRequest(url);
        LinkResponse expectedResponse = new LinkResponse(linkId, url);

        Mockito.when(linkService.remove(chatId, requestBody.link())).thenReturn(
            new LinkDto(
                linkId,
                url,
                updatedAt,
                lastCheckTime
            )
        );

        mockMvc
            .perform(
                delete("/links")
                    .header("Tg-Chat-Id", chatId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestBody))
            )
            .andDo(print())
            .andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                content().json(objectMapper.writeValueAsString(expectedResponse))
            );

        Mockito.verify(linkService).remove(chatId, requestBody.link());
    }

    @Test
    void givenInValidIdAndBody_whenDeleteLink_thenReturnBadRequest() throws Exception {
        Long chatId = -1L;
        URI url = URI.create("url");
        RemoveLinkRequest requestBody = new RemoveLinkRequest(url);

        mockMvc
            .perform(
                delete("/links")
                    .header("Tg-Chat-Id", chatId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestBody))
            )
            .andDo(print())
            .andExpectAll(
                status().isBadRequest()
            );

        Mockito.verify(linkService, Mockito.times(0)).remove(chatId, requestBody.link());
    }

    @Test
    void givenNonExistentId_whenDeleteLink_thenReturnNotFound() throws Exception {
        Long chatId = 1L;
        URI url = URI.create("url");
        RemoveLinkRequest requestBody = new RemoveLinkRequest(url);

        Mockito.when(linkService.remove(chatId, requestBody.link())).thenThrow(LinkNotExistException.class);

        mockMvc
            .perform(
                delete("/links")
                    .header("Tg-Chat-Id", chatId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestBody))
            )
            .andDo(print())
            .andExpectAll(
                status().isNotFound()
            );

        Mockito.verify(linkService).remove(chatId, requestBody.link());
    }

    private ListLinksResponse convertDtoListToResponse(List<LinkDto> linkDtoList) {
        List<LinkResponse> linkResponses =
            linkDtoList
                .stream()
                .map(linkDto -> new LinkResponse(linkDto.id(), linkDto.url()))
                .toList();
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    @TestConfiguration
    static class ScrapperControllerImplTestConfig {
        @Bean
        public RateLimitBucketsCache rateLimitBucketsCache() {
            return new RateLimitBucketsCache(new ApplicationConfig.RateLimit(
                100L,
                100L,
                1L
            ));
        }
    }

}
