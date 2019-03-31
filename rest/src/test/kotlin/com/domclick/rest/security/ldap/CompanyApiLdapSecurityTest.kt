package com.domclick.rest.security.ldap

import com.domclick.app.RestApplication
import com.domclick.config.security.ldap.config.LdapTestConfig
import com.domclick.rest.common.AbstractControllerTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest(classes = [RestApplication::class, LdapTestConfig::class], webEnvironment = RANDOM_PORT)
@ActiveProfiles("ldap-test")
class CompanyApiLdapSecurityTest : AbstractControllerTest() {

    @Value("classpath:json/company/companies.json")
    lateinit var companiesJson: Resource

    @Rule
    @JvmField
    final val expectedException = ExpectedException.none()!!

    @Test
    fun getCompanyTest_Happy_Path_Test() {
        val response = httpGetRequest("/api/company/list", headers("iipetrov", "RnIpIA-ybp82"))
        assertEquals(OK, response.statusCode)
        assertJson(companiesJson.getContent(), response.body!!)
    }

    @Test
    fun getCompanyTest_Bad_Credentials() {
        val response = httpGetRequest("/api/company/list",  headers("!", "RnIpIA-ybp82"))
        assertEquals(UNAUTHORIZED, response.statusCode)
    }

    private fun headers(username: String, password: String) =
            HttpHeaders().apply { add("Authorization", "Basic ${String(Base64.getEncoder().encode("$username:$password".toByteArray()))}") }
}