package edu.java.domain.jooq;

import edu.java.domain.SubscriptionRepositoryTest;
import edu.java.domain.jooq.SubscriptionRepositoryJooqImpl;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;

public class SubscriptionRepositoryJooqImplTest extends SubscriptionRepositoryTest {
    @Autowired
    private DSLContext dslContext;

    @Override
    public void initRepository() {
        subscriptionRepository = new SubscriptionRepositoryJooqImpl(dslContext);
    }
}
