package com.domclick.service

import com.domclick.exception.BadRequestException
import com.domclick.exception.RollbackException
import com.domclick.model.Account
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.persistence.EntityManager
import javax.persistence.LockModeType
import javax.persistence.PersistenceContext
import java.math.BigDecimal

@Transactional(rollbackFor = [(RollbackException::class)])
@Service
class AccountManagerImpl : AccountManager {
    @PersistenceContext
    private val em: EntityManager? = null

    @Throws(BadRequestException::class)
    override fun transfer(fromAccountId: Long?, toAccountId: Long?, value: BigDecimal) {
        checkValue(value)

        if (fromAccountId == toAccountId) {
            throw BadRequestException("From account can not be equals to account")
        }

        val fromAccount = reloadAccount(fromAccountId)
        val toAccount = reloadAccount(toAccountId)

        checkWithDrawPossibility(value, fromAccount)

        fromAccount.balance = fromAccount.balance!!.subtract(value)
        toAccount.balance = toAccount.balance!!.add(value)
    }

    @Throws(BadRequestException::class)
    override fun withdraw(accountId: Long?, value: BigDecimal) {
        checkValue(value)

        val account = reloadAccount(accountId)

        checkWithDrawPossibility(value, account)

        account.balance = account.balance!!.subtract(value)
    }

    @Throws(BadRequestException::class)
    override fun deposit(accountId: Long?, value: BigDecimal) {
        checkValue(value)

        val account = reloadAccount(accountId)
        account.balance =  account.balance!!.add(value)
    }

    @Throws(BadRequestException::class)
    private fun reloadAccount(accountId: Long?): Account {
        return em!!.find(Account::class.java, accountId, LockModeType.OPTIMISTIC)
                ?: throw BadRequestException(String.format("Can not load account with id '%s'", accountId))
    }

    @Throws(BadRequestException::class)
    private fun checkValue(value: BigDecimal) {
        if (value.toDouble() <= 0) {
            throw BadRequestException("Value must be over than 0")
        }
    }

    @Throws(BadRequestException::class)
    private fun checkWithDrawPossibility(value: BigDecimal, account: Account) {
        if (account.balance!!.subtract(value).toDouble() < 0) {
            throw BadRequestException(String.format("On account with id '%s' not enough money", value))
        }
    }
}