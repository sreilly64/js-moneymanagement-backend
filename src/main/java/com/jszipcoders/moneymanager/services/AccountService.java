package com.jszipcoders.moneymanager.services;

import com.jszipcoders.moneymanager.dto.AccountDTO;
import com.jszipcoders.moneymanager.repositories.entities.AccountEntity;
import com.jszipcoders.moneymanager.exceptions.InsufficientAccountInfoException;
import com.jszipcoders.moneymanager.exceptions.InsufficientFundsException;
import com.jszipcoders.moneymanager.controllers.responses.TransactionResponse;
import com.jszipcoders.moneymanager.controllers.requests.TransferRequest;
import com.jszipcoders.moneymanager.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountEntity findByAccountNumber(Long accountNumber) {
        Optional<AccountEntity> accountOptional = this.accountRepository.findById(accountNumber);
        if(accountOptional.isPresent()){
            return accountOptional.get();
        }else{
            throw new EntityNotFoundException("Account not found.");
        }
    }

    public List<AccountEntity> findAllAccountsByUserId(Long userId) {
        List<AccountEntity> listOfAccounts = this.accountRepository.findAll();
        return listOfAccounts.stream().filter(a -> a.getUserId().equals(userId)).collect(Collectors.toList());
    }

    public AccountEntity createAccount(AccountDTO newAccount) throws InsufficientAccountInfoException {
        //this should probably check if the userId in the DTO actually exists in database
        AccountEntity newAccountEntity = new AccountEntity();
        if(newAccount.getType() != null && newAccount.getUserId() != null && newAccount.getBalance() != null){
            newAccountEntity.setType(newAccount.getType());
            newAccountEntity.setUserId(newAccount.getUserId());
            newAccountEntity.setBalance(newAccount.getBalance());
            newAccountEntity.setNickname(newAccount.getNickname());
        }else{
            throw new InsufficientAccountInfoException("Not enough information to create account.");
        }

        return accountRepository.save(newAccountEntity);
    }

    public boolean deleteAccount(Long accountNumber) {
        AccountEntity account = accountRepository.findById(accountNumber).orElse(null);
        accountRepository.deleteById(accountNumber);
        return account != null;
    }

    public TransactionResponse deposit(Long accountNumber, Double amount) {
        AccountEntity accountEntity = this.accountRepository.getOne(accountNumber);
        BigDecimal newBalance = BigDecimal.valueOf(accountEntity.getBalance() + amount).setScale(2, RoundingMode.HALF_UP);
        accountEntity.setBalance(newBalance.doubleValue());
        accountRepository.save(accountEntity);
        return new TransactionResponse("deposit", amount, false);
    }

    public TransactionResponse withdraw(Long accountNumber, Double amount) throws InsufficientFundsException {
        AccountEntity accountEntity = this.accountRepository.getOne(accountNumber);
        if(accountEntity.getBalance() < 0){
            throw new InsufficientFundsException("Insufficient funds for withdrawal, account already over drafted.");
        }
        BigDecimal newBalance = BigDecimal.valueOf(accountEntity.getBalance() - amount).setScale(2, RoundingMode.HALF_UP);
        Double newBalanceDoubleValue = newBalance.doubleValue();
        if(newBalanceDoubleValue >= 0){
            accountEntity.setBalance(newBalanceDoubleValue);
            accountRepository.save(accountEntity);
            return new TransactionResponse("withdrawal", amount, false);
        }else if (newBalanceDoubleValue < -100.00){
            throw new InsufficientFundsException("Insufficient funds for withdrawal.");
        }else {
            accountEntity.setBalance(newBalanceDoubleValue - 25.00);
            accountRepository.save(accountEntity);
            return new TransactionResponse("withdrawal", amount, true);
        }
    }

    public TransactionResponse transfer(TransferRequest request) throws InsufficientFundsException {
        TransactionResponse withdrawResponse = withdraw(request.getFromAccountId(), request.getDollarAmount());
        deposit(request.getToAccountId(), request.getDollarAmount());
        return new TransactionResponse("transfer", request.getDollarAmount(), withdrawResponse.getOverDrafted());
    }

    public AccountEntity setNickname(Long accountNumber, String nickname) {
        AccountEntity account = findByAccountNumber(accountNumber);
        account.setNickname(nickname);
        return this.accountRepository.save(account);
    }
}
