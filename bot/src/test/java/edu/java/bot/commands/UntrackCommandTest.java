package edu.java.bot.commands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.message.ReplyMessages;
import edu.java.bot.persistence.exceptions.UserNotFoundException;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UntrackCommandTest extends CommandTest {
    @Override
    void initCommand() {
        command = new UntrackCommand(resourceDB, linkParser);
    }

    @Test
    void givenCommandWithoutArguments_whenHandleUnTrack_thenReturnHelpMessage() throws UserNotFoundException {
        initMockedUpdateWithId(userId);
        setMessageText("/untrack");

        SendMessage expectedSendMessage = new SendMessage(
            userId,
            command.helpMessage()
        );
        Map<String, Object> expectedParameters = expectedSendMessage.getParameters();

        SendMessage actualSendMessage = command.handle(mockedUpdate);
        Map<String, Object> actualParameters = actualSendMessage.getParameters();

        assertThat(actualParameters).isEqualTo(expectedParameters);
    }

    @Test
    void givenUnsupportedURL_whenHandleUnTrack_thenReturnUnsupportedURLMessage() throws UserNotFoundException {
        initMockedUpdateWithId(userId);
        setMessageText("/untrack unsupported_resource");

        SendMessage expectedSendMessage = new SendMessage(
            userId,
            ReplyMessages.UNSUPPORTED_RESOURCE_URI.getText()
        );
        Map<String, Object> expectedParameters = expectedSendMessage.getParameters();

        SendMessage actualSendMessage = command.handle(mockedUpdate);
        Map<String, Object> actualParameters = actualSendMessage.getParameters();

        assertThat(actualParameters).isEqualTo(expectedParameters);
    }

    @Test
    void givenInvalidURL_whenHandleUnTrack_thenReturnInvalidURLMessage() throws UserNotFoundException {
        initMockedUpdateWithId(userId);
        setMessageText("/track \"github\"");

        SendMessage expectedSendMessage = new SendMessage(
            userId,
            ReplyMessages.INVALID_URL.getText()
        );
        Map<String, Object> expectedParameters = expectedSendMessage.getParameters();

        SendMessage actualSendMessage = command.handle(mockedUpdate);
        Map<String, Object> actualParameters = actualSendMessage.getParameters();

        assertThat(actualParameters).isEqualTo(expectedParameters);
    }

    @Test
    void givenNonTrackingURL_whenHandleUnTrack_thenReturnUntrackNonTrackedMessage() throws UserNotFoundException {
        initMockedUpdateWithId(userId);
        setMessageText("/track " + newLink.url().toString());

        SendMessage expectedSendMessage = new SendMessage(
            userId,
            ReplyMessages.UNTRACK_NON_TRACKED.getText()
        );
        Map<String, Object> expectedParameters = expectedSendMessage.getParameters();

        SendMessage actualSendMessage = command.handle(mockedUpdate);
        Map<String, Object> actualParameters = actualSendMessage.getParameters();

        assertThat(actualParameters).isEqualTo(expectedParameters);
    }

    @Test
    void givenTrackedValidURL_whenHandleUnTrack_thenRemoveResourceAndReturnSuccessfulRemoveMessage()
        throws UserNotFoundException {
        initMockedUpdateWithId(userId);
        setMessageText("/track " + tracedLink.url().toString());

        SendMessage expectedSendMessage = new SendMessage(
            userId,
            ReplyMessages.SUCCESSFUL_REMOVE.getText()
        );
        Map<String, Object> expectedParameters = expectedSendMessage.getParameters();

        SendMessage actualSendMessage = command.handle(mockedUpdate);
        Map<String, Object> actualParameters = actualSendMessage.getParameters();

        assertThat(actualParameters).isEqualTo(expectedParameters);
        assertThat(resourceDB.getTrackedResources(userId).contains(newLink)).isFalse();
    }
}
