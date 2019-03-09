package com.domclick.rest

import com.domclick.dto.request.AccountDepositRequest
import com.domclick.dto.request.AccountTransferRequest
import com.domclick.dto.request.AccountWithdrawRequest
import com.domclick.rest.common.AbstractControllerTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.OK
import java.math.BigDecimal
import kotlin.test.assertEquals


class AccountApiControllerTest : AbstractControllerTest() {

    @Value("classpath:json/account/account.json")
    lateinit var accountJson: Resource

    @Value("classpath:json/account/account_not_found.json")
    lateinit var accountNotFoundJson: Resource

    @Value("classpath:json/account/account_not_found_deposit.json")
    lateinit var accountNotFoundDepositJson: Resource

    @Value("classpath:json/account/account_not_found_withdraw.json")
    lateinit var accountNotFoundWithdrawJson: Resource

    @Value("classpath:json/account/account_not_found_transfer.json")
    lateinit var accountNotFoundTransferJson: Resource

    @Value("classpath:json/account/deposit_response.json")
    lateinit var depositResponseJson: Resource

    @Value("classpath:json/account/transfer_response.json")
    lateinit var transferResponseJson: Resource

    @Value("classpath:json/account/withdraw_response.json")
    lateinit var withdrawResponseJson: Resource

    @Value("classpath:json/account/account_do_deposit_value_less_or_equal_0.json")
    lateinit var accountDoDepositValueLessOrEqualZeroJson: Resource

    @Value("classpath:json/account/account_do_withdraw_value_less_or_equal_0.json")
    lateinit var accountDoWithdrawValueLessOrEqualZeroJson: Resource

    @Value("classpath:json/account/account_do_transfer_value_less_or_equal_0.json")
    lateinit var accountDoTransferValueLessOrEqualZeroJson: Resource

    @Value("classpath:json/account/account_do_transfer_from_account_equals_to_account.json")
    lateinit var accountDoTransferFromAccountEqualsToAccountJson: Resource

    @Value("classpath:json/account/account_do_transfer_not_enough_money.json")
    lateinit var accountDoTransferNotEnoughMoneyJson: Resource

    private val NOT_FOUND_ACCOUNT_ID = 9999L

    @Test
    fun getAllAccountsTest() {
        val response = httpGetRequest("/api/account/list")
        assertEquals(OK, response.statusCode)
    }

    @Test
    fun getAccountWithId_0_Happy_Path_Test() {
        val response = httpGetRequest("/api/account/get/0")
        assertEquals(OK, response.statusCode)
        assertJson(accountJson.getContent(), response.body!!)
    }

    @Test
    fun getAccountWithId_9999_Account_Not_Found_Test() {
        val response = httpGetRequest("/api/account/get/$NOT_FOUND_ACCOUNT_ID")
        assertEquals(BAD_REQUEST, response.statusCode)
        assertJsonWithTimestamp(accountNotFoundJson.getContent(), response.body!!)
    }

    @Test
    fun postDeposit_Happy_Path_Test() {
        val account = createValidAccount(BigDecimal(1000))

        val request = AccountDepositRequest().apply {
            accountId = account.id!!
            value = BigDecimal(500)
        }

        val response = httpPostRequest("/api/account/deposit", request)
        assertEquals(OK, response.statusCode)
        assertJsonWithId(depositResponseJson.getContent(), response.body!!)

        deleteAccount(account)
    }

    @Test
    fun postDeposit_Value_Less_Or_Equals_0_Test() {
        val request = AccountDepositRequest().apply {
            accountId = 0
            value = BigDecimal(0)
        }

        val response = httpPostRequest("/api/account/deposit", request)

        assertEquals(BAD_REQUEST, response.statusCode)
        assertJsonWithTimestamp(accountDoDepositValueLessOrEqualZeroJson.getContent(), response.body!!)
    }

    @Test
    fun postDeposit_Account_Not_Found_Test() {
        val request = AccountDepositRequest().apply {
            accountId = NOT_FOUND_ACCOUNT_ID
            value = BigDecimal(1)
        }

        val response = httpPostRequest("/api/account/deposit", request)
        assertEquals(BAD_REQUEST, response.statusCode)
        assertJsonWithTimestamp(accountNotFoundDepositJson.getContent(), response.body!!)
    }

    @Test
    fun postWithdraw_Happy_Path_Test() {
        val account = createValidAccount(BigDecimal(1000))

        val request = AccountWithdrawRequest().apply {
            accountId = account.id!!
            value = BigDecimal(500)
        }

        val response = httpPostRequest("/api/account/withdraw", request)
        assertEquals(OK, response.statusCode)
        assertJsonWithId(withdrawResponseJson.getContent(), response.body!!)

        deleteAccount(account)
    }

