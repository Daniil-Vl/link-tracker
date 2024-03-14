package edu.java.bot.telegram.links;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LinkType {
    GITHUB("github.com"),
    STACK_OVER_FLOW("stackoverflow.com");

    private final String host;
}
