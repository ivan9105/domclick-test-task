package com.domclick.service;

import com.domclick.exception.BadRequestException;
import com.domclick.exception.RollbackException;
import com.domclick.model.Account;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;

@Transactional(rollbackFor = RollbackException.class)
@Service
public class AccountManagerImpl implements AccountManager {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal value) throws BadRequestException {
        checkValue(value);

        if (fromAccountId.equals(toAccountId)) {
            throw new BadRequestException("From account can not be equals to account");
        }

        Account fromAccount = reloadAccount(fromAccountId);
        Account toAccount = reloadAccount(toAccountId);

        checkWithDrawPossibility(value, fromAccount);

        fromAccount.setBalance(fromAccount.getBalance().subtract(value));
        toAccount.setBalance(toAccount.getBalance().add(value));
    }

    @Override
    public void withdraw(Long accountId, BigDecimal value) throws BadRequestException {
        checkValue(value);

        Account account = reloadAccount(accountId);

        checkWithDrawPossibility(value, account);

        account.setBalance(account.getBalance().subtract(value));
    }

    @Override
    public void deposit(Long accountId, BigDecimal value) throws BadRequestException {
        checkValue(value);

        Account account = reloadAccount(accountId);
        account.setBalance(account.getBalance().add(value));
    }

    private Account reloadAccount(Long accountId) throws BadRequestException {
        Account fromAccount = em.find(Account.class, accountId, LockModeType.OPTIMISTIC);
        if (fromAccount == null) {
            throw new BadRequestException(String.format("Can not load account with id '%s'", accountId));
        }
        return fromAccount;
    }

    private void checkValue(BigDecimal value) throws BadRequestException {
        if (value.doubleValue() <= 0) {
            throw new BadRequestException("Value must be over than 0");
        }
    }

    private void checkWithDrawPossibility(BigDecimal value, Account account) throws BadRequestException {
        if (account.getBalance().subtract(value).doubleValue() < 0) {
            throw new BadRequestException(String.format("On account with id '%s' not enough money", value));
        }
    }
}
