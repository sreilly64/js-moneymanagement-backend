package com.jszipcoders.moneymanager.services;

import com.jszipcoders.moneymanager.entities.AccountEntity;
import com.jszipcoders.moneymanager.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        accountRepository.findById(user_id).get();
        List<AccountEntity> listOfAccounts = this.accountRepository.findAll();
        return listOfAccounts.stream().filter(a -> a.getUserId() == user_id).collect(Collectors.toList());
    }

    public Double updateBalance(Long account_number, Double amount){
        AccountEntity accountEntity = this.accountRepository.findById(account_number).get();
        accountEntity.setBalance(amount + accountEntity.getBalance());
        return accountRepository.save(accountEntity).getBalance();
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
}
