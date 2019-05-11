package com.domclick.rest.security.oauth2

import com.domclick.client.oauth2.Oauth2Client
import com.domclick.config.properties.oauth2.Oauth2Properties
import com.domclick.dto.response.Oauth2AccessTokenResponse
import com.domclick.rest.common.AbstractControllerTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("oauth2-test")
class CompanyApiOauth2SecurityTest : AbstractControllerTest() {

    @Value("classpath:json/company/companies.json")
    lateinit var companiesJson: Resource

    @Value("classpath:json/company/invalid_token.json")
    lateinit var invalidTokenJson: Resource

    @MockBean
    lateinit var oauth2Properties: Oauth2Properties

    @Autowired
    lateinit var client: Oauth2Client

    @Rule
    @JvmField
    final val expectedException = ExpectedException.none()!!

    @Before
    fun init() {
        `when`(oauth2Properties.host).thenReturn("http://localhost")
        `when`(oauth2Properties.port).thenReturn(port.toString())
    }

    @Test
    fun getCompanyTest_Happy_Path_Test() {
        val accessTokenResponse = client.getAccessToken("admin", "admin1234", "oauth2-demo", "oauth2-demo-password")
        val response = httpGetRequest("/api/company/list", headers(accessTokenResponse))
        assertEquals(OK, response.statusCode)
        assertJson(companiesJson.getContent(), response.body!!)
    }

    @Test
    fun getCompanyTest_Token_Not_Found_Test() {
        val response = httpGetRequest("/api/company/list", headers(Oauth2AccessTokenResponse().apply { accessToken = "!" }))
        assertEquals(UNAUTHORIZED, response.statusCode)
        assertJson(invalidTokenJson.getContent(), response.body!!)
    }

    private fun headers(accessTokenResponse: Oauth2AccessTokenResponse) =
            HttpHeaders().apply { add("Authorization", "Bearer ${accessTokenResponse.accessToken}") }

    //Todo JdbcClientDetailsServiceBuilder(datasource).service for expired token case
}