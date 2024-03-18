package edu.java.bot.telegram.commands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.ApiErrorResponse;
import edu.java.bot.telegram.message.ReplyMessages;
import edu.java.exceptions.ApiErrorException;
import edu.java.scrapper.LinkResponse;
import edu.java.scrapper.ListLinksResponse;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;

public class ListCommandTest extends AbstractCommandTest {
    @Override
    void initCommand() {
        command = new ListCommand(scrapperClient);
    }

    @Test
    void givenUserWithNotEmptyList_whenHandle_thenReturnValidListMessage() {
        ListLinksResponse response = new ListLinksResponse(
            List.of(new LinkResponse(1L, URI.create("url"))),
            1
        );
        Mockito.when(scrapperClient.getLinks(chatId)).thenReturn(response);

        String successMessage = """
            List of your tracked resources:
            1) url
            """;
        SendMessage expectedSendMessage = new SendMessage(chatId, successMessage);
        var expectedParams = expectedSendMessage.getParameters();

        SendMessage actualSendMessage = command.handle(mockedUpdate);
        var actualParams = actualSendMessage.getParameters();

        assertThat(actualParams).isEqualTo(expectedParams);
    }

    @Test
    void givenUserWithEmptyList_whenHandle_thenReturnValidListMessage() {
        ListLinksResponse response = new ListLinksResponse(
            List.of(),
            0
        );
        Mockito.when(scrapperClient.getLinks(chatId)).thenReturn(response);

        SendMessage expectedSendMessage = new SendMessage(chatId, ReplyMessages.EMPTY_RESOURCE_LIST.getText());
        var expectedParams = expectedSendMessage.getParameters();

        SendMessage actualSendMessage = command.handle(mockedUpdate);
        var actualParams = actualSendMessage.getParameters();

        assertThat(actualParams).isEqualTo(expectedParams);
    }

    @Test
    void givenInvalidId_whenHandle_thenThrowApiErrorException() {
        ApiErrorException errorException = new ApiErrorException(new ApiErrorResponse(
            "description",
            "code",
            "exception name",
            "exception message",
            List.of()
        ));
        Mockito.when(scrapperClient.getLinks(chatId)).thenThrow(errorException);

        SendMessage expectedSendMessage = new SendMessage(
            chatId,
            errorException.getMessage()
        );
        var expectedParams = expectedSendMessage.getParameters();

        SendMessage actualSendMessage = command.handle(mockedUpdate);
        var actualParams = actualSendMessage.getParameters();

        assertThat(actualParams).isEqualTo(expectedParams);
    }
}
