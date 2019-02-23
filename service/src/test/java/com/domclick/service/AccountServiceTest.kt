package com.domclick.service

import com.domclick.entity.Account
import com.domclick.entity.User
import com.domclick.exception.BadRequestException
import com.domclick.repository.AccountRepository
import com.domclick.repository.UserRepository
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal
import java.util.Optional.empty
import java.util.Optional.of

@RunWith(MockitoJUnitRunner::class)
class AccountServiceTest {

    @InjectMocks
    lateinit var accountService: AccountServiceImpl
    @Mock
    lateinit var userRepository: UserRepository
    @Mock
    lateinit var accountRepository: AccountRepository

    @Rule
    @JvmField
    val expectedException = ExpectedException.none()!!

    @Test
    fun findAccountByIdWithLockTest() {
        accountService.findAccountByIdWithLock(3L)
        verify(accountRepository, times(1)).findById(eq(3L))
    }

    @Test
    fun upsertAccountAlreadyExistsButNotFoundTest() {
        expectRuntimeException("Can not found Account with id '1001'")

        val account = getValidAccount(getValidUser()).apply { id = 1001L }
        whenever(accountRepository.findById(eq(account.id!!))).thenReturn(empty())

        accountService.upsert(account)
    }

    @Test
    fun upsertAccountAlreadyExistsButUserNotFoundTest() {
        expectRuntimeException("Can not found User by id '1002'")

        val user = getValidUser().apply { id = 1002 }
        val account = getValidAccount(user).apply {
            id = 3001L
            userId = user.id.toString()
        }
        whenever(accountRepository.findById(account.id!!)).thenReturn(of(account))
        whenever(userRepository.findById(eq(user.id!!))).thenReturn(empty())

        accountService.upsert(account)
    }

    @Test
    fun upsertHappyPathTest() {
        val user = getValidUser().apply { id = 2002 }
        val account = getValidAccount(user)

        val updatedAccount = getValidAccount(user).apply {
            balance = BigDecimal("777.00")
            id = 4001L
            userId = user.id.toString()
        }

        whenever(accountRepository.findById(updatedAccount.id!!)).thenReturn(of(account))
        whenever(userRepository.findById(eq(user.id!!))).thenReturn(of(user))

        accountService.upsert(updatedAccount)

        assertEquals(updatedAccount.balance, account.balance)
        assertEquals(updatedAccount.userId, account.userId)
    }

    @Test
    fun transferNegativeValueTest() {
        expectBadRequestException("Value must be over than 0")
        accountService.transfer(1L, 2L, BigDecimal(-1))
    }

    @Test
    fun transferFromSameAccountTest() {
        expectBadRequestException("From account can not be equals to account")

        val account = getValidAccount(getValidUser()).apply { id = 6001L }
        accountService.transfer(account.id!!, account.id!!, BigDecimal("1"))
    }

    @Test
    fun transferFromAccountNotFoundTest() {
        expectRuntimeException("Can not found account by id '-1'")

        accountService.transfer(-1L, -2L, BigDecimal("1"))
    }

    @Test
    fun transferToAccountNotFoundTest() {
        expectRuntimeException("Can not found account by id '-2'")

        val account = getValidAccount(getValidUser()).apply { id = 5001L }
        whenever(accountRepository.findById(account.id!!)).thenReturn(of(account))

        accountService.transfer(account.id!!, -2L, BigDecimal("1"))
    }

    @Test
    fun transferWithdrawImpossibleTest() {
        expectBadRequestException("On account with id '7001' not enough money")

        val fromAccount = getValidAccount(getValidUser()).apply {
            id = 7001L
            balance = BigDecimal("0.00")
        }

        val toAccount = getValidAccount(getValidUser()).apply {
            id = 8001L
            balance = BigDecimal("0.00")
        }

        whenever(accountRepository.findById(fromAccount.id!!)).thenReturn(of(fromAccount))
        whenever(accountRepository.findById(toAccount.id!!)).thenReturn(of(toAccount))

        accountService.transfer(fromAccount.id!!, toAccount.id!!, BigDecimal("1"))
    }

    @Test
    fun transferHappyPathTest() {
        val fromAccount = getValidAccount(getValidUser()).apply {
            id = 7002L
            balance = BigDecimal("100.00")
        }

        val toAccount = getValidAccount(getValidUser()).apply {
            id = 8002L
            balance = BigDecimal("50.00")
        }

        whenever(accountRepository.findById(fromAccount.id!!)).thenReturn(of(fromAccount))
        whenever(accountRepository.findById(toAccount.id!!)).thenReturn(of(toAccount))

        accountService.transfer(fromAccount.id!!, toAccount.id!!, BigDecimal("50.00"))

        assertEquals(BigDecimal("100.00"), toAccount.balance)
        assertEquals(BigDecimal("50.00"), fromAccount.balance)
    }

    @Test
    fun withdrawNegativeValueTest() {
        expectBadRequestException("Value must be over than 0")
        accountService.withdraw(1L, BigDecimal(-1))
    }

    @Test
    fun withdrawAccountNotFoundTest() {
        expectRuntimeException("Can not found account by id '-3'")
        accountService.withdraw(-3L, BigDecimal("1"))
    }

    @Test
    fun withdrawImpossibleTest() {
        expectBadRequestException("On account with id '82' not enough money")

        val account = getValidAccount(getValidUser()).apply {
            id = 82L
            balance = BigDecimal("50.00")
        }

        whenever(accountRepository.findById(account.id!!)).thenReturn(of(account))

        accountService.withdraw(account.id!!, BigDecimal("100.00"))
    }

    @Test
    fun withdrawHappyPathTest() {
        val account = getValidAccount(getValidUser()).apply {
            id = 83L
            balance = BigDecimal("50.00")
        }

        whenever(accountRepository.findById(account.id!!)).thenReturn(of(account))

        accountService.withdraw(account.id!!, BigDecimal("30.00"))

        assertEquals(BigDecimal("20.00"), account.balance)
    }

    @Test
    fun depositNegativeValueTest() {
        expectBadRequestException("Value must be over than 0")
        accountService.deposit(1L, BigDecimal(-1))
    }

    @Test
    fun depositAccountNotFoundTest() {
        expectRuntimeException("Can not found account by id '-4'")
        accountService.deposit(-4L, BigDecimal("1"))
    }

    @Test
    fun depositHappyPathTest() {
        val account = getValidAccount(getValidUser()).apply {
            id = 83L
            balance = BigDecimal("50.00")
        }

        whenever(accountRepository.findById(account.id!!)).thenReturn(of(account))

        accountService.deposit(account.id!!, BigDecimal("30.00"))

        assertEquals(BigDecimal("80.00"), account.balance)
    }

    private fun getValidAccount(user: User) = Account(BigDecimal("100.00"), user)

    private fun getValidUser() = User("Иван", "Иванов", "Иванович")

    private fun expectRuntimeException(msg: String) {
        expectedException.expect(RuntimeException::class.java)
        expectedException.expectMessage(msg)
    }

    private fun expectBadRequestException(msg: String) {
        expectedException.expect(BadRequestException::class.java)
        expectedException.expectMessage(msg)
    }
}
