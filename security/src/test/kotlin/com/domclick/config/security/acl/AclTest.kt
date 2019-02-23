package com.domclick.config.security.acl

import com.domclick.config.PersistenceConfig
import com.domclick.config.properties.PersistenceProperties
import com.domclick.config.security.config.acl.AclConfig
import com.domclick.config.security.config.acl.AclMethodSecurityConfig
import com.domclick.repository.AnswerRepository
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.PropertySource
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringJUnit4ClassRunner::class)
@PropertySource("classpath:application-acl-test.properties")
@ComponentScan(basePackages = ["com.domclick.config.security.config.acl"])
@EnableTransactionManagement
@AutoConfigureDataJpa
@ImportAutoConfiguration(exclude = [(FlywayAutoConfiguration::class)])
@EntityScan(value = ["com.domclick.entity.acl"])
@EnableJpaRepositories(basePackages = ["com.domclick.repository"])
@ContextConfiguration(
        classes = [
            PersistenceProperties::class, PersistenceConfig::class,
            AclConfig::class, AclMethodSecurityConfig::class
        ],
        loader = AnnotationConfigContextLoader::class
)
@Transactional(propagation = NOT_SUPPORTED)
class AclTest {
    @Autowired
    lateinit var answerRepository: AnswerRepository

    @Rule
    @JvmField
    final val expectedException = ExpectedException.none()!!

    @Test
    @WithMockUser(username = "PROJECT_MANAGER")
    fun projectManagerCanWriteEntityWithId_1() {
        checkWritePermission(1L)
    }

    @Test
    @WithMockUser(username = "PROJECT_MANAGER")
    fun projectManageCannotWriteEntityWithId_4() {
        expectAccessDenied(4L)
    }

    @Test
    @WithMockUser(username = "DEVELOPER")
    fun developerCanWriteEntityWithId_5() {
        checkWritePermission(5L)
    }

    @Test
    @WithMockUser(username = "DEVELOPER")
    fun developerCannotWriteEntityWithId_2() {
        expectAccessDenied(2L)
    }

    @Test
    @WithMockUser(username = "TEXT_WRITER")
    fun textWriterCannotWriteEntityWithId_3() {
        expectAccessDenied(3L)
    }

    private fun checkWritePermission(id: Long) {
        var answer = answerRepository.findById(id).get()
        val oldContent = answer.content
        answer.content = "Change answer"
        answer = answerRepository.save(answer).apply { content = oldContent }
        answerRepository.save(answer)
    }

    private fun expectAccessDenied(id: Long) {
        expectedException.expect(AccessDeniedException::class.java)
        expectedException.expectMessage("Доступ запрещен")
        answerRepository.save(
                answerRepository.findById(id).get().apply {
                    content = "Доступ запрещен"
                }
        )
    }
}