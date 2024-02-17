package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.links.Link;
import edu.java.bot.links.linkparser.LinkParser;
import edu.java.bot.links.linkparser.exceptions.InvalidURL;
import edu.java.bot.links.linkparser.exceptions.UnsupportedResourceURL;
import edu.java.bot.message.ReplyMessages;
import edu.java.bot.persistence.ResourceDB;
import edu.java.bot.persistence.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@Component
public class TrackCommand implements Command {
    private final ResourceDB resourceDB;
    private final LinkParser linkParser;

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
    public SendMessage handle(Update update) {
        long userId = update.message().chat().id();
        String[] tokens = update.message().text().split(" ");

        if (tokens.length < 2) {
            log.warn("Received /track without arguments");
            return new SendMessage(
                userId,
                helpMessage()
            );
        }

        String resourceURI = tokens[1];

        try {
            Link link = linkParser.parseLink(resourceURI);
            boolean alreadyTracking = resourceDB.track(userId, link);

            if (alreadyTracking) {
                return new SendMessage(userId, ReplyMessages.ALREADY_TRACKING.getText());
            }

            log.info("Successfully added new resources = %s to the user with id = %s".formatted(resourceURI, userId));
            return new SendMessage(userId, ReplyMessages.ADD_NEW_RESOURCE.getText());
        } catch (UnsupportedResourceURL e) {
            log.warn("Tried to add unsupported resource with uri - %s".formatted(resourceURI));
            return new SendMessage(userId, ReplyMessages.UNSUPPORTED_RESOURCE_URI.getText());
        } catch (UserNotFoundException exception) {
            log.error("Tried to add resources to non-existent user with id = %s".formatted(userId));
            throw new RuntimeException(exception);
        } catch (InvalidURL e) {
            log.warn("Tried to add invalid uri - %s".formatted(resourceURI));
            return new SendMessage(userId, ReplyMessages.INVALID_URL.getText());
        }
    }
}
