package edu.java.domain;

import edu.java.domain.jooq.LinkRepositoryJooqImpl;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;

public class LinkRepositoryJooqImplTest extends LinkRepositoryTest {
    @Autowired
    private DSLContext dslContext;

    @Override
    public void initRepository() {
        linkRepository = new LinkRepositoryJooqImpl(dslContext);
    }
}
