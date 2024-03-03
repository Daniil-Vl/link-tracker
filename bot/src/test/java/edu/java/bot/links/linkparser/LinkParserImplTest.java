package edu.java.bot.links.linkparser;

import edu.java.bot.telegram.links.linkparser.LinkParser;
import edu.java.bot.telegram.links.linkparser.LinkParserImpl;

public class LinkParserImplTest extends LinkParserTest {
    @Override
    LinkParser getInstance() {
        return new LinkParserImpl();
    }
}
