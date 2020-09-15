package com.jszipcoders.moneymanager.controllers;

import com.jszipcoders.moneymanager.entities.AccountEntity;
import com.jszipcoders.moneymanager.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = "/accounts/{account_number}")
    public ResponseEntity<AccountEntity> findByAccountNumber(@PathVariable Long account_number) {
        AccountEntity accountEntity = null;
        try{
            accountEntity = accountService.findByAccountNumber(account_number);
        }catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
            return new ResponseEntity<AccountEntity>(accountEntity, HttpStatus.OK);
    }

    @GetMapping(value = "/accounts/user/{user_id}")
    public ResponseEntity<List<AccountEntity>> findAllAccountsByUserId(@PathVariable Long user_id) {
       List<AccountEntity> accountEntityList = new ArrayList<>();
        try{
            accountEntityList = accountService.findAllAccountsByUserId(user_id);
        } catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<List<AccountEntity>>(accountEntityList, HttpStatus.OK);
    }

    @PutMapping(value = "/accounts/{account_number}/update/{amount}")
    public ResponseEntity<Double> updateBalance(@PathVariable Long account_number, @PathVariable Double amount) {
        Double balance = null;
        try{
            balance = accountService.updateBalance(account_number, amount);
        }catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<Double>(balance, HttpStatus.OK);
    }

    @PostMapping(value = "/accounts")
    public ResponseEntity<AccountEntity> createAccount(@RequestBody AccountEntity newAccount) {
        AccountEntity accountEntity = null;
        try{
            accountEntity = accountService.createAccount(newAccount);
        }catch (Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<AccountEntity>(accountEntity, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/accounts/{account_number}")
    public ResponseEntity<Boolean> deleteAccount(@PathVariable Long account_number) {
        Boolean accountDeleted = null;
        try{
            accountDeleted = accountService.deleteAccount(account_number);
        }catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Boolean>(accountDeleted, HttpStatus.OK);
    }

}
