package edu.java.bot.links.linkparser;

import edu.java.bot.links.Link;
import edu.java.bot.links.LinkType;
import edu.java.bot.links.linkparser.exceptions.InvalidURL;
import edu.java.bot.links.linkparser.exceptions.UnsupportedResourceURL;
import java.net.URI;
import java.net.URISyntaxException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class LinkParserImpl implements LinkParser {
    @Override
    public Link parseLink(@NotNull String url) throws UnsupportedResourceURL, InvalidURL {
        URI uri;

        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new InvalidURL();
        }

        for (LinkType linkType : LinkType.values()) {
            if (linkType.getHost().equals(uri.getHost())) {
                return new Link(uri, linkType);
            }
        }

        throw new UnsupportedResourceURL();
    }
}
