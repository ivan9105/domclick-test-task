package com.domclick.rest;

import com.domclick.dto.AccountDto;
import com.domclick.dto.request.AccountDepositRequest;
import com.domclick.dto.request.AccountTransferRequest;
import com.domclick.dto.request.AccountWithdrawRequest;
import com.domclick.dto.response.AccountResponse;
import com.domclick.exception.BadRequestException;
import com.domclick.model.Account;
import com.domclick.repository.AccountRepository;
import com.domclick.service.AccountManager;
import com.domclick.utils.DtoBuilder;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/account")
public class AccountRESTController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DtoBuilder dtoBuilder;

    @Autowired
    private AccountManager accountManager;

    @GetMapping("/list")
    public AccountResponse accountList() {
        return dtoBuilder.buildAccountResponse(Lists.newArrayList(accountRepository.findAll()));
    }

    @GetMapping("/get/{id}")
    public AccountDto getAccount(@PathVariable(name = "id") Long id) throws BadRequestException {
        return doGetAccount(id);
    }

    @PostMapping("/transfer")
    public ResponseEntity<AccountDto> doTransfer(@Valid @RequestBody AccountTransferRequest request) throws BadRequestException {
        accountManager.transfer(request.getFromAccountId(), request.getToAccountId(), request.getValue());
        return ResponseEntity.ok(doGetAccount(request.getFromAccountId()));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<AccountDto> dotWithdraw(@Valid @RequestBody AccountWithdrawRequest request) throws BadRequestException {
        accountManager.withdraw(request.getAccountId(), request.getValue());
        return ResponseEntity.ok(doGetAccount(request.getAccountId()));
    }

    @PostMapping("/deposit")
    public ResponseEntity<AccountDto> doDeposit(@Valid @RequestBody AccountDepositRequest request) throws BadRequestException {
        accountManager.deposit(request.getAccountId(), request.getValue());
        return ResponseEntity.ok(doGetAccount(request.getAccountId()));
    }

    private AccountDto doGetAccount(@PathVariable(name = "id") Long id) throws BadRequestException {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (!accountOptional.isPresent()) {
            throw new BadRequestException(String.format("Account with id '%s' not found", id));
        }
        return dtoBuilder.buildAccountDto(accountOptional.get());
    }
}