package com.example.bank_service.repository;

import com.example.bank_service.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    // Tìm thông báo của một tài khoản, sắp xếp cái mới nhất lên đầu
    List<Alert> findByAccount_AccountNumberOrderByCreateAtDesc(String accountNumber);

    // Đếm số thông báo chưa đọc
    long countByAccount_AccountNumberAndIsReadFalse(String accountNumber);

}