package com.domclick.repository

import com.domclick.BaseTestSupport
import com.domclick.app.AppApplication
import org.junit.Ignore
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@Ignore("fix later")
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(AppApplication::class)])
class UserRepositoryTest : BaseTestSupport() {
/*
    @Test
    fun crudTest() {
        var user = createTestUser()

        user = userRepository.findById(user.id).orElseThrow { RuntimeException("It's impossible") }
        user.middleName = "Иванович"
        user = userRepository.save(user)

        Assert.assertEquals(user.middleName, "Иванович")
        userRepository.delete(user)

        val userOptional = userRepository.findById(user.id)
        Assert.assertTrue(!userOptional.isPresent)
    }


    @Test
    fun cascadeDropAccountsTest() {
        lateinit var user: User
        lateinit var account1: Account
        lateinit var account2: Account
        lateinit var account3: Account
        getSessionFactory().openSession().use { session ->
            val tx = session.beginTransaction()
            user = createTestUser(session)
            account1 = createTestAccount(user, session)
            account2 = createTestAccount(user, session)
            account3 = createTestAccount(user, session)
            tx.commit()
        }


        getSessionFactory().openSession().use { session ->
            val tx = session.beginTransaction()
            session.remove(reload(user, session))
            checkNotExists(session, user, account1, account2, account3)
            tx.commit()
        }
    }

    @Test
    fun versionTest() {
        var user = createTestUser()
        Assert.assertEquals(user.version, 0L)
        user = userRepository.findById(user.id).orElseThrow { RuntimeException("It's impossible") }
        user.middleName = "Иванович"
        user = userRepository.save(user)

        Assert.assertEquals(user.version, 1L)

        user = userRepository.findById(user.id).orElseThrow { RuntimeException("It's impossible") }
        user.middleName = "Петрович"
        user = userRepository.save(user)

        Assert.assertEquals(user.version, 2L)

        userRepository.delete(user)
    }*/
}
