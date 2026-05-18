package com.example.bank_service.repository;

import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    //Tìm tất cả các giao dịch của một tk rồi sx lên đầu
    List<Transaction> findByAccountOrderByTransactionDateDesc(Account account);

    //Lấy giao dịch vừa thực hiện xong
    Transaction findFirstByAccountOrderByTransactionDateDesc(Account account);

    Long countByTransactionDateBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    List<Transaction> findByTransactionDateBetween(
            LocalDateTime start,
            LocalDateTime end
    );
}