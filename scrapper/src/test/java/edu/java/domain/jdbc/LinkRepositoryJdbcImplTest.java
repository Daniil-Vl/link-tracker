package edu.java.domain.jdbc;

import edu.java.domain.LinkRepositoryTest;
import edu.java.domain.jdbc.LinkRepositoryJdbcImpl;

public class LinkRepositoryJdbcImplTest extends LinkRepositoryTest {
    @Override
    public void initRepository() {
        linkRepository = new LinkRepositoryJdbcImpl(jdbcClient);
    }
}
