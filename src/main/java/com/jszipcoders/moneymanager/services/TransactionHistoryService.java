package com.jszipcoders.moneymanager.services;

import com.jszipcoders.moneymanager.repositories.TransactionHistoryRepository;
import com.jszipcoders.moneymanager.repositories.entities.TransactionHistoryEntity;
import com.jszipcoders.moneymanager.repositories.entities.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionHistoryService {

    private final TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    public TransactionHistoryService(TransactionHistoryRepository transactionHistoryRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    public TransactionHistoryEntity save(Long primaryAccount, Long secondaryAccount, Double amount, TransactionType type){
        TransactionHistoryEntity transaction = new TransactionHistoryEntity(primaryAccount, secondaryAccount, LocalDateTime.now(), amount, type);
        return this.transactionHistoryRepository.save(transaction);
    }

    public List<TransactionHistoryEntity> getTransactions(Long accountNumber) {
        return this.transactionHistoryRepository.findAllByAccountNumber(accountNumber);
    }
}
