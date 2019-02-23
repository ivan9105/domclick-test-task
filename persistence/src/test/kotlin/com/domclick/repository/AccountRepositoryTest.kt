package com.domclick.repository

import com.domclick.repository.common.AbstractRepositoryTest
import com.domclick.repository.utils.getValidAccount
import com.domclick.repository.utils.getValidUser
import org.junit.Assert.*
import org.junit.Test
import org.springframework.orm.ObjectOptimisticLockingFailureException
import java.math.BigDecimal

class AccountRepositoryTest : AbstractRepositoryTest() {

    @Test
    fun crudTest() {
        val user = userRepository.save(getValidUser())
        var account = accountRepository.save(getValidAccount(user))
        account = accountRepository.findById(account.id!!).orElse(null)
        assertNotNull(account)
        account.balance = BigDecimal("5000.00")
        account = accountRepository.save(account)
        account = accountRepository.findById(account.id!!).orElse(null)
        assertEquals(BigDecimal("5000.00"), account.balance)
        val accountId = account.id!!
        accountRepository.delete(account)
        val optional = accountRepository.findById(accountId)
        assertNull(optional.orElse(null))
        userRepository.delete(user)
    }

    @Test
    fun optimisticLockTest() {
        val user = userRepository.save(getValidUser())
        var account = accountRepository.save(getValidAccount(user))

        account = accountRepository.findById(account.id!!).get()
        account.balance = BigDecimal(10000L)
        accountRepository.save(account)

        var hasException = false
        try {
            account.balance = BigDecimal(5000L)
            accountRepository.save(account)
        } catch (optimisticException: ObjectOptimisticLockingFailureException) {
            hasException = true
        }

        assertTrue(hasException)

        account = accountRepository.findById(account.id!!).get()
        accountRepository.delete(account)
        userRepository.delete(user)
    }
}