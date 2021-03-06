package com.domclick.config.security.ldap.client

import com.domclick.config.security.ldap.config.LdapTestConfig
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ldap.AuthenticationException
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader

@RunWith(SpringJUnit4ClassRunner::class)
@ActiveProfiles("ldap-test")
@ContextConfiguration(classes = [LdapTestConfig::class], loader = AnnotationConfigContextLoader::class)
class LdapClientTest {

    @Autowired
    lateinit var ldapClient: LdapClient

    @Rule
    @JvmField
    val expectedException = ExpectedException.none()!!

    companion object {
        private const val USER_1 = "iipetrov"
        private const val USER_2 = "eadegtiarev"
        private const val USER_3 = "olchernov"
        private const val USER_4 = "aakurochkov"
        private const val ENCODED_PASSWORD_2 = "ulO2OprYRr2_"
        private const val ENCODED_PASSWORD_3 = "JRotiVOt1%y2"
        private const val ENCODED_PASSWORD_4 = "TipUuTIb1a5%"
    }

    @Test
    fun authenticateHappyPath() {
        ldapClient.authenticate(USER_2, ENCODED_PASSWORD_2)
    }

    @Test
    fun authenticateFail() {
        expectedException.expect(AuthenticationException::class.java)
        ldapClient.authenticate(USER_2, "123")
    }

    @Test
    fun searchHappyPathTest() {
        val searchResult = ldapClient.search("*i*")
        assertThat(searchResult, containsInAnyOrder(USER_1, USER_2))
    }

    @Test
    fun createHappyPathTest() {
        ldapClient.create(USER_3, ENCODED_PASSWORD_3)
        ldapClient.authenticate(USER_3, ENCODED_PASSWORD_3)
    }

    @Test
    fun modifyHappyPathTest() {
        ldapClient.create(USER_4, ENCODED_PASSWORD_3)
        ldapClient.authenticate(USER_4, ENCODED_PASSWORD_3)

        ldapClient.modify(USER_4, ENCODED_PASSWORD_4)
        ldapClient.authenticate(USER_4, ENCODED_PASSWORD_4)
    }
}