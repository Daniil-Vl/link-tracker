package edu.java.domain.jooq;

import edu.java.domain.ChatRepositoryTest;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ChatRepositoryJooqImplTest extends ChatRepositoryTest {
    @Autowired
    private DSLContext dslContext;

    @Override
    public void initRepository() {
        chatRepository = new ChatRepositoryJooqImpl(dslContext);
    }
}
