package edu.java.bot.telegram.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.client.scrapper.ScrapperClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
abstract class AbstractCommandTest {
    @Mock
    protected Chat mockedChat;
    @Mock
    protected Message mockedMessage;
    @Mock
    protected Update mockedUpdate;
    @Mock
    protected ScrapperClient scrapperClient;
    protected Command command;
    long chatId = 1;

    @BeforeEach
    void setUp() {
        initMockedUpdate(1);
        initCommand();
    }

    @AfterEach
    void tearDown() {
    }

    abstract void initCommand();

    void initMockedUpdate(long chatId) {
        Mockito.when(mockedUpdate.message()).thenReturn(mockedMessage);
        Mockito.when(mockedMessage.chat()).thenReturn(mockedChat);
        Mockito.when(mockedChat.id()).thenReturn(chatId);
    }
}
