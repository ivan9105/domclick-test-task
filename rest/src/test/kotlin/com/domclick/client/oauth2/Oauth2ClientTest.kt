package com.domclick.client.oauth2

import com.domclick.app.RestApplication
import com.domclick.config.properties.oauth2.Oauth2Properties
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.client.HttpClientErrorException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [RestApplication::class], webEnvironment = RANDOM_PORT)
@ActiveProfiles("oauth2-test")
class Oauth2ClientTest {

    @LocalServerPort
    var port: Int? = null

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
    fun getAccessToken_Happy_Path_Test() {
        val accessTokenResponse = client.getAccessToken("admin", "admin1234", "oauth2-demo", "oauth2-demo-password")
        assertEquals("read write", accessTokenResponse.scope)
        assertEquals("bearer", accessTokenResponse.tokenType)
        assertNotNull(accessTokenResponse.accessToken)
        assertNotNull(accessTokenResponse.refreshToken)
        assertNotNull(accessTokenResponse.expiresIn)
    }

    @Test
    fun getAccessToken_User_Details_Not_Found_Test() {
        expectedException.expect(HttpClientErrorException.BadRequest::class.java)
        client.getAccessToken("admin1", "admin1234", "oauth2-demo", "oauth2-demo-password")
    }

    @Test
    fun getAccessToken_Client_Details_Not_Found_Test() {
        expectedException.expect(HttpClientErrorException.Unauthorized::class.java)
        client.getAccessToken("admin", "admin1234", "oauth2-demo1", "oauth2-demo-password")
    }

    @Test
    fun getRefreshToken_Happy_Path_Test() {
        val accessTokenResponse = client.getAccessToken("admin", "admin1234", "oauth2-demo", "oauth2-demo-password")
        val refreshTokenResponse = client.getRefreshToken(accessTokenResponse.refreshToken!!, "oauth2-demo", "oauth2-demo-password")
        assertEquals("read write", refreshTokenResponse.scope)
        assertEquals("bearer", refreshTokenResponse.tokenType)
        assertNotNull(refreshTokenResponse.accessToken)
        assertNotNull(refreshTokenResponse.refreshToken)
        assertNotNull(refreshTokenResponse.expiresIn)
    }

    @Test
    fun getRefreshToken_Access_Token_Not_Found_Test() {
        expectedException.expect(HttpClientErrorException.BadRequest::class.java)
        client.getRefreshToken("!", "oauth2-demo", "oauth2-demo-password")
    }

    @Test
    fun getRefreshToken_Client_Details_Not_Found_Test() {
        expectedException.expect(HttpClientErrorException.Unauthorized::class.java)
        val accessTokenResponse = client.getAccessToken("admin", "admin1234", "oauth2-demo", "oauth2-demo-password")
        client.getRefreshToken(accessTokenResponse.refreshToken!!, "oauth2-demo!", "oauth2-demo-password")
    }
}