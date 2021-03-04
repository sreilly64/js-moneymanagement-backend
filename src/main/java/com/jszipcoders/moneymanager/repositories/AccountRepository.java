package com.jszipcoders.moneymanager.repositories;

import com.jszipcoders.moneymanager.repositories.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    @Query(value = "SELECT * FROM ACCOUNTENTITY WHERE USERID = :userId", nativeQuery = true)
    List<AccountEntity> findAllByUserId(@Param("userId")Long userId);
}
