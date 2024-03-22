package edu.java.domain;

import edu.java.domain.jooq.ChatRepositoryJooqImpl;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ChatRepositoryJooqImplTest extends ChatRepositoryTest {
    @Autowired
    private DSLContext dslContext;

    @Override
    void initRepository() {
        chatRepository = new ChatRepositoryJooqImpl(dslContext);
    }
}
