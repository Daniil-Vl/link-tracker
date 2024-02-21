package edu.java.bot.commands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.message.ReplyMessages;
import java.util.Map;
import edu.java.bot.persistence.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ListCommandTest extends CommandTest {
    @Override
    void initCommand() {
        command = new ListCommand(resourceDB);
    }

    @Test
    void givenDB_whenListFromUser_thenReturnValidListMessage() throws UserNotFoundException {
        initMockedUpdateWithId(userId);

        SendMessage expectedSendMessage = new SendMessage(
            userId,
            "List of your tracked resources: \n1) %s\n".formatted(tracedLink.url().toString())
        );
        Map<String, Object> expectedParameters = expectedSendMessage.getParameters();

        SendMessage actualSendMessage = command.handle(mockedUpdate);
        Map<String, Object> actualParameters = actualSendMessage.getParameters();

        assertThat(actualParameters).isEqualTo(expectedParameters);
    }

    @Test
    void givenDB_whenListFromUserWithEmptyList_thenReturnValidMessageForEmptyList() throws UserNotFoundException {
        long emptyListUserId = 2;
        initMockedUpdateWithId(emptyListUserId);
        resourceDB.addUser(emptyListUserId);

        SendMessage expectedSendMessage = new SendMessage(
            emptyListUserId,
            ReplyMessages.EMPTY_RESOURCE_LIST.getText()
        );
        Map<String, Object> expectedParameters = expectedSendMessage.getParameters();

        SendMessage actualSendMessage = command.handle(mockedUpdate);
        Map<String, Object> actualParameters = actualSendMessage.getParameters();

        assertThat(actualParameters).isEqualTo(expectedParameters);
    }
}
