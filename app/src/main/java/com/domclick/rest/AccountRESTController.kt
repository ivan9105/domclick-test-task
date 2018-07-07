package com.domclick.rest

import com.domclick.dto.AccountDto
import com.domclick.dto.request.AccountDepositRequest
import com.domclick.dto.request.AccountTransferRequest
import com.domclick.dto.request.AccountWithdrawRequest
import com.domclick.dto.response.AccountResponse
import com.domclick.exception.BadRequestException
import com.domclick.repository.AccountRepository
import com.domclick.service.AccountManager
import com.domclick.utils.DtoBuilder
import org.assertj.core.util.Lists
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import javax.validation.Valid

@RestController
@RequestMapping("/api/account")
class AccountRESTController {
    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var dtoBuilder: DtoBuilder

    @Autowired
    private lateinit var accountManager: AccountManager

    @GetMapping("/list")
    fun accountList(): AccountResponse {
        return dtoBuilder.buildAccountResponse(Lists.newArrayList(accountRepository.findAll()))
    }

    @GetMapping("/get/{id}")
    @Throws(BadRequestException::class)
    fun getAccount(@PathVariable(name = "id") id: Long?): AccountDto {
        return doGetAccount(id!!)
    }

    @PostMapping("/transfer")
    @Throws(BadRequestException::class)
    fun doTransfer(@Valid @RequestBody request: AccountTransferRequest): ResponseEntity<AccountDto> {
        accountManager.transfer(request.fromAccountId, request.toAccountId, request.value)
        return ResponseEntity.ok<AccountDto>(this.doGetAccount(request.fromAccountId))
    }

    @PostMapping("/withdraw")
    @Throws(BadRequestException::class)
    fun dotWithdraw(@Valid @RequestBody request: AccountWithdrawRequest): ResponseEntity<AccountDto> {
        accountManager.withdraw(request.accountId, request.value)
        return ResponseEntity.ok<AccountDto>(doGetAccount(request.accountId))
    }

    @PostMapping("/deposit")
    @Throws(BadRequestException::class)
    fun doDeposit(@Valid @RequestBody request: AccountDepositRequest): ResponseEntity<AccountDto> {
        accountManager.deposit(request.accountId, request.value)
        return ResponseEntity.ok<AccountDto>(doGetAccount(request.accountId))
    }

    @Throws(BadRequestException::class)
    private fun doGetAccount(@PathVariable(name = "id") id: Long): AccountDto {
        val accountOptional = accountRepository.findById(id)
        if (!accountOptional.isPresent()) {
            throw BadRequestException(String.format("Account with id '%s' not found", id))
        }
        return dtoBuilder.buildAccountDto(accountOptional.get())
    }
}