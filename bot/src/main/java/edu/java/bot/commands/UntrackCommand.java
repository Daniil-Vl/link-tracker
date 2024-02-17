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
public class UntrackCommand implements Command {
    private final ResourceDB resourceDB;
    private final LinkParser linkParser;

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "stop content tracking";
    }

    @Override
    public String helpMessage() {
        return "Use this command to remove resources. \nSyntax - /untrack <resource_name>";
    }

    @SuppressWarnings("ReturnCount")
    @Override
    public SendMessage handle(Update update) {
        long userId = update.message().chat().id();
        String[] tokens = update.message().text().split(" ");

        if (tokens.length < 2) {
            log.warn("Received /untrack without arguments");
            return new SendMessage(
                userId,
                helpMessage()
            );
        }
        String resourceURI = update.message().text().split(" ")[1];

        try {
            Link link = linkParser.parseLink(resourceURI);
            boolean wasPreviouslyTracked = resourceDB.untrack(userId, link);

            if (!wasPreviouslyTracked) {
                return new SendMessage(userId, ReplyMessages.UNTRACK_NON_TRACKED.getText());
            }

            log.info("Successfully remove resources = %s from the user with id = %s".formatted(resourceURI, userId));
            return new SendMessage(userId, ReplyMessages.SUCCESSFUL_REMOVE.getText());
        } catch (UserNotFoundException exception) {
            log.error("Tried to remove resources from non-existent user with id = %s".formatted(userId));
            throw new RuntimeException(exception);
        } catch (UnsupportedResourceURL e) {
            log.warn("Tried to remove unsupported resource with uri - %s".formatted(tokens[1]));
            return new SendMessage(userId, ReplyMessages.UNSUPPORTED_RESOURCE_URI.getText());
        } catch (InvalidURL e) {
            log.warn("Tried to remove invalid uri - %s".formatted(resourceURI));
            return new SendMessage(userId, ReplyMessages.INVALID_URL.getText());
        }
    }
}
