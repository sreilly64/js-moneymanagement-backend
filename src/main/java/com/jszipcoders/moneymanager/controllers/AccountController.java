package com.jszipcoders.moneymanager.controllers;

import com.jszipcoders.moneymanager.entities.AccountEntity;
import com.jszipcoders.moneymanager.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = "/accounts/{account_number}")
    public AccountEntity findByAccountNumber(@PathVariable Long account_number) {
        return accountService.findByAccountNumber(account_number);
    }

    @GetMapping(value = "/accounts/user/{user_id}")
    public List<AccountEntity> findAllAccountsByUserId(@PathVariable Long user_id) {
        return accountService.findAllAccountsByUserId(user_id);
    }

    @PutMapping(value = "/accounts/{account_number}/update/{amount}")
    public Double updateBalance(@PathVariable Long account_number, @PathVariable Double amount) {
        return accountService.updateBalance(account_number, amount);
    }

    @PostMapping(value = "/accounts")
    public AccountEntity createAccount(@RequestBody AccountEntity newAccount) {
        return accountService.createAccount(newAccount);
    }

    @DeleteMapping(value = "/accounts/{account_number}")
    public boolean deleteAccount(@PathVariable Long account_number) {
        return accountService.deleteAccount(account_number);
    }

}
