package com.jszipcoders.moneymanager.controllers;

import com.jszipcoders.moneymanager.entities.AccountEntity;
import com.jszipcoders.moneymanager.entities.TransactionHistoryEntity;
import com.jszipcoders.moneymanager.entities.TransactionType;
import com.jszipcoders.moneymanager.requests.NicknameRequest;
import com.jszipcoders.moneymanager.responses.TransactionResponse;
import com.jszipcoders.moneymanager.requests.TransferRequest;
import com.jszipcoders.moneymanager.services.AccountService;
import com.jszipcoders.moneymanager.services.TransactionHistoryService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
    private AccountService accountService;
    private TransactionHistoryService transactionHistoryService;

    @Autowired
    public AccountController(AccountService accountService, TransactionHistoryService transactionHistoryService) {
        this.accountService = accountService;
        this.transactionHistoryService = transactionHistoryService;
    }

    @GetMapping(value = "/accounts/{accountNumber}/transactions")
    public ResponseEntity<?> getTransactions(@PathVariable Long accountNumber){
        List<TransactionHistoryEntity> history = null;
        try {
            history = transactionHistoryService.getTransactions(accountNumber);
        }catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(history);
    }

    @GetMapping(value = "/accounts/{accountNumber}")
    public ResponseEntity<AccountEntity> findByAccountNumber(@PathVariable Long accountNumber) {
        AccountEntity accountEntity = null;
        try{
            accountEntity = accountService.findByAccountNumber(accountNumber);
        }catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(accountEntity, HttpStatus.OK);
    }

    @GetMapping(value = "/accounts/user/{userId}")
    public ResponseEntity<List<AccountEntity>> findAllAccountsByUserId(@PathVariable Long userId) {
       List<AccountEntity> accountEntityList = new ArrayList<>();
        try{
            accountEntityList = accountService.findAllAccountsByUserId(userId);
        } catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(accountEntityList, HttpStatus.OK);
    }

    @PutMapping(value = "/accounts/{accountNumber}/deposit/{amount}")
    public ResponseEntity<?> deposit(@PathVariable Long accountNumber, @PathVariable Double amount) {
        TransactionResponse response = null;
        try{
            response = accountService.deposit(accountNumber, amount);
            transactionHistoryService.save(accountNumber, null, amount, TransactionType.DEPOSIT);
        }catch(NoSuchElementException e){
            LOGGER.info(e.getMessage(), e);
            JSONObject json = new JSONObject();
            json.put("message", "Invalid account number");
            return new ResponseEntity<>(json.toString(), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/accounts/{accountNumber}/withdraw/{amount}")
    public ResponseEntity<?> withdraw(@PathVariable Long accountNumber, @PathVariable Double amount) {
        TransactionResponse response = null;
        try{
            response = accountService.withdraw(accountNumber, amount);
            transactionHistoryService.save(accountNumber, null, amount, TransactionType.WITHDRAW);
        }catch(InvalidParameterException e){
            LOGGER.info(e.getMessage(), e);
            JSONObject json = new JSONObject();
            json.put("message", e.getMessage());
            return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
        }catch(NoSuchElementException e){
            LOGGER.info(e.getMessage(), e);
            JSONObject json = new JSONObject();
            json.put("message", "Invalid account number");
            return new ResponseEntity<>(json.toString(), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/accounts/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
        TransactionResponse response = null;
        try{
            response = accountService.transfer(request);
            transactionHistoryService.save(request.getFromAccountId(), request.getToAccountId(), request.getDollarAmount(), TransactionType.TRANSFER);
        }catch(InvalidParameterException e){
            LOGGER.info(e.getMessage(), e);
            JSONObject json = new JSONObject();
            json.put("message", e.getMessage());
            return new ResponseEntity<>(json.toString(), HttpStatus.BAD_REQUEST);
        }catch(NoSuchElementException e){
            LOGGER.info(e.getMessage(), e);
            JSONObject json = new JSONObject();
            json.put("message", "Invalid account number");
            return new ResponseEntity<>(json.toString(), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/accounts/{accountNumber}/nickname")
    public ResponseEntity<?> setNickname(@PathVariable Long accountNumber, @RequestBody NicknameRequest request){
        AccountEntity updatedAccount = null;
        try{
            updatedAccount = accountService.setNickname(accountNumber, request.getNickname());
        }catch(NoSuchElementException e){
            LOGGER.info(e.getMessage(), e);
            JSONObject json = new JSONObject();
            json.put("message", "Invalid account number");
            return new ResponseEntity<>(json.toString(), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updatedAccount);
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
        return new ResponseEntity<>(accountEntity, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/accounts/{accountNumber}")
    public ResponseEntity<Boolean> deleteAccount(@PathVariable Long accountNumber) {
        Boolean accountDeleted = null;
        try{
            accountDeleted = accountService.deleteAccount(accountNumber);
        }catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(accountDeleted, HttpStatus.OK);
    }

}
