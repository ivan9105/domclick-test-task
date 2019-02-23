package com.domclick.repository

import com.domclick.repository.common.AbstractRepositoryTest
import com.domclick.repository.utils.getValidAccount
import com.domclick.repository.utils.getValidUser
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED
import org.springframework.transaction.annotation.Transactional

@Transactional(propagation = NOT_SUPPORTED)
class UserRepositoryTest : AbstractRepositoryTest() {
    @Test
    fun crudTest() {
        var user = userRepository.save(getValidUser())

        user = userRepository.findById(user.id!!).get()
        user.middleName = "Иванович"
        user = userRepository.save(user)

        Assert.assertEquals(user.middleName, "Иванович")
        userRepository.delete(user)

        assertTrue(!userRepository.findById(user.id!!).isPresent)
    }

    @Test
    fun deleteCascadeAccountsTest() {
        var user = userRepository.save(getValidUser())
        user = userRepository.findById(user.id!!).get()
        val account = accountRepository.save(getValidAccount(user))
        userRepository.delete(user)

        assertTrue(!userRepository.findById(user.id!!).isPresent)
        assertTrue(!accountRepository.findById(account.id!!).isPresent)
    }

    @Test
    fun versionTest() {
        var user = userRepository.save(getValidUser())
        assertEquals(0L, user.version)
        user = userRepository.save(userRepository.findById(user.id!!).get().apply {
            middleName = "Игоревич"
        })
        assertEquals(1L, user.version)
        user = userRepository.save(userRepository.findById(user.id!!).get().apply {
            middleName = "Петрович"
        })
        user = userRepository.findById(user.id!!).get()
        assertEquals(2L, user.version)
        userRepository.delete(user)
    }
}