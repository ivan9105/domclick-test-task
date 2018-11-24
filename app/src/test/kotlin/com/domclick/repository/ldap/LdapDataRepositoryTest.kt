package com.domclick.repository.ldap

import com.domclick.test_config.LdapTestConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader

@RunWith(SpringJUnit4ClassRunner::class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration(classes = [(LdapTestConfig::class)], loader = AnnotationConfigContextLoader::class)
class LdapDataRepositoryTest {
    @Autowired
    lateinit var ldapUserRepository: LdapUserRepository

    @Test
    fun test() {
        //Todo start embedded ldap in test context environment
        //Todo mock password
        //Todo dn* userDn search mask
        //Todo run org.springframework.security.ldap.DefaultSpringSecurityContextSource
//        ldapUserRepository.findAll()
    }
}