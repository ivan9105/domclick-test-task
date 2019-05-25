package com.domclick.utils

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
    lateinit var dtoBuilder: DtoBuilder

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
        assertEquals(1, res.users.size)
        assertEquals(user.id, res.users[0].id)
        assertEquals(user.firstName, res.users[0].firstName)
        assertEquals(user.lastName, res.users[0].lastName)
        assertEquals(user.middleName, res.users[0].middleName)
        assertEquals(1, res.users[0].links.size)
        assertEquals("self", res.users[0].links[0].rel)
        assertEquals("GET", res.users[0].links[0].type)
        assertEquals("http://localhost:9555/api/user/get/0", res.users[0].links[0].href)
    }
}