package edu.java.bot.telegram.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Default reply messages on certain user's actions
 */
@RequiredArgsConstructor
@Getter
public enum ReplyMessages {
    EMPTY_RESOURCE_LIST("List of your tracked resources is empty"),
    USER_ALREADY_SIGNED_IN("You're already signed in"),
    SUCCESSFUL_SIGNING_UP("You're successfully signed up"),
    ADD_NEW_RESOURCE("New resource was successfully added"),
    ALREADY_TRACKING("You are already tracking this resource"),
    UNTRACK_NON_TRACKED("You cannot remove resources, which you wasn't track"),
    SUCCESSFUL_REMOVE("Resource was successfully removed"),
    REQUIRE_SIGNING_IN("Please start with command /start to sign in first"),
    UNSUPPORTED_COMMAND(
        """
            Sorry, I don't know how to process this command,\s
            Please, use only allowed commands:\s
            /start, /help, /list, /track, /untrack"""),
    UNSUPPORTED_RESOURCE_URI(
        "This resource is unsupported. "
            + "Please input valid link for supported resource. Available resource right now: github, stackoverflow"),
    INVALID_URL("Url is invalid");

    private final String text;
}
