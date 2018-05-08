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

import java.math.BigDecimal;
import java.util.Random;

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

        Account fromAccount = createTestAccount(newBigDecimal(155.5), fromUser);
        Account toAccount = createTestAccount(newBigDecimal(300d), toUser);

        Assert.assertTrue(doTransfer(fromAccount, toAccount, newBigDecimal(100d)));

        fromAccount = accountRepository.findById(fromAccount.getId()).orElseThrow(() -> new RuntimeException("It's impossible"));
        toAccount = accountRepository.findById(toAccount.getId()).orElseThrow(() -> new RuntimeException("It's impossible"));

        Assert.assertEquals(fromAccount.getBalance(), newBigDecimal(55.5d));
        Assert.assertEquals(toAccount.getBalance(), newBigDecimal(400d));

        fromAccount = updateAccountBalance(fromAccount, newBigDecimal(1000d));
        toAccount = updateAccountBalance(toAccount, newBigDecimal(3000d));

        Assert.assertFalse(doTransfer(fromAccount, fromAccount, newBigDecimal(100d)));
        Assert.assertFalse(doTransfer(fromAccount, toAccount, newBigDecimal(0d)));
        Assert.assertFalse(doTransfer(fromAccount, toAccount, newBigDecimal(-1d)));
        Assert.assertFalse(doTransfer(fromAccount, toAccount, newBigDecimal(50000d)));
        Assert.assertFalse(doTransfer(fromAccount, getFakeAccount(), newBigDecimal(50d)));

        userRepository.delete(fromUser);
        userRepository.delete(toUser);
    }

    @Test
    public void withdrawTest() {
        User user = createTestUser("Иван", "Долгорукий", "Петрович");
        Account account = createTestAccount(newBigDecimal(1000d), user);
        Assert.assertTrue(doWithdraw(account, newBigDecimal(500d), newBigDecimal(500d)));
        Assert.assertTrue(doWithdraw(account, newBigDecimal(450d), newBigDecimal(50d)));
        Assert.assertFalse(doWithdraw(account, newBigDecimal(500d), newBigDecimal(50d)));
        Assert.assertFalse(doWithdraw(account, newBigDecimal(0d), newBigDecimal(50d)));
        Assert.assertFalse(doWithdraw(account, newBigDecimal(-100d), newBigDecimal(50d)));
        Assert.assertFalse(doWithdraw(getFakeAccount(), newBigDecimal(10d), newBigDecimal(50d)));
        Assert.assertTrue(doWithdraw(account, newBigDecimal(50d), newBigDecimal(0d)));
        userRepository.delete(user);
    }

    @Test
    public void depositTest() {
        User user = createTestUser("Иван", "Долгорукий", "Петрович");
        Account account = createTestAccount(newBigDecimal(1000d), user);
        Assert.assertTrue(doDeposit(account, newBigDecimal(500d), newBigDecimal(1500d)));
        Assert.assertTrue(doDeposit(account, newBigDecimal(500d), newBigDecimal(2000d)));
        Assert.assertFalse(doDeposit(account, newBigDecimal(-1000d), newBigDecimal(2000d)));
        Assert.assertFalse(doDeposit(getFakeAccount(), newBigDecimal(1000d), newBigDecimal(2000d)));
        userRepository.delete(user);
    }

    @Test
    public void optimisticTest() throws InterruptedException {
        User fromUser = createTestUser("Иван", "Долгорукий", "Петрович");
        User toUser = createTestUser("Федор", "Игнатов", "Алексеевич");

        Account fromAccount = createTestAccount(newBigDecimal(10000d), fromUser);
        Account toAccount = createTestAccount(newBigDecimal(3000d), toUser);

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
                    accountManager.transfer(fromAccountId, toAccountId, newBigDecimal(100d));
                } catch (BadRequestException ignore) {
                } catch (ObjectOptimisticLockingFailureException ole) {
                    updateOptimisticCounter();
                }
            }).start();
        }

        Thread.sleep(1000L);

        fromAccount = accountRepository.findById(fromAccount.getId()).orElseThrow(() -> new RuntimeException("It's impossible"));
        toAccount = accountRepository.findById(toAccount.getId()).orElseThrow(() -> new RuntimeException("It's impossible"));

        Assert.assertEquals(fromAccount.getBalance(), newBigDecimal((10000d - (threadCount - optimisticCounter) * 100d)));
        Assert.assertEquals(toAccount.getBalance(), newBigDecimal((3000d + (threadCount - optimisticCounter) * 100d)));
        System.out.println("Optimistic counter: " + optimisticCounter);
        optimisticCounter = 0;

        userRepository.delete(fromUser);
        userRepository.delete(toUser);
    }

    @Test
    public void badRequestExceptionTest() throws InterruptedException {
        User user = createTestUser("Иван", "Долгорукий", "Петрович");
        Account account = createTestAccount(newBigDecimal(10001d), user);
        Long accountId = account.getId();
        Random random = new Random();
        int threadCount = 10;
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    try {
                        Thread.sleep(100 * random.nextInt(3));
                    } catch (InterruptedException ignore) {
                    }
                    accountManager.withdraw(accountId, newBigDecimal(5000d));
                } catch (BadRequestException | ObjectOptimisticLockingFailureException ignore) {
                }
            }).start();
        }

        Thread.sleep(1000L);
        account = accountRepository.findById(account.getId()).orElseThrow(() -> new RuntimeException("It's impossible"));
        Assert.assertEquals(newBigDecimal(1d), account.getBalance());

        userRepository.delete(user);
    }

    @Test
    public void precisionTest() {
        User user = createTestUser("Иван", "Долгорукий", "Петрович");
        Account account = createTestAccount(newBigDecimal(10001.33d), user);
        Assert.assertTrue(doDeposit(account, newBigDecimal(10.66d), newBigDecimal(10011.99d)));
        Assert.assertTrue(doDeposit(account, newBigDecimal(30.33d), newBigDecimal(10042.32d)));
        Assert.assertTrue(doWithdraw(account, newBigDecimal(79.77d), newBigDecimal(9962.55d)));
        Assert.assertTrue(doWithdraw(account, newBigDecimal(93.59d), newBigDecimal(9868.96d)));
        userRepository.delete(user);
    }

    private boolean doTransfer(Account fromAccount, Account toAccount, BigDecimal value) {
        try {
            accountManager.transfer(fromAccount.getId(), toAccount.getId(), value);
        } catch (BadRequestException bre) {
            return false;
        }
        return true;
    }

    private boolean doWithdraw(Account account, BigDecimal value, BigDecimal expected) {
        try {
            accountManager.withdraw(account.getId(), value);
        } catch (BadRequestException bre) {
            return false;
        }
        account = accountRepository.findById(account.getId()).orElseThrow(() -> new RuntimeException("It's impossible"));
        Assert.assertEquals(account.getBalance(), expected);
        return true;
    }

    private boolean doDeposit(Account account, BigDecimal value, BigDecimal expected) {
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
        fakeAccount.setBalance(newBigDecimal(10000d));
        return fakeAccount;
    }

    private synchronized void updateOptimisticCounter() {
        optimisticCounter++;
    }
}
