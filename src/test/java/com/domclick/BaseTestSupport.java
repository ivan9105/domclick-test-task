package com.domclick;

import com.domclick.model.Account;
import com.domclick.model.BaseEntity;
import com.domclick.model.User;
import com.domclick.repository.AccountRepository;
import com.domclick.repository.UserRepository;
import com.domclick.utils.HibernateSessionUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;


public class BaseTestSupport {
    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private HibernateSessionUtils hibernateSessionUtils;

    private SessionFactory sessionFactory;

    protected Account createTestAccount(User user) {
        Account account = getTestAccount(user);
        account = accountRepository.save(account);
        return account;
    }

    protected User createTestUser() {
        User user = getTestUser();
        user = userRepository.save(user);
        return user;
    }

    protected Account createTestAccount(User user, Session session) {
        Account account = getTestAccount(user);
        return session.find(Account.class, session.save(account));
    }

    protected User createTestUser(Session session) {
        User user = getTestUser();
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

    private User getTestUser() {
        User user = new User();
        user.setFirstName("Федор");
        user.setLastName("Петров");
        user.setMiddleName("Петрович");
        return user;
    }

    private Account getTestAccount(User user) {
        Account account = new Account();
        account.setUser(user);
        account.setBalance(0.0);
        return account;
    }
}
