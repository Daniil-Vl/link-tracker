package edu.java.bot.telegram.commands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.ApiErrorResponse;
import edu.java.bot.telegram.message.ReplyMessages;
import edu.java.exceptions.ApiErrorException;
import edu.java.scrapper.LinkResponse;
import edu.java.scrapper.RemoveLinkRequest;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;

public class UntrackCommandTest extends AbstractCommandTest {
    @Override
    void initCommand() {
        command = new UntrackCommand(scrapperClient);
    }

    @Test
    void givenValidMessage_whenHandle_thenReturnSuccessMessage() {
        URI url = URI.create("url");
        Mockito.when(mockedMessage.text()).thenReturn("/untrack %s".formatted(url.toString()));
        LinkResponse linkResponse = new LinkResponse(
            2L,
            url
        );
        String successMessage = "Resource url was removed";
        Mockito.when(scrapperClient.deleteLink(chatId, new RemoveLinkRequest(url))).thenReturn(linkResponse);

        SendMessage expectedSendMessage = new SendMessage(
            chatId,
            successMessage
        );
        var expectedParams = expectedSendMessage.getParameters();

        SendMessage actualSendMessage = command.handle(mockedUpdate);
        var actualParams = actualSendMessage.getParameters();

        assertThat(actualParams).isEqualTo(expectedParams);
    }

    @Test
    void givenMessageWithoutArgument_whenHandle_thenReturnHelpMessage() {
        Mockito.when(mockedMessage.text()).thenReturn("/untrack");

        SendMessage expectedSendMessage = new SendMessage(
            chatId,
            command.helpMessage()
        );
        var expectedParams = expectedSendMessage.getParameters();

        SendMessage actualSendMessage = command.handle(mockedUpdate);
        var actualParams = actualSendMessage.getParameters();

        assertThat(actualParams).isEqualTo(expectedParams);
    }

    @Test
    void givenInvalidUrl_whenHandle_thenReturnHelpMessage() {
        Mockito.when(mockedMessage.text()).thenReturn("/untrack \"\"");

        SendMessage expectedSendMessage = new SendMessage(
            chatId,
            ReplyMessages.INVALID_URL.getText()
        );
        var expectedParams = expectedSendMessage.getParameters();

        SendMessage actualSendMessage = command.handle(mockedUpdate);
        var actualParams = actualSendMessage.getParameters();

        assertThat(actualParams).isEqualTo(expectedParams);
    }

    @Test
    void givenNonExistentLink_whenHandle_thenReturnFailMessage() {
        URI url = URI.create("url");
        Mockito.when(mockedMessage.text()).thenReturn("/untrack %s".formatted(url.toString()));
        ApiErrorException errorException = new ApiErrorException(new ApiErrorResponse(
            "description",
            "code",
            "name",
            "message",
            List.of()
        ));
        Mockito.when(
            scrapperClient.deleteLink(chatId, new RemoveLinkRequest(url))
        ).thenThrow(errorException);

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
