package edu.java.domain.jdbc;

import edu.java.domain.SubscriptionRepositoryTest;

public class SubscriptionRepositoryJdbcImplTest extends SubscriptionRepositoryTest {
    @Override
    public void initRepository() {
        subscriptionRepository = new SubscriptionRepositoryJdbcImpl(jdbcClient);
    }
}
