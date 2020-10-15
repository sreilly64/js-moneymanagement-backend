package com.jszipcoders.moneymanager.repositories;

import com.jszipcoders.moneymanager.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
