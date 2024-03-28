package edu.java.domain.jdbc;

import edu.java.domain.ChatRepositoryTest;

public class ChatRepositoryJdbcImplTest extends ChatRepositoryTest {
    @Override
    public void initRepository() {
        chatRepository = new ChatRepositoryJdbcImpl(jdbcClient);
    }
}
