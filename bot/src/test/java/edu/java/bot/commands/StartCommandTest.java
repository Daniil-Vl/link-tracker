package edu.java.bot.commands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.telegram.commands.StartCommand;
import edu.java.bot.telegram.message.ReplyMessages;
import java.util.Map;
import edu.java.bot.telegram.persistence.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class StartCommandTest extends CommandTest {

    @Override
    void initCommand() {
        command = new StartCommand(resourceDB);
    }

    @Test
    void givenDB_whenExistingUserStart_thenReturnReplyMessageAlreadySignedIn() throws UserNotFoundException {
        initMockedUpdateWithId(userId);

        SendMessage expectedSendMessage = new SendMessage(
            userId,
            ReplyMessages.USER_ALREADY_SIGNED_IN.getText()
        );
        Map<String, Object> expectedParameters = expectedSendMessage.getParameters();

        SendMessage actualSendMessage = command.handle(mockedUpdate);
        Map<String, Object> actualParameters = actualSendMessage.getParameters();

        assertThat(actualParameters).isEqualTo(expectedParameters);
    }

    @Test
    void givenDB_whenNewUserStart_thenSuccessfullyAddNewUserToDB() throws UserNotFoundException {
        initMockedUpdateWithId(nonExistentUserId);

        SendMessage expectedSendMessage = new SendMessage(
            nonExistentUserId,
            ReplyMessages.SUCCESSFUL_SIGNING_UP.getText()
        );
        Map<String, Object> expectedParameters = expectedSendMessage.getParameters();

        SendMessage actualSendMessage = command.handle(mockedUpdate);
        Map<String, Object> actualParameters = actualSendMessage.getParameters();

        assertThat(actualParameters).isEqualTo(expectedParameters);
    }
}
