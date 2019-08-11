package com.domclick.service

import com.domclick.entity.AccountEntity
import com.domclick.exception.BadRequestException
import com.domclick.exception.RollbackException
import com.domclick.repository.AccountRepository
import com.domclick.repository.UserRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.String.format
import java.math.BigDecimal

@Transactional(rollbackFor = [(RollbackException::class)])
@Service
class AccountServiceImpl(
        private val accountRepository: AccountRepository,
        private val userRepository: UserRepository
) : CrudServiceImpl<AccountEntity, Long>(), AccountService {

    override fun findAccountByIdWithLock(accountId: Long) = accountRepository.findById(accountId)

    override fun upsert(entity: AccountEntity) {
        val reload = if (entity.isNew()) AccountEntity() else accountRepository.findById(entity.id!!)
                .orElseThrow { BadRequestException(String.format("Account with id '%s' not found", entity.id!!)) }
        reload.balance = entity.balance
        reload.user = userRepository.findById(entity.userId.toLong()).orElseThrow {
            BadRequestException(format("User with id '%s' not found", entity.userId))
        }

        reload.updateUserId()

        save(reload)
    }

    override fun getRepository(): CrudRepository<AccountEntity, Long> = accountRepository

    @Throws(BadRequestException::class)
    override fun transfer(fromAccountId: Long, toAccountId: Long, value: BigDecimal) {
        validateValue(value)

        if (fromAccountId == toAccountId) {
            throw BadRequestException("From account can not be equals to account")
        }

        val fromAccount = reloadAccount(fromAccountId)
        val toAccount = reloadAccount(toAccountId)

        validateWithdrawPossibility(value, fromAccount)

        fromAccount.balance = fromAccount.balance!!.subtract(value)
        toAccount.balance = toAccount.balance!!.add(value)
    }

    @Throws(BadRequestException::class)
    override fun withdraw(accountId: Long, value: BigDecimal) {
        validateValue(value)

        val account = reloadAccount(accountId)

        validateWithdrawPossibility(value, account)

        account.balance = account.balance!!.subtract(value)
    }

    @Throws(BadRequestException::class)
    override fun deposit(accountId: Long, value: BigDecimal) {
        validateValue(value)

        val account = reloadAccount(accountId)
        account.balance = account.balance!!.add(value)
    }

    @Throws(BadRequestException::class)
    private fun reloadAccount(accountId: Long) = accountRepository.findById(accountId).orElseThrow {
        BadRequestException(format("Account with id '%s' not found", accountId))
    }

    @Throws(BadRequestException::class)
    private fun validateValue(value: BigDecimal) {
        if (value.toDouble() <= 0) {
            throw BadRequestException("Value must be over than 0")
        }
    }

    @Throws(BadRequestException::class)
    private fun validateWithdrawPossibility(value: BigDecimal, account: AccountEntity) {
        if (account.balance!!.subtract(value).toDouble() < 0) {
            throw BadRequestException(String.format("On account with id '%s' not enough money", account.id))
        }
    }
}