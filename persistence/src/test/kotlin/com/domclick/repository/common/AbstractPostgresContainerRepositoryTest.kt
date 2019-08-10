package com.domclick.repository.common

import com.domclick.config.PersistenceConfig
import com.domclick.repository.AccountRepository
import com.domclick.repository.CompanyRepository
import com.domclick.repository.UserRepository
import com.domclick.repository.container.PostgreSQLContainer
import mu.KLogging
import org.junit.ClassRule
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader
import org.springframework.transaction.PlatformTransactionManager
import javax.persistence.EntityManager

@RunWith(SpringRunner::class)
@PropertySource("classpath:application-postgres-test.properties")
@AutoConfigureDataJpa
@SpringBootTest(webEnvironment = NONE)
@ContextConfiguration(
        initializers = [AbstractPostgresContainerRepositoryTest.Initializer::class],
        classes = [PersistenceConfig::class],
        loader = AnnotationConfigContextLoader::class
)
@Configuration
abstract class AbstractPostgresContainerRepositoryTest {
    @Autowired
    lateinit var accountRepository: AccountRepository
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var companyRepository: CompanyRepository
    @Autowired
    lateinit var em: EntityManager
    @Autowired
    lateinit var transactionManager: PlatformTransactionManager

    companion object : KLogging() {
        @ClassRule
        @JvmField
        var container = PostgreSQLContainer()
                .withDatabaseName("domclick")
                .withUsername("root")
                .withPassword("root")
    }

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
            TestPropertyValues.of("spring.datasource.url=${container.jdbcUrl}")
                    .applyTo(configurableApplicationContext.environment)
        }
    }
}