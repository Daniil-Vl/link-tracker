package edu.java.domain;

import edu.java.domain.jdbc.ChatRepositoryJdbcImpl;

public class ChatRepositoryJdbcImplTest extends ChatRepositoryTest {
    @Override
    void initRepository() {
        chatRepository = new ChatRepositoryJdbcImpl(jdbcClient);
    }
}