    @Test
    fun postWithdraw_Value_Less_Or_Equals_0_Test() {
        val request = AccountWithdrawRequest().apply {
            accountId = 0
            value = BigDecimal(0)
        }

        val response = httpPostRequest("/api/account/withdraw", request)

        assertEquals(BAD_REQUEST, response.statusCode)
        assertJsonWithTimestamp(accountDoWithdrawValueLessOrEqualZeroJson.getContent(), response.body!!)
    }

    @Test
    fun postWithdraw_Account_Not_Found_Test() {
        val request = AccountWithdrawRequest().apply {
            accountId = NOT_FOUND_ACCOUNT_ID
            value = BigDecimal(1)
        }

        val response = httpPostRequest("/api/account/withdraw", request)
        assertEquals(BAD_REQUEST, response.statusCode)
        assertJsonWithTimestamp(accountNotFoundWithdrawJson.getContent(), response.body!!)
    }

    @Test
    fun postTransfer_Happy_Path_Test() {
        val fromAccount = createValidAccount(BigDecimal(1000))
        var toAccount = createValidAccount(BigDecimal(700))

        val request = AccountTransferRequest().apply {
            fromAccountId = fromAccount.id!!
            toAccountId = toAccount.id!!
            value = BigDecimal(800)
        }

        val response = httpPostRequest("/api/account/transfer", request)
        assertEquals(OK, response.statusCode)

        assertJsonWithId(transferResponseJson.getContent(), response.body!!)

        toAccount = reloadAccount(toAccount)
        assertEquals(BigDecimal("1500.00"), toAccount.balance)

        deleteAccount(fromAccount)
        deleteAccount(toAccount)
    }

    @Test
    fun postTransfer_Value_Less_Or_Equals_0_Test() {
        val request = AccountTransferRequest().apply {
            fromAccountId = 0
            toAccountId = 1
            value = BigDecimal(0)
        }

        val response = httpPostRequest("/api/account/transfer", request)

        assertEquals(BAD_REQUEST, response.statusCode)
        assertJsonWithTimestamp(accountDoTransferValueLessOrEqualZeroJson.getContent(), response.body!!)
    }

    @Test
    fun postTransfer_From_Account_Equals_To_Account_Test() {
        val request = AccountTransferRequest().apply {
            fromAccountId = 0
            toAccountId = 0
            value = BigDecimal(100)
        }

        val response = httpPostRequest("/api/account/transfer", request)

        assertEquals(BAD_REQUEST, response.statusCode)
        assertJsonWithTimestamp(accountDoTransferFromAccountEqualsToAccountJson.getContent(), response.body!!)
    }

    @Test
    fun postTransfer_From_Account_Not_Found_Test() {
        val request = AccountTransferRequest().apply {
            fromAccountId = NOT_FOUND_ACCOUNT_ID
            toAccountId = 0
            value = BigDecimal(1)
        }

        val response = httpPostRequest("/api/account/transfer", request)
        assertEquals(BAD_REQUEST, response.statusCode)
        assertJsonWithTimestamp(accountNotFoundTransferJson.getContent(), response.body!!)
    }

    @Test
    fun postTransfer_To_Account_Not_Found_Test() {
        val request = AccountTransferRequest().apply {
            fromAccountId = 0
            toAccountId = NOT_FOUND_ACCOUNT_ID
            value = BigDecimal(1)
        }

        val response = httpPostRequest("/api/account/transfer", request)
        assertEquals(BAD_REQUEST, response.statusCode)
        assertJsonWithTimestamp(accountNotFoundTransferJson.getContent(), response.body!!)
    }

    @Test
    fun postTransfer_Not_Enough_Money_Test() {
        val fromAccount = createValidAccount(BigDecimal(1000))
        val toAccount = createValidAccount(BigDecimal(1000))

        val request = AccountTransferRequest().apply {
            fromAccountId = fromAccount.id!!
            toAccountId = toAccount.id!!
            value = BigDecimal(2000)
        }

        val response = httpPostRequest("/api/account/transfer", request)
        assertEquals(BAD_REQUEST, response.statusCode)

        assertJsonWithId(
                accountDoTransferNotEnoughMoneyJson.getContent()
                        .replaceWithId()
                        .replaceLineBreaksAndSpacesAndTimeStamp(),
                response.body!!
                        .replaceWithId()
                        .replaceLineBreaksAndSpacesAndTimeStamp()
        )

        deleteAccount(fromAccount)
        deleteAccount(toAccount)
    }
}