package com.domclick.service

import com.domclick.config.ElasticConfig
import com.domclick.config.ElasticConfig.Companion.CONTAINER_HOST
import com.domclick.config.ElasticConfig.Companion.CONTAINER_PORT
import com.domclick.config.properties.ElasticProperties
import com.domclick.container.ElasticsearchContainer
import com.domclick.container.ElasticsearchContainer.Companion.ELASTICSEARCH_DEFAULT_TCP_PORT
import com.domclick.entity.AccountData
import com.domclick.entity.TagData
import com.domclick.entity.UserData
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.support.AnnotationConfigContextLoader
import java.lang.System.setProperty
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(
        classes = [ElasticProperties::class, ElasticConfig::class],
        loader = AnnotationConfigContextLoader::class
)
@ActiveProfiles("test")
class UserDataServiceTest {

    @Autowired
    lateinit var userDataService: UserDataService

    companion object {
        @ClassRule
        @JvmField
        var container = ElasticsearchContainer()

        @BeforeClass
        @JvmStatic
        fun init() {
            setProperty(CONTAINER_HOST, container.containerIpAddress)
            setProperty(CONTAINER_PORT, container.getMappedPort(ELASTICSEARCH_DEFAULT_TCP_PORT).toString())
        }
    }

    @Test
    fun crudTest() {
        var userData = getUserData()

        userData = userDataService.save(userData)

        assertEquals("Дмитрий", userData.firstName)
        assertEquals("Чернов", userData.lastName)
        assertEquals("Игоревич", userData.middleName)
        assertEquals("пользователь", userData.tags.iterator().next().value)
        assertEquals(55.0, userData.accounts.iterator().next().value)
//        assertEquals(LocalDate.of(2018, 1, 1), userData.createdDate)

        userData = userDataService.findById(userData.id!!)
        userData.firstName = "Анатолий"
        userData.tags.iterator().next().value = "HR"
        userData = userDataService.save(userData)

        assertEquals("Анатолий", userData.firstName)
        assertEquals("Чернов", userData.lastName)
        assertEquals("Игоревич", userData.middleName)
        assertEquals("HR", userData.tags.iterator().next().value)
        assertEquals(55.0, userData.accounts.iterator().next().value)
//        assertEquals(LocalDate.of(2018, 1, 1), userData.createdDate)

        userDataService.delete(userData.id!!)
        assertNull(userDataService.findById(userData.id!!))
    }

    @Test
    fun findByFirstNameTest() {
        var userData = getUserData()

        userData = userDataService.save(userData)

        userData = userDataService.findByFirstName(userData.firstName!!, Pageable.unpaged()).get().iterator().next()

        assertNotNull(userData)

        userDataService.delete(userData.id!!)
        assertNull(userDataService.findById(userData.id!!))
    }

    @Test
    fun findByTagValueTest() {
        var userData = getUserData()

        userData = userDataService.save(userData)

        userData = userDataService.findByTagValue(userData.tags.iterator().next().value!!, Pageable.unpaged()).get().iterator().next()

        assertNotNull(userData)

        userDataService.delete(userData.id!!)
        assertNull(userDataService.findById(userData.id!!))
    }

    @Test
    fun findByTagValueAndFirstNameTest() {
        var userData = getUserData()

        userData = userDataService.save(userData)

        userData = userDataService.findByTagValueAndFirstName(userData.tags.iterator().next().value!!, userData.firstName!!, Pageable.unpaged()).get().iterator().next()

        assertNotNull(userData)

        userDataService.delete(userData.id!!)
        assertNull(userDataService.findById(userData.id!!))
    }

    private fun getUserData(): UserData {
        return UserData().apply {
            firstName = "Дмитрий"
            lastName = "Чернов"
            middleName = "Игоревич"
            tags = mutableSetOf(TagData().apply { value = "пользователь" })
            accounts = mutableSetOf(AccountData().apply { value = 55.0 })
//            createdDate = LocalDate.of(2018, 1, 1)
        }
    }
}