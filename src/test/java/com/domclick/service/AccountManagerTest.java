package com.domclick.service;

import com.domclick.BaseTestSupport;
import com.domclick.app.Application;
import com.domclick.exception.BadRequestException;
import com.domclick.model.Account;
import com.domclick.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AccountManagerTest extends BaseTestSupport {
    @Autowired
    private AccountManager accountManager;

    private static volatile int optimisticCounter = 0;

    @Test
    public void transferTest() {
        User fromUser = createTestUser("Иван", "Долгорукий", "Петрович");
        User toUser = createTestUser("Федор", "Игнатов", "Алексеевич");

        Account fromAccount = createTestAccount(155.5, fromUser);
        Account toAccount = createTestAccount(300d, toUser);

        Assert.assertTrue(doTransfer(fromAccount, toAccount, 100d));

        fromAccount = accountRepository.findById(fromAccount.getId()).orElseThrow(() -> new RuntimeException("It's impossible"));
        toAccount = accountRepository.findById(toAccount.getId()).orElseThrow(() -> new RuntimeException("It's impossible"));

        Assert.assertEquals(fromAccount.getBalance(), 55.5d, 0d);
        Assert.assertEquals(toAccount.getBalance(), 400d, 0d);

        fromAccount = updateAccountBalance(fromAccount, 1000d);
        toAccount = updateAccountBalance(toAccount, 3000d);

        Assert.assertFalse(doTransfer(fromAccount, fromAccount, 100d));
        Assert.assertFalse(doTransfer(fromAccount, toAccount, 0d));
        Assert.assertFalse(doTransfer(fromAccount, toAccount, -1d));
        Assert.assertFalse(doTransfer(fromAccount, toAccount, 50000d));
        Assert.assertFalse(doTransfer(fromAccount, getFakeAccount(), 50d));

        userRepository.delete(fromUser);
        userRepository.delete(toUser);
    }

    @Test
    public void withdrawTest() {
        User user = createTestUser("Иван", "Долгорукий", "Петрович");
        Account account = createTestAccount(1000d, user);
        Assert.assertTrue(doWithdraw(account, 500d, 500d));
        Assert.assertTrue(doWithdraw(account, 450d, 50d));
        Assert.assertFalse(doWithdraw(account, 500d, 50d));
        Assert.assertFalse(doWithdraw(account, 0d, 50d));
        Assert.assertFalse(doWithdraw(account, -100d, 50d));
        Assert.assertFalse(doWithdraw(getFakeAccount(), 10d, 50d));
        Assert.assertTrue(doWithdraw(account, 50d, 0d));
        userRepository.delete(user);
    }

    @Test
    public void depositTest() {
        User user = createTestUser("Иван", "Долгорукий", "Петрович");
        Account account = createTestAccount(1000d, user);
        Assert.assertTrue(doDeposit(account, 500d, 1500d));
        Assert.assertTrue(doDeposit(account, 500d, 2000d));
        Assert.assertFalse(doDeposit(account, -1000d, 2000d));
        Assert.assertFalse(doDeposit(getFakeAccount(), 1000d, 2000d));
        userRepository.delete(user);
    }

    @Test
    public void optimisticTest() throws InterruptedException {
        User fromUser = createTestUser("Иван", "Долгорукий", "Петрович");
        User toUser = createTestUser("Федор", "Игнатов", "Алексеевич");

        Account fromAccount = createTestAccount(10000d, fromUser);
        Account toAccount = createTestAccount(3000d, toUser);

        final Long fromAccountId = fromAccount.getId();
        final Long toAccountId = toAccount.getId();

        int threadCount = 30;
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException ignore) {
                    }
                    accountManager.transfer(fromAccountId, toAccountId, 100d);
                } catch (BadRequestException ignore) {
                } catch (ObjectOptimisticLockingFailureException ole) {
                    updateOptimisticCounter();
                }
            }).start();
        }

        Thread.sleep(1000L);

        fromAccount = accountRepository.findById(fromAccount.getId()).orElseThrow(() -> new RuntimeException("It's impossible"));
        toAccount = accountRepository.findById(toAccount.getId()).orElseThrow(() -> new RuntimeException("It's impossible"));

        Assert.assertEquals(fromAccount.getBalance(), (10000d - (threadCount - optimisticCounter) * 100d), 0d);
        Assert.assertEquals(toAccount.getBalance(), (3000d + (threadCount - optimisticCounter) * 100d), 0d);
        System.out.println("Optimistic counter: " + optimisticCounter);

        userRepository.delete(fromUser);
        userRepository.delete(toUser);
    }

    private boolean doTransfer(Account fromAccount, Account toAccount, Double value) {
        try {
            accountManager.transfer(fromAccount.getId(), toAccount.getId(), value);
        } catch (BadRequestException bre) {
            return false;
        }
        return true;
    }

    private boolean doWithdraw(Account account, Double value, Double expected) {
        try {
            accountManager.withdraw(account.getId(), value);
        } catch (BadRequestException bre) {
            return false;
        }
        account = accountRepository.findById(account.getId()).orElseThrow(() -> new RuntimeException("It's impossible"));
        Assert.assertEquals(account.getBalance(), expected);
        return true;
    }

    private boolean doDeposit(Account account, Double value, Double expected) {
        try {
            accountManager.deposit(account.getId(), value);
        } catch (BadRequestException bre) {
            return false;
        }
        account = accountRepository.findById(account.getId()).orElseThrow(() -> new RuntimeException("It's impossible"));
        Assert.assertEquals(account.getBalance(), expected);
        return true;
    }

    private Account getFakeAccount() {
        Account fakeAccount = new Account();
        fakeAccount.setId(9999999999L);
        fakeAccount.setBalance(10000d);
        return fakeAccount;
    }

    private synchronized void updateOptimisticCounter() {
        optimisticCounter++;
    }
}
