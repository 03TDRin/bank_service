package com.example.bank_service.repository;

import com.example.bank_service.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);

    @Query("SELECT a FROM Account a " +
            "JOIN a.customer c " +
            "JOIN c.user u " +
            "WHERE (:accountNumber IS NULL OR a.accountNumber LIKE %:accountNumber%) " +
            "AND (:keyword IS NULL OR u.username LIKE %:keyword% OR c.phoneNumber LIKE %:keyword%)")
    Page<Account> searchAccounts(
            @Param("accountNumber") String accountNumber,
            @Param("keyword") String keyword,
            Pageable pageable);
}