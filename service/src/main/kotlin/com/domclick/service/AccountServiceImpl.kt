package com.domclick.service

import com.domclick.entity.Account
import com.domclick.exception.BadRequestException
import com.domclick.exception.RollbackException
import com.domclick.repository.AccountRepository
import com.domclick.repository.UserRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Transactional(rollbackFor = [(RollbackException::class)])
@Service
class AccountServiceImpl(
        private val accountRepository: AccountRepository,
        private val userRepository: UserRepository
) : CrudServiceImpl<Account, Long>(), AccountService {

    override fun findAccountByIdWithLock(accountId: Long) = accountRepository.findById(accountId)

    override fun upsert(entity: Account) {
        val reload = if (entity.isNew()) Account() else findById(entity.id!!).orElseThrow { RuntimeException(String.format("Can not found Account with id '%s'", entity.id!!)) }
        reload.balance = entity.balance
        reload.user = userRepository.findById(entity.userId.toLong()).orElseThrow {
            RuntimeException(
                    java.lang.String.format("Can not found account by id '%s'", entity.userId))
        }

        reload.updateUserId()

        save(reload)
    }

    override fun getRepository(): CrudRepository<Account, Long> = accountRepository

    @Throws(BadRequestException::class)
    override fun transfer(fromAccountId: Long, toAccountId: Long, value: BigDecimal) {
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
    override fun withdraw(accountId: Long, value: BigDecimal) {
        checkValue(value)

        val account = reloadAccount(accountId)

        checkWithDrawPossibility(value, account)

        account.balance = account.balance!!.subtract(value)
    }

    @Throws(BadRequestException::class)
    override fun deposit(accountId: Long, value: BigDecimal) {
        checkValue(value)

        val account = reloadAccount(accountId)
        account.balance = account.balance!!.add(value)
    }

    @Throws(BadRequestException::class)
    private fun reloadAccount(accountId: Long) = accountRepository.findById(accountId).orElseThrow {
        RuntimeException(
                java.lang.String.format("Can not found account by id '%s'", accountId))
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