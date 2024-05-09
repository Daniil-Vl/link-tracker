package edu.java.domain.jpa;

import edu.java.domain.LinkRepositoryTest;
import edu.java.domain.jpa.jpa_repositories.JpaLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class LinkRepositoryJpaImplTest extends LinkRepositoryTest {
    @Autowired
    private JpaLinkRepository jpaLinkRepository;

    @Override
    public void initRepository() {
        linkRepository = new LinkRepositoryJpaImpl(jpaLinkRepository);
    }
}
