package com.domclick.service

import com.domclick.BaseTestSupport
import com.domclick.app.AppApplication
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(AppApplication::class)])
class AccountServiceTest : BaseTestSupport() {
    /*@Autowired
    private lateinit var accountService: AccountService

    private val fakeAccount: Account
        get() {
            val fakeAccount = Account()
            fakeAccount.id = 9999999999L
            fakeAccount.balance = newBigDecimal(10000.0)
            return fakeAccount
        }

    @Test
    fun transferTest() {
        val fromUser = createTestUser("Иван", "Долгорукий", "Петрович")
        val toUser = createTestUser("Федор", "Игнатов", "Алексеевич")

        var fromAccount = createTestAccount(newBigDecimal(155.5), fromUser)
        var toAccount = createTestAccount(newBigDecimal(300.0), toUser)

        Assert.assertTrue(doTransfer(fromAccount, toAccount, newBigDecimal(100.0)))

        fromAccount = accountRepository.findById(fromAccount.id).orElseThrow { RuntimeException("It's impossible") }
        toAccount = accountRepository.findById(toAccount.id).orElseThrow { RuntimeException("It's impossible") }

        Assert.assertEquals(fromAccount.balance, newBigDecimal(55.5))
        Assert.assertEquals(toAccount.balance, newBigDecimal(400.0))

        fromAccount = updateAccountBalance(fromAccount, newBigDecimal(1000.0))
        toAccount = updateAccountBalance(toAccount, newBigDecimal(3000.0))

        Assert.assertFalse(doTransfer(fromAccount, fromAccount, newBigDecimal(100.0)))
        Assert.assertFalse(doTransfer(fromAccount, toAccount, newBigDecimal(0.0)))
        Assert.assertFalse(doTransfer(fromAccount, toAccount, newBigDecimal(-1.0)))
        Assert.assertFalse(doTransfer(fromAccount, toAccount, newBigDecimal(50000.0)))
        Assert.assertFalse(doTransfer(fromAccount, fakeAccount, newBigDecimal(50.0)))

        userRepository.delete(fromUser)
        userRepository.delete(toUser)
        accountRepository.delete(fromAccount)
        accountRepository.delete(toAccount)
    }

    @Test
    fun withdrawTest() {
        val user = createTestUser("Иван", "Долгорукий", "Петрович")
        val account = createTestAccount(newBigDecimal(1000.0), user)
        Assert.assertTrue(doWithdraw(account, newBigDecimal(500.0), newBigDecimal(500.0)))
        Assert.assertTrue(doWithdraw(account, newBigDecimal(450.0), newBigDecimal(50.0)))
        Assert.assertFalse(doWithdraw(account, newBigDecimal(500.0), newBigDecimal(50.0)))
        Assert.assertFalse(doWithdraw(account, newBigDecimal(0.0), newBigDecimal(50.0)))
        Assert.assertFalse(doWithdraw(account, newBigDecimal(-100.0), newBigDecimal(50.0)))
        Assert.assertFalse(doWithdraw(fakeAccount, newBigDecimal(10.0), newBigDecimal(50.0)))
        Assert.assertTrue(doWithdraw(account, newBigDecimal(50.0), newBigDecimal(0.0)))
        userRepository.delete(user)
    }

    @Test
    fun depositTest() {
        val user = createTestUser("Иван", "Долгорукий", "Петрович")
        val account = createTestAccount(newBigDecimal(1000.0), user)
        Assert.assertTrue(doDeposit(account, newBigDecimal(500.0), newBigDecimal(1500.0)))
        Assert.assertTrue(doDeposit(account, newBigDecimal(500.0), newBigDecimal(2000.0)))
        Assert.assertFalse(doDeposit(account, newBigDecimal(-1000.0), newBigDecimal(2000.0)))
        Assert.assertFalse(doDeposit(fakeAccount, newBigDecimal(1000.0), newBigDecimal(2000.0)))
        userRepository.delete(user)
    }

    @Test
    @Throws(InterruptedException::class)
    fun optimisticTest() {
        val fromUser = createTestUser("Иван", "Долгорукий", "Петрович")
        val toUser = createTestUser("Федор", "Игнатов", "Алексеевич")

        var fromAccount = createTestAccount(newBigDecimal(10000.0), fromUser)
        var toAccount = createTestAccount(newBigDecimal(3000.0), toUser)

        val fromAccountId = fromAccount.id
        val toAccountId = toAccount.id

        val threadCount = 30
        for (i in 0 until threadCount) {
            Thread {
                try {
                    try {
                        Thread.sleep(100L)
                    } catch (ignore: InterruptedException) {
                    }

                    accountService.transfer(fromAccountId, toAccountId, newBigDecimal(100.0))
                } catch (ignore: BadRequestException) {
                } catch (ole: ObjectOptimisticLockingFailureException) {
                    updateOptimisticCounter()
                }
            }.start()
        }

        Thread.sleep(1000L)

        fromAccount = accountRepository.findById(fromAccount.id).orElseThrow { RuntimeException("It's impossible") }
        toAccount = accountRepository.findById(toAccount.id).orElseThrow { RuntimeException("It's impossible") }

        Assert.assertEquals(fromAccount.balance, newBigDecimal(10000.0 - (threadCount - optimisticCounter) * 100.0))
        Assert.assertEquals(toAccount.balance, newBigDecimal(3000.0 + (threadCount - optimisticCounter) * 100.0))
        println("Optimistic counter: $optimisticCounter")
        optimisticCounter = 0

        userRepository.delete(fromUser)
        userRepository.delete(toUser)
        accountRepository.delete(fromAccount)
        accountRepository.delete(toAccount)
    }

    @Test
    @Throws(InterruptedException::class)
    fun badRequestExceptionTest() {
        val user = createTestUser("Иван", "Долгорукий", "Петрович")
        var account = createTestAccount(newBigDecimal(10001.0), user)
        val accountId = account.id
        val random = Random()
        val threadCount = 10
        for (i in 0 until threadCount) {
            Thread {
                try {
                    try {
                        Thread.sleep((100 * random.nextInt(3)).toLong())
                    } catch (ignore: InterruptedException) {
                    }

                    accountService.withdraw(accountId, newBigDecimal(5000.0))
                } catch (ignore: BadRequestException) {
                } catch (ignore: ObjectOptimisticLockingFailureException) {
                }
            }.start()
        }

        Thread.sleep(1000L)
        account = accountRepository.findById(account.id).orElseThrow { RuntimeException("It's impossible") }
        Assert.assertEquals(newBigDecimal(1.0), account.balance)

        userRepository.delete(user)
        accountRepository.delete(account)
    }

    @Test
    fun precisionTest() {
        val user = createTestUser("Иван", "Долгорукий", "Петрович")
        val account = createTestAccount(newBigDecimal(10001.33), user)
        Assert.assertTrue(doDeposit(account, newBigDecimal(10.66), newBigDecimal(10011.99)))
        Assert.assertTrue(doDeposit(account, newBigDecimal(30.33), newBigDecimal(10042.32)))
        Assert.assertTrue(doWithdraw(account, newBigDecimal(79.77), newBigDecimal(9962.55)))
        Assert.assertTrue(doWithdraw(account, newBigDecimal(93.59), newBigDecimal(9868.96)))
        userRepository.delete(user)
    }

    private fun doTransfer(fromAccount: Account, toAccount: Account, value: BigDecimal): Boolean {
        try {
            accountService.transfer(fromAccount.id, toAccount.id, value)
        } catch (bre: BadRequestException) {
            return false
        }

        return true
    }

    private fun doWithdraw(account: Account, value: BigDecimal, expected: BigDecimal): Boolean {
        try {
            accountService.withdraw(account.id, value)
        } catch (bre: BadRequestException) {
            return false
        }

        val reload = accountRepository.findById(account.id).orElseThrow { RuntimeException("It's impossible") }
        Assert.assertEquals(reload.balance, expected)
        return true
    }

    private fun doDeposit(account: Account, value: BigDecimal, expected: BigDecimal): Boolean {
        try {
            accountService.deposit(account.id, value)
        } catch (bre: BadRequestException) {
            return false
        }

        val reload = accountRepository.findById(account.id).orElseThrow { RuntimeException("It's impossible") }
        Assert.assertEquals(reload.balance, expected)
        return true
    }

    @Synchronized
    private fun updateOptimisticCounter() {
        optimisticCounter++
    }

    companion object {

        @Volatile
        private var optimisticCounter = 0
    }*/
}
