package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.telegram.commands.Command;
import edu.java.bot.telegram.links.Link;
import edu.java.bot.telegram.links.LinkType;
import edu.java.bot.telegram.links.linkparser.LinkParser;
import edu.java.bot.telegram.links.linkparser.LinkParserImpl;
import edu.java.bot.telegram.persistence.InMemoryResourceDB;
import edu.java.bot.telegram.persistence.ResourceDB;
import edu.java.bot.telegram.persistence.exceptions.UserNotFoundException;
import java.net.URI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
abstract class CommandTest {
    static long userId = 1;
    static ResourceDB resourceDB;
    static LinkParser linkParser;
    static Link tracedLink;
    static Link newLink;
    long nonExistentUserId = 2;
    @Mock
    Chat mockedChat;
    @Mock
    Message mockedMessage;
    @Mock
    Update mockedUpdate;

    Command command;

    @BeforeAll
    static void initResourceDB() throws UserNotFoundException {
        resourceDB = new InMemoryResourceDB();
        tracedLink = new Link(URI.create("https://github.com/traced"), LinkType.GITHUB);
        newLink = new Link(URI.create("https://github.com/untraced"), LinkType.GITHUB);

        resourceDB.addUser(userId);
        resourceDB.track(userId, tracedLink);
    }

    @BeforeAll
    static void initLinkParser() {
        linkParser = new LinkParserImpl();
    }

    @BeforeEach
    abstract void initCommand();

    //        @BeforeEach
    void initMockedUpdateWithId(long id) {
        Mockito.when(mockedUpdate.message()).thenReturn(mockedMessage);
        Mockito.when(mockedMessage.chat()).thenReturn(mockedChat);
        Mockito.when(mockedChat.id()).thenReturn(id);
    }

    void setMessageText(String text) {
        Mockito.when(mockedMessage.text()).thenReturn(text);
    }
}
