package com.domclick.repository;

import com.domclick.BaseTestSupport;
import com.domclick.app.AppApplication;
import com.domclick.model.Account;
import com.domclick.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApplication.class)
public class AccountRepositoryTest extends BaseTestSupport {
    private User user;

    @Before
    public void init() {
        user = userRepository.findById(0L).orElseThrow(
                () -> new IllegalStateException("Datasource do not initialize"));
    }

    @Test
    public void crudTest() {
        Account account = createTestAccount(user);

        account = accountRepository.findById(account.getId()).orElseThrow(() -> new RuntimeException("It's impossible"));
        account.setBalance(new BigDecimal(50000d));
        account = accountRepository.save(account);
        account = accountRepository.findById(account.getId()).orElseThrow(() -> new RuntimeException("It's impossible"));
        Assert.assertEquals(account.getBalance(), newBigDecimal(50000d));

        accountRepository.delete(account);
        Optional<Account> accountOptional = accountRepository.findById(account.getId());
        Assert.assertTrue(!accountOptional.isPresent());
    }
}
