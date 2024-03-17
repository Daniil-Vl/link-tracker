package edu.java.bot.commands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.telegram.commands.TrackCommand;
import edu.java.bot.telegram.message.ReplyMessages;
import edu.java.bot.telegram.persistence.exceptions.UserNotFoundException;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TrackCommandTest extends CommandTest {
    @Override
    void initCommand() {
//        command = new TrackCommand(resourceDB, linkParser);

    }

//    @Test
//    void givenCommandWithoutArguments_whenHandleTrack_thenReturnHelpMessage() throws UserNotFoundException {
//        initMockedUpdateWithId(userId);
//        setMessageText("/track");
//
//        SendMessage expectedSendMessage = new SendMessage(
//            userId,
//            command.helpMessage()
//        );
//        Map<String, Object> expectedParameters = expectedSendMessage.getParameters();
//
//        SendMessage actualSendMessage = command.handle(mockedUpdate);
//        Map<String, Object> actualParameters = actualSendMessage.getParameters();
//
//        assertThat(actualParameters).isEqualTo(expectedParameters);
//    }
//
//    @Test
//    void givenUnsupportedURL_whenHandleTrack_thenReturnUnsupportedURLMessage() throws UserNotFoundException {
//        initMockedUpdateWithId(userId);
//        setMessageText("/track unsupported_resource");
//
//        SendMessage expectedSendMessage = new SendMessage(
//            userId,
//            ReplyMessages.UNSUPPORTED_RESOURCE_URI.getText()
//        );
//        Map<String, Object> expectedParameters = expectedSendMessage.getParameters();
//
//        SendMessage actualSendMessage = command.handle(mockedUpdate);
//        Map<String, Object> actualParameters = actualSendMessage.getParameters();
//
//        assertThat(actualParameters).isEqualTo(expectedParameters);
//    }
//
//    @Test
//    void givenInvalidURL_whenHandleTrack_thenReturnInvalidURLMessage() throws UserNotFoundException {
//        initMockedUpdateWithId(userId);
//        setMessageText("/track \"github\"");
//
//        SendMessage expectedSendMessage = new SendMessage(
//            userId,
//            ReplyMessages.INVALID_URL.getText()
//        );
//        Map<String, Object> expectedParameters = expectedSendMessage.getParameters();
//
//        SendMessage actualSendMessage = command.handle(mockedUpdate);
//        Map<String, Object> actualParameters = actualSendMessage.getParameters();
//
//        assertThat(actualParameters).isEqualTo(expectedParameters);
//    }
//
//    @Test
//    void givenAlreadyTrackingURL_whenHandleTrack_thenReturnAlreadyTrackingMessage() throws UserNotFoundException {
//        initMockedUpdateWithId(userId);
//        setMessageText("/track " + tracedLink.url().toString());
//
//        SendMessage expectedSendMessage = new SendMessage(
//            userId,
//            ReplyMessages.ALREADY_TRACKING.getText()
//        );
//        Map<String, Object> expectedParameters = expectedSendMessage.getParameters();
//
//        SendMessage actualSendMessage = command.handle(mockedUpdate);
//        Map<String, Object> actualParameters = actualSendMessage.getParameters();
//
//        assertThat(actualParameters).isEqualTo(expectedParameters);
//    }
//
//    @Test
//    void givenNewValidURL_whenHandleTrack_thenAddResourceAndReturnAddNewResourceMessage() throws UserNotFoundException {
//        initMockedUpdateWithId(userId);
//        setMessageText("/track " + newLink.url().toString());
//
//        SendMessage expectedSendMessage = new SendMessage(
//            userId,
//            ReplyMessages.ADD_NEW_RESOURCE.getText()
//        );
//        Map<String, Object> expectedParameters = expectedSendMessage.getParameters();
//
//        SendMessage actualSendMessage = command.handle(mockedUpdate);
//        Map<String, Object> actualParameters = actualSendMessage.getParameters();
//
//        assertThat(actualParameters).isEqualTo(expectedParameters);
//        assertThat(resourceDB.getTrackedResources(userId).contains(newLink)).isTrue();
//    }
}
