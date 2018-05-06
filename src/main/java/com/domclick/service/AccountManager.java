package com.domclick.service;

import com.domclick.exception.BadRequestException;

public interface AccountManager {
    /**
     * Transfer money from one account to another account
     * If account has incorrect state throws bad request exception
     * @param fromAccountId from account id
     * @param toAccountId to account id
     * @param value money
     * @throws BadRequestException
     */
    void transfer(Long fromAccountId, Long toAccountId, Double value) throws BadRequestException;

    /**
     * Withdraw money from account
     * If account has incorrect state throws bad request exception
     * @param accountId target account id
     * @param value money
     */
    void withdraw(Long accountId, Double value) throws BadRequestException;

    /**
     * Put money on account
     * If account has incorrect state throws bad request exception
     * @param accountId target account id
     * @param value money
     */
    void put(Long accountId, Double value) throws BadRequestException;
}
