package com.jszipcoders.moneymanager.services;

import com.jszipcoders.moneymanager.entities.AccountEntity;
import com.jszipcoders.moneymanager.responses.TransactionResponse;
import com.jszipcoders.moneymanager.requests.TransferRequest;
import com.jszipcoders.moneymanager.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountEntity findByAccountNumber(Long accountNumber) throws NoSuchElementException {
        Optional<AccountEntity> account = this.accountRepository.findById(accountNumber);
        if(account.isPresent()){
            return account.get();
        }else{
            throw new NoSuchElementException("Account not found.");
        }
    }

    public List<AccountEntity> findAllAccountsByUserId(Long userId) {
        List<AccountEntity> listOfAccounts = this.accountRepository.findAll();
        return listOfAccounts.stream().filter(a -> a.getUserId() == userId).collect(Collectors.toList());
    }

    public AccountEntity createAccount(AccountEntity newAccount) {
        return accountRepository.save(newAccount);
    }

    public boolean deleteAccount(Long accountNumber) {
        AccountEntity account = accountRepository.findById(accountNumber).orElse(null);
        accountRepository.deleteById(accountNumber);
        return account != null;
    }

    public TransactionResponse deposit(Long accountNumber, Double amount) {
        AccountEntity accountEntity = this.accountRepository.findById(accountNumber).get();
        BigDecimal newBalance = BigDecimal.valueOf(accountEntity.getBalance() + amount).setScale(2, RoundingMode.HALF_UP);
        accountEntity.setBalance(newBalance.doubleValue());
        accountRepository.save(accountEntity);
        return new TransactionResponse("deposit", amount, false);
    }

    public TransactionResponse withdraw(Long accountNumber, Double amount) {
        AccountEntity accountEntity = this.accountRepository.findById(accountNumber).get();
        if(accountEntity.getBalance() < 0){
            throw new InvalidParameterException("Insufficient funds, account already over drafted");
        }
        BigDecimal newBalance = BigDecimal.valueOf(accountEntity.getBalance() - amount).setScale(2, RoundingMode.HALF_UP);
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

    public AccountEntity setNickname(Long accountNumber, String nickname) {
        AccountEntity account = findByAccountNumber(accountNumber);
        account.setNickname(nickname);
        return this.accountRepository.save(account);
    }
}
