package edu.java.bot.links.linkparser;

import edu.java.bot.links.Link;
import edu.java.bot.links.linkparser.exceptions.InvalidURL;
import edu.java.bot.links.linkparser.exceptions.UnsupportedResourceURL;
import org.springframework.stereotype.Service;

@Service
public interface LinkParser {
    /**
     * Tries to parse a link from given string
     *
     * @param url - the string, from which the link will be parsed
     * @return Link with given an uri and correct link type
     * @throws UnsupportedResourceURL - if, application doesn't support links tracking from this resource
     */
    Link parseLink(String url) throws UnsupportedResourceURL, InvalidURL;
}
