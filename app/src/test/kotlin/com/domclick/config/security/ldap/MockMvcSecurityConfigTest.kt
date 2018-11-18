package com.domclick.config.security.ldap

import com.domclick.app.AppApplication
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.util.Base64Utils

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(AppApplication::class)])
@AutoConfigureMockMvc
class MockMvcSecurityConfigTest {
    @Autowired
    private var mockMvc: MockMvc? = null

    private val accountsListPath = "/api/account/list"

    @Test
    fun `base case`() {
        mockMvc!!.perform(get(accountsListPath).header(HttpHeaders.AUTHORIZATION,
                "Basic " + Base64Utils.encodeToString("ben:benspassword".toByteArray())))
                .andExpect(authenticated().withUsername("ben"))
    }

    @Test
    fun `bad credential`() {
        mockMvc!!.perform(get(accountsListPath)).andExpect(unauthenticated())
    }
}