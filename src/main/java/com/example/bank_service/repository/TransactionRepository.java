package com.example.bank_service.repository;

import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Tìm tất cả giao dịch của một tài khoản và sắp xếp cái mới nhất lên đầu
    List<Transaction> findByAccountOrderByTransactionDateDesc(Account account);
}