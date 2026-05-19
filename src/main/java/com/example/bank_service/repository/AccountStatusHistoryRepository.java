package com.example.bank_service.repository;

import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.AccountStatusHistory;
import com.example.bank_service.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AccountStatusHistoryRepository
        extends JpaRepository<AccountStatusHistory, Long> {

    //Lấy toàn bộ lịch sử trạng thái của 1 tài khoản
    List<AccountStatusHistory>
    findByAccountOrderByChangedAtDesc(
            Account account
    );

    //Admin tìm kiếm theo:
    //account number + status + khoảng thời gian
    List<AccountStatusHistory>
    findByAccount_AccountNumberAndStatusAndChangedAtBetween(
            String accountNumber,
            AccountStatus status,
            LocalDateTime start,
            LocalDateTime end
    );

    //User tìm kiếm theo:
    //status + khoảng thời gian
    List<AccountStatusHistory>
    findByStatusAndChangedAtBetween(
            AccountStatus status,
            LocalDateTime start,
            LocalDateTime end
    );

    //Lấy toàn bộ lịch sử theo account number
    List<AccountStatusHistory>
    findByAccount_AccountNumberOrderByChangedAtDesc(
            String accountNumber
    );

    //Lấy toàn bộ lịch sử theo status
    List<AccountStatusHistory>
    findByStatusOrderByChangedAtDesc(
            AccountStatus status
    );

    //Lấy lịch sử theo khoảng thời gian
    List<AccountStatusHistory>
    findByChangedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );
}