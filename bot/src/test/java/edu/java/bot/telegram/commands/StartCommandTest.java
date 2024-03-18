package edu.java.bot.telegram.commands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.ApiErrorResponse;
import edu.java.exceptions.ApiErrorException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;

public class StartCommandTest extends AbstractCommandTest {
    @Override
    void initCommand() {
        command = new StartCommand(scrapperClient);
    }

    @Test
    void givenValidId_whenHandle_thenReturnSuccessMessage() {
        String successMessage = "Chat has been successfully registered";
        Mockito.when(scrapperClient.registerChat(chatId)).thenReturn(successMessage);

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
    void givenInvalidId_whenHandle_thenThrowApiErrorException() {
        ApiErrorException errorException = new ApiErrorException(new ApiErrorResponse(
            "description",
            "code",
            "exception name",
            "exception message",
            List.of()
        ));
        Mockito.when(scrapperClient.registerChat(chatId)).thenThrow(errorException);

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
