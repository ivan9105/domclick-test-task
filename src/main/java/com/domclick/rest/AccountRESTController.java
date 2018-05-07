package com.domclick.rest;

import com.domclick.dto.request.AccountDepositRequest;
import com.domclick.dto.request.AccountTransferRequest;
import com.domclick.dto.request.AccountWithdrawRequest;
import com.domclick.repository.AccountRepository;
import com.domclick.utils.DtoBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/account")
public class AccountRESTController {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DtoBuilder dtoBuilder;

//    @GetMapping("/list")
//    public AccountResponse accountList() {
//
//    }

    @PostMapping("/transfer")
    public ResponseEntity<?> doTransfer(@Valid @RequestBody AccountTransferRequest request) {
        //Todo
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> dotWithdraw(@Valid @RequestBody AccountWithdrawRequest request) {
        //Todo
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> dotDeposit(@Valid @RequestBody AccountDepositRequest request) {
        //Todo
        return ResponseEntity.ok().build();
    }


    //Todo Exception Handler
}