package edu.java.bot.links.linkparser;

import edu.java.bot.links.Link;
import edu.java.bot.links.LinkType;
import edu.java.bot.links.linkparser.exceptions.InvalidURL;
import edu.java.bot.links.linkparser.exceptions.UnsupportedResourceURL;
import java.net.URI;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

abstract class LinkParserTest {

    private LinkParser linkParser;

    static Stream<Arguments> provideLinks() {
        return Stream.of(
            arguments(
                "https://github.com/torvalds/linux",
                new Link(URI.create("https://github.com/torvalds/linux"), LinkType.GITHUB)
            ),
            arguments(
                "https://github.com/golang/go",
                new Link(URI.create("https://github.com/golang/go"), LinkType.GITHUB)
            ),
            arguments(
                "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c",
                new Link(
                    URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"),
                    LinkType.STACK_OVER_FLOW
                )
            )
        );
    }

    abstract LinkParser getInstance();

    @BeforeEach
    void init() {
        linkParser = getInstance();
    }

    @ParameterizedTest
    @MethodSource("provideLinks")
    void givenUrl_whenParseLink_thenReturnValidLink(String url, Link expectedLink)
        throws UnsupportedResourceURL, InvalidURL {
        assertThat(linkParser.parseLink(url)).isEqualTo(expectedLink);
    }
}
