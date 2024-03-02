package edu.java.bot.links.linkparser;

public class LinkParserImplTest extends LinkParserTest {
    @Override
    LinkParser getInstance() {
        return new LinkParserImpl();
    }
}
