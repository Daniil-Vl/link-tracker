package edu.java.bot.telegram.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.scrapper.ScrapperClient;
import edu.java.bot.telegram.message.ReplyMessages;
import edu.java.exceptions.ApiErrorException;
import edu.java.scrapper.LinkResponse;
import edu.java.scrapper.ListLinksResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@Component
public class ListCommand implements Command {
    private final ScrapperClient scrapperClient;

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

    private String buildListMessage(List<LinkResponse> trackedResources) {
        StringBuilder replyMessageBuilder = new StringBuilder();
        replyMessageBuilder.append("List of your tracked resources: \n");

        for (int ind = 0; ind < trackedResources.size(); ind++) {
            LinkResponse linkResponse = trackedResources.get(ind);
            replyMessageBuilder.append("%d) %s\n".formatted(ind + 1, linkResponse.url().toString()));
        }

        return replyMessageBuilder.toString();
    }

    @Override
    public SendMessage handle(Update update) {
        long userId = update.message().chat().id();

        ListLinksResponse links;
        try {
            links = scrapperClient.getLinks(userId);
        } catch (ApiErrorException e) {
            log.warn("Catch ApiErrorException");
            return new SendMessage(
                userId,
                e.getMessage()
            );
        }

        if (links.links().isEmpty()) {
            return new SendMessage(userId, ReplyMessages.EMPTY_RESOURCE_LIST.getText());
        }

        return new SendMessage(
            userId,
            buildListMessage(links.links())
        );
    }
}
