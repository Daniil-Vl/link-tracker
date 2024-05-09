package edu.java.domain.jpa;

import edu.java.domain.ChatRepositoryTest;
import edu.java.domain.jpa.jpa_repositories.JpaChatRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ChatRepositoryJpaImplTest extends ChatRepositoryTest {
    @Autowired
    private JpaChatRepository jpaChatRepository;

    @Override
    public void initRepository() {
        chatRepository = new ChatRepositoryJpaImpl(jpaChatRepository);
    }
}
