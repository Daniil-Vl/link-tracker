package edu.java.domain;

import edu.java.domain.jdbc.LinkRepositoryJdbcImpl;

public class LinkRepositoryJdbcImplTest extends LinkRepositoryTest {
    @Override
    public void initRepository() {
        linkRepository = new LinkRepositoryJdbcImpl(jdbcClient);
    }
}
