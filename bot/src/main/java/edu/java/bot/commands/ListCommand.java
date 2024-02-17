package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.links.Link;
import edu.java.bot.message.ReplyMessages;
import edu.java.bot.persistence.ResourceDB;
import edu.java.bot.persistence.exceptions.UserNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@Component
public class ListCommand implements Command {
    private final ResourceDB resourceDB;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "show list of all tracked links";
    }

    @Override
    public String helpMessage() {
        return "help message for /list";
    }

    private String buildListMessage(List<Link> trackedResources) throws UserNotFoundException {
        StringBuilder replyMessageBuilder = new StringBuilder();
        replyMessageBuilder.append("List of your tracked resources: \n");

        for (int ind = 0; ind < trackedResources.size(); ind++) {
            Link link = trackedResources.get(ind);
            replyMessageBuilder.append("%d) %s\n".formatted(ind + 1, link.url().toString()));
        }

        return replyMessageBuilder.toString();
    }

    @Override
    public SendMessage handle(Update update) {
        long userId = update.message().chat().id();

        try {
            List<Link> trackedResources = resourceDB.getTrackedResources(userId);

            if (trackedResources.isEmpty()) {
                return new SendMessage(userId, ReplyMessages.EMPTY_RESOURCE_LIST.getText());
            }

            return new SendMessage(userId, buildListMessage(trackedResources));
        } catch (UserNotFoundException e) {
            log.error("Tried to get list of tracked resources of non-existent user with id = %s".formatted(userId));
            throw new RuntimeException(e);
        }
    }
}
