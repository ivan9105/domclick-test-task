package com.domclick;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;


public class BaseTestSupport {
    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private HibernateSessionUtils hibernateSessionUtils;

    private SessionFactory sessionFactory;

    protected Account createTestAccount(User user) {
        return createTestAccount(newBigDecimal(0d), user);
    }

    protected Account createTestAccount(BigDecimal balance, User user) {
        Account account = getTestAccount(balance, user);
        account = accountRepository.save(account);
        return account;
    }

    protected User createTestUser() {
        return createTestUser("Федор", "Петров", "Петрович");
    }

    protected User createTestUser(String firstName, String lastName, String middleName) {
        User user = getTestUser(firstName, lastName, middleName);
        user = userRepository.save(user);
        return user;
    }

    protected Account createTestAccount(User user, Session session) {
        Account account = getTestAccount(newBigDecimal(0d), user);
        return session.find(Account.class, session.save(account));
    }

    protected User createTestUser(Session session) {
        User user = getTestUser("Федор", "Петров", "Петрович");
        return session.find(User.class, session.save(user));
    }

    protected SessionFactory getSessionFactory() {
        return hibernateSessionUtils.getSessionFactory();
    }

    protected BaseEntity reload(BaseEntity entity, Session session) {
        return session.find(entity.getClass(), entity.getId());
    }

    protected void checkNotExists(Session session, BaseEntity... entities) {
        for (BaseEntity entity : entities) {
            Assert.assertNull("Object must be removed", session.find(entity.getClass(), entity.getId()));
        }
    }

    private User getTestUser(String firstName, String lastName, String middleName) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMiddleName(middleName);
        return user;
    }

    private Account getTestAccount(BigDecimal balance, User user) {
        Account account = new Account();
        account.setUser(user);
        account.setBalance(balance);
        return account;
    }

    protected Account updateAccountBalance(Account account, BigDecimal value) {
        account.setBalance(value);
        return accountRepository.save(account);
    }

    protected BigDecimal newBigDecimal(Double value) {
        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }
}
