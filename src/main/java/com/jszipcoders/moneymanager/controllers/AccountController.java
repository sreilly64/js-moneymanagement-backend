package com.jszipcoders.moneymanager.controllers;

import com.jszipcoders.moneymanager.dto.AccountDTO;
import com.jszipcoders.moneymanager.repositories.entities.AccountEntity;
import com.jszipcoders.moneymanager.repositories.entities.TransactionHistoryEntity;
import com.jszipcoders.moneymanager.repositories.entities.TransactionType;
import com.jszipcoders.moneymanager.exceptions.InsufficientAccountInfoException;
import com.jszipcoders.moneymanager.exceptions.InsufficientFundsException;
import com.jszipcoders.moneymanager.controllers.requests.NicknameRequest;
import com.jszipcoders.moneymanager.controllers.responses.NicknameChangeResponse;
import com.jszipcoders.moneymanager.controllers.responses.TransactionResponse;
import com.jszipcoders.moneymanager.controllers.requests.TransferRequest;
import com.jszipcoders.moneymanager.services.AccountService;
import com.jszipcoders.moneymanager.services.TransactionHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
    private final AccountService accountService;
    private final TransactionHistoryService transactionHistoryService;
    private static final String ACCOUNT_NOT_FOUND = "Account not found.";

    @Autowired
    public AccountController(AccountService accountService, TransactionHistoryService transactionHistoryService) {
        this.accountService = accountService;
        this.transactionHistoryService = transactionHistoryService;
    }

    @GetMapping(value = "/accounts/{accountNumber}/transactions", produces = "application/json")
    public ResponseEntity<List<TransactionHistoryEntity>> getTransactionsByAccountNumber(@PathVariable Long accountNumber){
        List<TransactionHistoryEntity> history = transactionHistoryService.getTransactions(accountNumber);
        return ResponseEntity.ok(history);
    }

    @GetMapping(value = "/accounts/{accountNumber}", produces = "application/json")
    public ResponseEntity<AccountEntity> findAccountByAccountNumber(@PathVariable Long accountNumber) {
        AccountEntity accountEntity = accountService.findByAccountNumber(accountNumber);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(accountEntity);
    }

    @GetMapping(value = "/accounts/user/{userId}", produces = "application/json")
    public ResponseEntity<List<AccountEntity>> findAllAccountsByUserId(@PathVariable Long userId) {
        List<AccountEntity> accountEntityList = accountService.findAllAccountsByUserId(userId);
        return ResponseEntity.ok(accountEntityList);
    }

    @PutMapping(value = "/accounts/{accountNumber}/deposit/{amount}", produces = "application/json")
    public ResponseEntity<TransactionResponse> deposit(@PathVariable Long accountNumber, @PathVariable Double amount) {
        TransactionResponse response = null;
        try{
            response = accountService.deposit(accountNumber, amount);
            transactionHistoryService.save(accountNumber, null, amount, TransactionType.DEPOSIT);
        }catch(EntityNotFoundException e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.badRequest().body(new TransactionResponse(ACCOUNT_NOT_FOUND));
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/accounts/{accountNumber}/withdraw/{amount}")
    public ResponseEntity<TransactionResponse> withdraw(@PathVariable Long accountNumber, @PathVariable Double amount) {
        TransactionResponse response = null;
        try{
            response = accountService.withdraw(accountNumber, amount);
            transactionHistoryService.save(accountNumber, null, amount, TransactionType.WITHDRAW);
        }catch(InsufficientFundsException e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.badRequest().body(new TransactionResponse(e.getMessage()));
        }catch(EntityNotFoundException e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.badRequest().body(new TransactionResponse(ACCOUNT_NOT_FOUND));
        }catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/accounts/transfer")
    public ResponseEntity<TransactionResponse> transfer(@RequestBody TransferRequest request) {
        TransactionResponse response = null;
        try{
            response = accountService.transfer(request);
            transactionHistoryService.save(request.getFromAccountId(), request.getToAccountId(), request.getDollarAmount(), TransactionType.TRANSFER);
        }catch(InsufficientFundsException e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.badRequest().body(new TransactionResponse(e.getMessage()));
        }catch(EntityNotFoundException e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.badRequest().body(new TransactionResponse(ACCOUNT_NOT_FOUND));
        }catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/accounts/{accountNumber}/nickname")
    public ResponseEntity<NicknameChangeResponse> setNickname(@PathVariable Long accountNumber, @RequestBody NicknameRequest request){
        try{
            accountService.setNickname(accountNumber, request.getNickname());
        }catch(EntityNotFoundException e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.badRequest().body(new NicknameChangeResponse(ACCOUNT_NOT_FOUND));
        }catch(Exception e){
            LOGGER.info(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new NicknameChangeResponse(request));
    }

    @PostMapping(value = "/accounts", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AccountEntity> createAccount(@RequestBody AccountDTO newAccount) {
        AccountEntity accountEntity = null;
        try{
            accountEntity = accountService.createAccount(newAccount);
        }catch (InsufficientAccountInfoException e){
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
