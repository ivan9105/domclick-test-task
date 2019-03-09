package com.domclick.rest

import com.domclick.dto.AccountDto
import com.domclick.dto.request.AccountDepositRequest
import com.domclick.dto.request.AccountTransferRequest
import com.domclick.dto.request.AccountWithdrawRequest
import com.domclick.exception.BadRequestException
import com.domclick.service.AccountService
import com.domclick.utils.DtoBuilder
import org.assertj.core.util.Lists
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.String.format
import javax.validation.Valid

@RestController
@RequestMapping("/api/account")
class AccountApiController(
        private val dtoBuilder: DtoBuilder,
        private val accountService: AccountService
) {

    @GetMapping("/list")
    fun accountList() = dtoBuilder.buildAccountResponse(Lists.newArrayList(accountService.findAll()))


    @GetMapping("/get/{id}")
    @Throws(BadRequestException::class)
    fun getAccount(@PathVariable(name = "id") id: Long?) = doGetAccount(id!!)

    @PostMapping("/transfer")
    @Throws(BadRequestException::class)
    fun doTransfer(@Valid @RequestBody request: AccountTransferRequest): ResponseEntity<AccountDto> {
        accountService.transfer(request.fromAccountId, request.toAccountId, request.value)
        return ResponseEntity.ok<AccountDto>(this.doGetAccount(request.fromAccountId))
    }

    @PostMapping("/withdraw")
    @Throws(BadRequestException::class)
    fun dotWithdraw(@Valid @RequestBody request: AccountWithdrawRequest): ResponseEntity<AccountDto> {
        accountService.withdraw(request.accountId, request.value)
        return ResponseEntity.ok<AccountDto>(doGetAccount(request.accountId))
    }

    @PostMapping("/deposit")
    @Throws(BadRequestException::class)
    fun doDeposit(@Valid @RequestBody request: AccountDepositRequest): ResponseEntity<AccountDto> {
        accountService.deposit(request.accountId, request.value)
        return ResponseEntity.ok<AccountDto>(doGetAccount(request.accountId))
    }

    @Throws(BadRequestException::class)
    private fun doGetAccount(@PathVariable(name = "id") id: Long): AccountDto {
        val accountOptional = accountService.findById(id)
        if (!accountOptional.isPresent) {
            throw BadRequestException(format("Account with id '%s' not found", id))
        }
        return dtoBuilder.buildAccountDto(accountOptional.get())
    }
}