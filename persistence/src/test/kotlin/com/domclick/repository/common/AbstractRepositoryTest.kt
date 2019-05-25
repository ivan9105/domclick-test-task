package com.domclick.repository.common

import com.domclick.config.PersistenceConfig
import com.domclick.repository.AccountRepository
import com.domclick.repository.UserRepository
import org.flywaydb.core.Flyway
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import org.springframework.context.annotation.PropertySource
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader
import javax.persistence.EntityManager

@RunWith(SpringRunner::class)
@PropertySource("classpath:application-test.properties")
@AutoConfigureDataJpa
@AutoConfigureTestEntityManager
@SpringBootTest(webEnvironment = NONE)
@ContextConfiguration(classes = [PersistenceConfig::class], loader = AnnotationConfigContextLoader::class)
@ImportAutoConfiguration
abstract class AbstractRepositoryTest {
    @Autowired
    lateinit var accountRepository: AccountRepository
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var em: EntityManager

    @Autowired
    lateinit var flyway: Flyway

    companion object {
        private var migrated = false
    }

    @Before
    fun migrate() {
        if (migrated) {
            return
        }

        flyway.clean()
        flyway.migrate()
        migrated = true
    }
}

//Todo testcontainers