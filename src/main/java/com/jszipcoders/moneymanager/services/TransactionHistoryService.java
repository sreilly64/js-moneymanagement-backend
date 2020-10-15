package com.jszipcoders.moneymanager.services;

import com.jszipcoders.moneymanager.entities.TransactionHistoryEntity;
import com.jszipcoders.moneymanager.entities.TransactionType;
import com.jszipcoders.moneymanager.repositories.TransactionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionHistoryService {

    private TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    public TransactionHistoryService(TransactionHistoryRepository transactionHistoryRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    public TransactionHistoryEntity save(Long primaryAccount, Long secondaryAccount, Double amount, TransactionType type){
        TransactionHistoryEntity transaction = new TransactionHistoryEntity(primaryAccount, secondaryAccount, LocalDateTime.now(), amount, type);
        return this. transactionHistoryRepository.save(transaction);
    }

}
