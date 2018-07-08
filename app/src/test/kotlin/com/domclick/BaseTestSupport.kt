package com.domclick

import com.domclick.model.Account
import com.domclick.model.BaseEntity
import com.domclick.model.User
import com.domclick.repository.AccountRepository
import com.domclick.repository.UserRepository
import com.domclick.utils.HibernateSessionUtils
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.junit.Assert
import org.springframework.beans.factory.annotation.Autowired

import java.math.BigDecimal


open class BaseTestSupport {
    @Autowired
    protected lateinit var accountRepository: AccountRepository

    @Autowired
    protected lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var hibernateSessionUtils: HibernateSessionUtils

    private lateinit var sessionFactory: SessionFactory

    protected fun createTestAccount(user: User): Account {
        return createTestAccount(newBigDecimal(0.0), user)
    }

    protected fun createTestAccount(balance: BigDecimal, user: User): Account {
        var account = getTestAccount(balance, user)
        account = accountRepository.save(account)
        return account
    }

    @JvmOverloads
    protected fun createTestUser(firstName: String = "Федор", lastName: String = "Петров", middleName: String = "Петрович"): User {
        var user = getTestUser(firstName, lastName, middleName)
        user = userRepository.save(user)
        return user
    }

    protected fun createTestAccount(user: User, session: Session): Account {
        val account = getTestAccount(newBigDecimal(0.0), user)
        return session.find(Account::class.java, session.save(account))
    }

    protected fun createTestUser(session: Session): User {
        val user = getTestUser("Федор", "Петров", "Петрович")
        return session.find(User::class.java, session.save(user))
    }

    protected fun getSessionFactory(): SessionFactory {
        return hibernateSessionUtils.getSessionFactory()
    }

    protected fun reload(entity: BaseEntity, session: Session): BaseEntity {
        return session.find(entity::class.java, entity.id)
    }

    protected fun checkNotExists(session: Session, vararg entities: BaseEntity) {
        for (entity in entities) {
            Assert.assertNull("Object must be removed", session.find(entity::class.java, entity.id))
        }
    }

    private fun getTestUser(firstName: String, lastName: String, middleName: String): User {
        val user = User()
        user.firstName = firstName
        user.lastName = lastName
        user.middleName = middleName
        return user
    }

    private fun getTestAccount(balance: BigDecimal, user: User): Account {
        val account = Account()
        account.user = user
        account.balance = balance
        return account
    }

    protected fun updateAccountBalance(account: Account, value: BigDecimal): Account {
        account.balance = value
        return accountRepository.save(account)
    }

    protected fun newBigDecimal(value: Double): BigDecimal {
        return BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_EVEN)
    }
}
