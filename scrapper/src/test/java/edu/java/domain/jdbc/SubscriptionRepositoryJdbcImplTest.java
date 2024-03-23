package edu.java.domain.jdbc;

import edu.java.domain.SubscriptionRepositoryTest;
import edu.java.domain.jdbc.SubscriptionRepositoryJdbcImpl;

public class SubscriptionRepositoryJdbcImplTest extends SubscriptionRepositoryTest {
    @Override
    public void initRepository() {
        subscriptionRepository = new SubscriptionRepositoryJdbcImpl(jdbcClient);
    }
}
