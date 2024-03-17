package edu.java.controller;

import edu.java.ApiErrorResponse;
import edu.java.exceptions.ChatNotExistException;
import edu.java.exceptions.LinkNotExistException;
import edu.java.scrapper.AddLinkRequest;
import edu.java.scrapper.LinkResponse;
import edu.java.scrapper.ListLinksResponse;
import edu.java.scrapper.RemoveLinkRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Scrapper Controller")
public interface ScrapperController {
    @Operation(summary = "Зарегистрировать чат")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Чат зарегистрирован"),
        @ApiResponse(responseCode = "400",
                     description = "Некорректные данные запроса",
                     content = @Content(
                         mediaType = MediaType.APPLICATION_JSON_VALUE,
                         schema = @Schema(implementation = ApiErrorResponse.class)
                     ))
    })
    @PostMapping("/tg-chat/{id}")
    String registerChat(@PathVariable @PositiveOrZero Long id);

    @Operation(summary = "Удалить чат")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Чат успешно удален"),
        @ApiResponse(responseCode = "400",
                     description = "Некорректные параметры запроса",
                     content = @Content(
                         mediaType = MediaType.APPLICATION_JSON_VALUE,
                         schema = @Schema(implementation = ApiErrorResponse.class)
                     )),
        @ApiResponse(responseCode = "404",
                     description = "Чат не существует",
                     content = @Content(
                         mediaType = MediaType.APPLICATION_JSON_VALUE,
                         schema = @Schema(implementation = ApiErrorResponse.class)
                     ))
    })
    @DeleteMapping("/tg-chat/{id}")
    String deleteChat(@PathVariable @PositiveOrZero Long id) throws ChatNotExistException;

    @Operation(summary = "Получить все отслеживаемые ссылки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Ссылки успешно получены",
                     content = @Content(
                         mediaType = MediaType.APPLICATION_JSON_VALUE,
                         schema = @Schema(implementation = ListLinksResponse.class)
                     )),
        @ApiResponse(responseCode = "400",
                     description = "Некорректные параметры запроса",
                     content = @Content(
                         mediaType = MediaType.APPLICATION_JSON_VALUE,
                         schema = @Schema(implementation = ApiErrorResponse.class)
                     ))
    })
    @GetMapping("/links")
    ListLinksResponse getLinks(@RequestHeader(name = "Tg-Chat-Id") @PositiveOrZero Long tgChatId);

    @Operation(summary = "Добавить отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Ссылка успешно добавлена",
                     content = @Content(
                         mediaType = MediaType.APPLICATION_JSON_VALUE,
                         schema = @Schema(implementation = LinkResponse.class)
                     )),
        @ApiResponse(responseCode = "400",
                     description = "Некорректные параметры запроса",
                     content = @Content(
                         mediaType = MediaType.APPLICATION_JSON_VALUE,
                         schema = @Schema(implementation = ApiErrorResponse.class)
                     )
        )
    })
    @PostMapping("/links")
    LinkResponse addLink(
        @RequestHeader(name = "Tg-Chat-Id") @PositiveOrZero Long tgChatId,
        @RequestBody AddLinkRequest addLinkRequest
    );

    @Operation(summary = "Убрать отслеживание ссылки")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                     description = "Ссылка успешно убрана",
                     content = @Content(
                         mediaType = MediaType.APPLICATION_JSON_VALUE,
                         schema = @Schema(implementation = LinkResponse.class)
                     )),
        @ApiResponse(responseCode = "400",
                     description = "Некорректные параметры запроса",
                     content = @Content(
                         mediaType = MediaType.APPLICATION_JSON_VALUE,
                         schema = @Schema(implementation = ApiErrorResponse.class)
                     )),
        @ApiResponse(responseCode = "404",
                     description = "Ссылка не найдена",
                     content = @Content(
                         mediaType = MediaType.APPLICATION_JSON_VALUE,
                         schema = @Schema(implementation = ApiErrorResponse.class)
                     ))
    })
    @DeleteMapping("/links")
    LinkResponse deleteLinks(
        @RequestHeader(name = "Tg-Chat-Id") @PositiveOrZero Long tgChatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    ) throws ChatNotExistException, LinkNotExistException;
}
