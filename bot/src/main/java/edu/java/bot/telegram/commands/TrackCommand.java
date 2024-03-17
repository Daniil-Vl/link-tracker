package edu.java.bot.telegram.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.telegram.links.linkparser.LinkParser;
import edu.java.bot.telegram.persistence.ResourceDB;
import edu.java.bot.telegram.persistence.exceptions.UserNotFoundException;
import edu.java.exceptions.ApiErrorException;
import edu.java.scrapper.LinkResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@Component
public class TrackCommand implements Command {
    private final ResourceDB resourceDB;
    private final LinkParser linkParser;
    private final ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "start tracking content";
    }

    @Override
    public String helpMessage() {
        return "Use this command to track resources. \nSyntax - /track <resource_name>";
    }

    @SuppressWarnings("ReturnCount")
    @Override
    public SendMessage handle(Update update) throws UserNotFoundException {
        long userId = update.message().chat().id();
        String[] tokens = update.message().text().split(" ");

//        if (!resourceDB.userExists(userId)) {
//            throw new UserNotFoundException("Cannot use /track command for unauthenticated user");
//        }

        if (tokens.length < 2) {
            log.warn("Received /track without arguments");
            return new SendMessage(userId, helpMessage());
        }

        String resourceURI = tokens[1];
        LinkResponse response;

        try {
            response = scrapperClient.addLink(
                userId,
                URI.create(resourceURI)
            );
        } catch (ApiErrorException e) {
            log.warn("Catch ApiErrorException");
            return new SendMessage(
                userId,
                e.getMessage()
            );
        }

        return new SendMessage(
            userId,
            "New resource added: %s".formatted(response.url().toString())
        );

        // TODO: Remove commented code
//
//        try {
//            Link link = linkParser.parseLink(resourceURI);
//            boolean alreadyTracking = resourceDB.track(userId, link);
//
//            if (alreadyTracking) {
//                return new SendMessage(userId, ReplyMessages.ALREADY_TRACKING.getText());
//            }
//
//            log.info("Successfully added new resources = %s to the user with id = %s".formatted(resourceURI, userId));
//
//            return new SendMessage(userId, ReplyMessages.ADD_NEW_RESOURCE.getText());
//        } catch (UnsupportedResourceURL e) {
//            log.warn("Tried to add unsupported resource with uri - %s".formatted(resourceURI));
//            return new SendMessage(userId, ReplyMessages.UNSUPPORTED_RESOURCE_URI.getText());
//        } catch (InvalidURL e) {
//            log.warn("Tried to add invalid uri - %s".formatted(resourceURI));
//            return new SendMessage(userId, ReplyMessages.INVALID_URL.getText());
//        }
    }
}
