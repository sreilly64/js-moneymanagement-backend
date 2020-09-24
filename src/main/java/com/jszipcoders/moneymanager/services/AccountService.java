package com.jszipcoders.moneymanager.services;

import com.jszipcoders.moneymanager.entities.AccountEntity;
import com.jszipcoders.moneymanager.entities.TransactionResponse;
import com.jszipcoders.moneymanager.entities.TransferRequest;
import com.jszipcoders.moneymanager.entities.UserEntity;
import com.jszipcoders.moneymanager.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountEntity findByAccountNumber(Long accountNumber) {
        return this.accountRepository.findById(accountNumber).get();
    }

    public List<AccountEntity> findAllAccountsByUserId(Long user_id) {
        List<AccountEntity> listOfAccounts = this.accountRepository.findAll();
        return listOfAccounts.stream().filter(a -> a.getUserId() == user_id).collect(Collectors.toList());
    }

    public AccountEntity createAccount(AccountEntity newAccount) {
        return accountRepository.save(newAccount);
    }

    public boolean deleteAccount(Long account_number) {
        AccountEntity account = accountRepository.findById(account_number).orElse(null);
        accountRepository.deleteById(account_number);
        if(account == null) {
            return false;
        } else {
            return true;
        }
    }

    public TransactionResponse deposit(Long account_number, Double amount) {
        AccountEntity accountEntity = this.accountRepository.findById(account_number).get();
        BigDecimal newBalance = new BigDecimal(accountEntity.getBalance() + amount).setScale(2, RoundingMode.HALF_UP);
        accountEntity.setBalance(newBalance.doubleValue());
        accountRepository.save(accountEntity);
        return new TransactionResponse("deposit", amount, false);
    }

    public TransactionResponse withdraw(Long account_number, Double amount) {
        AccountEntity accountEntity = this.accountRepository.findById(account_number).get();
        if(accountEntity.getBalance() < 0){
            throw new InvalidParameterException("Insufficient funds, account already over drafted");
        }
        BigDecimal newBalance = new BigDecimal(accountEntity.getBalance() - amount).setScale(2, RoundingMode.HALF_UP);
        if(newBalance.doubleValue() >= 0){
            accountEntity.setBalance(newBalance.doubleValue());
            accountRepository.save(accountEntity);
            return new TransactionResponse("withdrawal", amount, false);
        }else if (newBalance.doubleValue() < -100.00){
            throw new InvalidParameterException("Insufficient funds");
        }else {
            accountEntity.setBalance(newBalance.doubleValue() - 25.00);
            accountRepository.save(accountEntity);
            return new TransactionResponse("withdrawal", amount, true);
        }
    }

    public TransactionResponse transfer(TransferRequest request) {
        TransactionResponse withdrawResponse = withdraw(request.getFromAccountId(), request.getDollarAmount());
        deposit(request.getToAccountId(), request.getDollarAmount());
        return new TransactionResponse("transfer", request.getDollarAmount(), withdrawResponse.getOverDrafted());
    }
}
