package com.domclick.utils

import com.domclick.entity.Account
import com.domclick.entity.User
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DtoBuilderTest {
    @InjectMocks
    private lateinit var dtoBuilder: DtoBuilder

    @Before
    fun `init`() {
        dtoBuilder.serverUrl = "http://localhost:9555/"
    }

    @Test
    fun buildUserResponseTest() {
        val user = User().apply {
            id = 0L
            firstName = "Ivan"
            lastName = "Ivanov"
            middleName = "Ivanovich"
        }

        val res = dtoBuilder.buildUserResponse(listOf(user))
        assertEquals(res.users.size, 1)
        assertEquals(res.users[0].id, user.id)
        assertEquals(res.users[0].firstName, user.firstName)
        assertEquals(res.users[0].lastName, user.lastName)
        assertEquals(res.users[0].middleName, user.middleName)
        assertEquals(res.users[0].links.size, 1)
        assertEquals(res.users[0].links[0].rel, "self")
        assertEquals(res.users[0].links[0].type, "GET")
        assertEquals(res.users[0].links[0].href, "http://localhost:9555/api/user/get/0")
    }

    @Test
    fun buildUserAccountsDtoTest() {
        val user = User().apply {
            id = 0L
            accounts = setOf(Account().apply {
                id = 0L
            })
        }
    }
}