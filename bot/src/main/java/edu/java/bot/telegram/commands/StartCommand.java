package edu.java.bot.telegram.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.telegram.persistence.ResourceDB;
import edu.java.exceptions.ApiErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@Component
public class StartCommand implements Command {
    private final ResourceDB resourceDB;
    private final ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "start using this bot";
    }

    @Override
    public String helpMessage() {
        return "Use this command to sign up in this tracking system to start using bot";
    }

    @Override
    public SendMessage handle(Update update) {
        long userId = update.message().chat().id();

        String response;
        try {
            response = scrapperClient.registerChat(userId);
        } catch (ApiErrorException e) {
            log.warn("Catch ApiErrorException");
            return new SendMessage(
                userId,
                e.getMessage()
            );
        }

        log.info("New user with id = %s and username = %s signed up".formatted(
            userId,
            update.message().chat().username()
        ));
        return new SendMessage(userId, response);

//        if (resourceDB.userExists(userId)) {
//            return new SendMessage(userId, ReplyMessages.USER_ALREADY_SIGNED_IN.getText());
//        }
//
//        resourceDB.addUser(userId);
//
//        log.info("New user with id = %s and username = %s signed up".formatted(
//            userId,
//            update.message().chat().username()
//        ));
//
//        return new SendMessage(userId, ReplyMessages.SUCCESSFUL_SIGNING_UP.getText());
    }
}
