package com.domclick.service

import com.domclick.config.ElasticConfig
import com.domclick.config.ElasticConfig.Companion.CONTAINER_HOST
import com.domclick.config.ElasticConfig.Companion.CONTAINER_PORT
import com.domclick.config.properties.ElasticProperties
import com.domclick.container.ElasticsearchContainer
import com.domclick.container.ElasticsearchContainer.Companion.ELASTICSEARCH_DEFAULT_TCP_PORT
import com.domclick.entity.AccountData
import com.domclick.entity.AddressData
import com.domclick.entity.TagData
import com.domclick.entity.UserData
import com.domclick.entity.enums.Role
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
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
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
        fun init() {  //Todo use initializer
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
        assertEquals(55.0, userData.accounts.iterator().next().balance)
        assertEquals(getLocalDate(), userData.createdDate)
        assertEquals(Role.RCIK, userData.role)
        assertEquals(getLocalDateTime(), userData.lastLoginDateTime)
        assertAddress(userData)

        userData = userDataService.findById(userData.id!!)
        userData.firstName = "Анатолий"
        userData.tags.iterator().next().value = "HR"
        userData = userDataService.save(userData)

        assertEquals("Анатолий", userData.firstName)
        assertEquals("Чернов", userData.lastName)
        assertEquals("Игоревич", userData.middleName)
        assertEquals("HR", userData.tags.iterator().next().value)
        assertEquals(55.0, userData.accounts.iterator().next().balance)
        assertEquals(getLocalDate(), userData.createdDate)
        assertEquals(Role.RCIK, userData.role)
        assertEquals(getLocalDateTime(), userData.lastLoginDateTime)
        assertAddress(userData)

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

    @Test
    fun getAvgBalanceByFirstNameTest() {
        var userData = getUserData().apply {
            firstName = "Мария"
        }

        userData = userDataService.save(userData)

        val avg = userDataService.getAvgBalanceByFirstName(userData.firstName!!)
        assertEquals(BigDecimal("55.0"), avg)

        userDataService.delete(userData.id!!)
        assertNull(userDataService.findById(userData.id!!))
    }

    @Test
    fun getAvgBalanceByFirstNamePainLessTest() {
        var userData = getUserData().apply {
            firstName = "Мария"
            accounts = setOf(
                    AccountData().apply {
                        balance = 1000.0
                    },
                    AccountData().apply {
                        balance = 500.0
                    },
                    AccountData().apply {
                        balance = 2000.0
                    }
            )
        }

        userData = userDataService.save(userData)

        val avg = userDataService.getAvgBalanceByFirstNamePainless(userData.firstName!!)
        assertEquals(BigDecimal("1166.6666666666667"), avg)

        userDataService.delete(userData.id!!)
        assertNull(userDataService.findById(userData.id!!))
    }

    private fun getUserData(): UserData {
        return UserData().apply {
            firstName = "Дмитрий"
            lastName = "Чернов"
            middleName = "Игоревич"
            tags = mutableSetOf(TagData().apply { value = "пользователь" })
            accounts = mutableSetOf(AccountData().apply { balance = 55.0 })
            createdDate = getLocalDate()
            role = Role.RCIK
            lastLoginDateTime = getLocalDateTime()
            address = AddressData().apply {
                unstructured = "Самара Ново-Садовая дом 44 строение 2"
                region = "63"
                city = "Самара"
                cityType = "г"
                street = "Ново-Садовая"
                house = 4
                houseType = "д"
                block = "строение 2"
                flat = 12
                flatType = "кв"
            }
        }
    }

    private fun getLocalDateTime() = LocalDateTime.of(
            getLocalDate(),
            LocalTime.of(1, 1, 1)
    )

    private fun getLocalDate() = LocalDate.of(2018, 1, 1)

    private fun assertAddress(userData: UserData) {
        val address = userData.address!!
        assertEquals("Самара Ново-Садовая дом 44 строение 2", address.unstructured)
        assertEquals("63", address.region)
        assertEquals("Самара", address.city)
        assertEquals("г", address.cityType)
        assertEquals("Ново-Садовая", address.street)
        assertEquals(4, address.house)
        assertEquals("д", address.houseType)
        assertEquals("строение 2", address.block)
        assertEquals(12, address.flat)
        assertEquals("кв", address.flatType)
    }
}