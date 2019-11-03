package com.domclick.rest.common

import com.domclick.app.RestApplication
import com.domclick.entity.AccountEntity
import com.domclick.entity.UserEntity
import com.domclick.repository.AccountRepository
import com.domclick.repository.UserRepository
import org.flywaydb.core.Flyway
import org.junit.Before
import org.junit.runner.RunWith
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode.LENIENT
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.core.io.Resource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.io.InputStream
import java.math.BigDecimal
import java.nio.charset.Charset
import kotlin.text.Charsets.UTF_8
//Todo убыстрить тесты исп отдельные конфиги и исп моки на репозитории
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [RestApplication::class], webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
abstract class AbstractControllerTest {
    @LocalServerPort
    var port: Int? = null

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var userRepository: UserRepository

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

    protected fun url(path: String) = "http://localhost:$port$path"

    private val TIMESTAMP_REGEX = Regex("\"timestamp\":\\d+,")
    private val ID_REGEX = Regex("\"id\":\\d+,")
    private val GET_ID_REGEX = Regex("get/\\d+")
    private val WITH_ID_REGEX = Regex("with id '\\d+'")

    fun InputStream.readContent(charset: Charset = UTF_8) = this.bufferedReader(charset).use { it.readText() }

    fun Resource.getContent(charset: Charset = UTF_8) = this.inputStream.readContent(charset)

    fun httpGetRequest(path: String, headers: HttpHeaders = HttpHeaders()) =
            restTemplate.exchange<String>(
                    url(path),
                    GET,
                    HttpEntity<String>(null, headers),
                    String::class
            )

    fun httpPostRequest(path: String, body: Any?, headers: HttpHeaders = HttpHeaders()) =
            restTemplate.exchange<String>(
                    url(path),
                    POST,
                    HttpEntity(body, headers),
                    String::class
            )

    fun assertJson(expected: String, actual: String) {
        JSONAssert.assertEquals(expected, actual, LENIENT)
    }

    fun assertJsonWithTimestamp(expected: String, actual: String) {
        //Todo JSONCompareMode
        JSONAssert.assertEquals(
                expected.replaceLineBreaksAndSpacesAndTimeStamp(),
                actual.replaceLineBreaksAndSpacesAndTimeStamp(),
                LENIENT
        )
    }

    fun assertJsonWithId(expected: String, actual: String) {
        //Todo JSONCompareMode
        JSONAssert.assertEquals(
                expected.replaceLineBreaksAndSpacesAndId(),
                actual.replaceLineBreaksAndSpacesAndId(),
                LENIENT
        )
    }

    private fun String.replaceLineBreaksAndSpaces() =
            this
                    .replace(" ", "")
                    .replace("\n", "")
                    .replace("\r", "")

    fun String.replaceLineBreaksAndSpacesAndTimeStamp() =
            this
                    .replaceLineBreaksAndSpaces()
                    .replace(TIMESTAMP_REGEX, "\"timestamp\":1,")

    fun String.replaceWithId() = this.replace(WITH_ID_REGEX, "with id '1'")

    private fun String.replaceLineBreaksAndSpacesAndId() =
            this
                    .replaceLineBreaksAndSpaces()
                    .replace(ID_REGEX, "\"id\":1,")
                    .replace(GET_ID_REGEX, "get/1")

    fun createValidUser() = userRepository.save(UserEntity("Иванов", "Иван", "Иванович"))

    fun createValidAccount(
            balance: BigDecimal = BigDecimal(10)
    ) = accountRepository.save(AccountEntity(balance, createValidUser()))

    fun deleteAccount(account: AccountEntity) = accountRepository.delete(reloadAccount(account))

    fun reloadAccount(account: AccountEntity) = accountRepository.findById(account.id!!).get()

}