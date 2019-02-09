package com.domclick.service

import com.domclick.entity.Account
import com.domclick.exception.BadRequestException
import java.math.BigDecimal
import java.util.*

interface AccountService : CrudService<Account, Long> {
    /**
     * Transfer money from one account to another account
     * If account has incorrect state throws bad request exception
     * @param fromAccountId from account id
     * @param toAccountId to account id
     * @param value money
     * @throws BadRequestException
     */
    @Throws(BadRequestException::class)
    fun transfer(fromAccountId: Long, toAccountId: Long, value: BigDecimal)

    /**
     * Withdraw money from account
     * If account has incorrect state throws bad request exception
     * @param accountId target account id
     * @param value money
     */
    @Throws(BadRequestException::class)
    fun withdraw(accountId: Long, value: BigDecimal)

    /**
     * Deposit money on account
     * If account has incorrect state throws bad request exception
     * @param accountId target account id
     * @param value money
     */
    @Throws(BadRequestException::class)
    fun deposit(accountId: Long, value: BigDecimal)

    fun findAccountByIdWithLock(accountId: Long) : Optional<Account>
}
