package com.domclick.rest

import com.domclick.rest.common.AbstractControllerTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.OK
import kotlin.test.assertEquals

class UserApiControllerTest : AbstractControllerTest() {

    @Value("classpath:json/user/user.json")
    lateinit var userJson: Resource

    @Value("classpath:json/user/user_not_found.json")
    lateinit var userNotFoundJson: Resource

    private val NOT_FOUND_USER_ID = 9999L

    @Test
    fun getAllUsersTest() {
        val response = httpGetRequest("/api/user/list")
        assertEquals(OK, response.statusCode)
    }

    @Test
    fun getUserWithId_0_Happy_Path_Test() {
        val response = httpGetRequest("/api/user/get/0")
        assertEquals(OK, response.statusCode)
        assertJson(userJson.getContent(), response.body!!)
    }

    @Test
    fun getUserWithId_9999_User_Not_Found_Test() {
        val response = httpGetRequest("/api/user/get/$NOT_FOUND_USER_ID")
        assertEquals(BAD_REQUEST, response.statusCode)
        assertJsonWithTimestamp(userNotFoundJson.getContent(), response.body!!)
    }
}