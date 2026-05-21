package com.example.bank_service.service;

import com.example.bank_service.dto.accountstatushistory.AccountStatusHistoryResponseDTO;
import com.example.bank_service.dto.accountstatushistory.AccountStatusHistorySearchDTO;
import com.example.bank_service.dto.accountstatushistory.AccountStatusHistoryUserSearchDTO;
import com.example.bank_service.entity.Account;
import com.example.bank_service.enums.AccountStatus;

import java.util.List;

public interface AccountStatusHistoryService {

    //Admin search
    List<AccountStatusHistoryResponseDTO>
    search(AccountStatusHistorySearchDTO request);

    //User search
    List<AccountStatusHistoryResponseDTO>
    userSearch(AccountStatusHistoryUserSearchDTO request);

    //save
    void logStatusChange(
            Account account,
            AccountStatus status,
            String reason
    );

    List<AccountStatusHistoryResponseDTO>
    getAll();

    List<AccountStatusHistoryResponseDTO>
    getByAccountNumber(String accountNumber);
}