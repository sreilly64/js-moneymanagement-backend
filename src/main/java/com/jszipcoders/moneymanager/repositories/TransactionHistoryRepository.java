package com.jszipcoders.moneymanager.repositories;

import com.jszipcoders.moneymanager.repositories.entities.TransactionHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistoryEntity, Long> {
    @Query(value = "SELECT * FROM Transaction_History_Entity WHERE Primary_Account = :accountNumber OR Secondary_Account = :accountNumber", nativeQuery = true)
    List<TransactionHistoryEntity> findAllByAccountNumber(@Param("accountNumber") Long accountNumber);
}
