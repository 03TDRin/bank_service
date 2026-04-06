package com.example.bank_service.repository;

import com.example.bank_service.entity.AccountStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountStatusHistoryRepository extends JpaRepository<AccountStatusHistory, Long> {
    List<AccountStatusHistory> findAllByAccountIdOrderByChangedAtDesc(Long accountId);
}