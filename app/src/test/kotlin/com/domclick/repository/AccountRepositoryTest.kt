package com.domclick.repository

import com.domclick.BaseTestSupport
import com.domclick.app.AppApplication
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(AppApplication::class)])
class AccountRepositoryTest : BaseTestSupport() {
    /*private lateinit var user: User

    //Todo refactoring

    @Before
    fun init() {
        user = userRepository.findById(0L).orElseThrow { IllegalStateException("Datasource do not initialize") }
    }

    @Test
    fun crudTest() {
        var account = createTestAccount(user)

        account = accountRepository.findById(account.id).orElseThrow { RuntimeException("It's impossible") }
        account.balance = BigDecimal(50000.0)
        account = accountRepository.save(account)
        account = accountRepository.findById(account.id).orElseThrow { RuntimeException("It's impossible") }
        Assert.assertEquals(account.balance, newBigDecimal(50000.0))

        accountRepository.delete(account)
        val accountOptional = accountRepository.findById(account.id)
        Assert.assertTrue(!accountOptional.isPresent())
    }*/
}
