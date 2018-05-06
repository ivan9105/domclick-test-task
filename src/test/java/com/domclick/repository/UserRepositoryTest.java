package com.domclick.repository;

import com.domclick.BaseTestSupport;
import com.domclick.app.Application;
import com.domclick.model.Account;
import com.domclick.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserRepositoryTest extends BaseTestSupport {

    @Test
    public void crudTest() {
        User user = createTestUser();

        user = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("It's impossible"));
        user.setMiddleName("Иванович");
        user = userRepository.save(user);

        Assert.assertEquals(user.getMiddleName(), "Иванович");
        userRepository.delete(user);

        Optional<User> userOptional = userRepository.findById(user.getId());
        Assert.assertTrue(!userOptional.isPresent());
    }


    @Test
    public void cascadeDropAccountsTest() {
        User user;
        Account account1, account2, account3;
        try (Session session = getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            user = createTestUser(session);
            account1 = createTestAccount(user, session);
            account2 = createTestAccount(user, session);
            account3 = createTestAccount(user, session);
            tx.commit();
        }


        try (Session session = getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.remove(reload(user, session));

            checkNotExists(session, user, account1, account2, account3);

            tx.commit();
        }
    }

    @Test
    public void versionTest() {
        User user = createTestUser();
        Assert.assertEquals(0, (long) user.getVersion());
        user = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("It's impossible"));
        user.setMiddleName("Иванович");
        user = userRepository.save(user);

        Assert.assertEquals(1, (long) user.getVersion());

        user = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("It's impossible"));
        user.setMiddleName("Петрович");
        user = userRepository.save(user);

        Assert.assertEquals(2, (long) user.getVersion());

        userRepository.delete(user);
    }
}
