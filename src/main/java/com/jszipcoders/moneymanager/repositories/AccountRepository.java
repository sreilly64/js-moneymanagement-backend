package com.jszipcoders.moneymanager.repositories;

import com.jszipcoders.moneymanager.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

//    AccountEntity findByAccountNumber(Long accountNumber);
}
