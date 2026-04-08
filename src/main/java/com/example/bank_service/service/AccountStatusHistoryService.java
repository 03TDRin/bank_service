package com.example.bank_service.service;

import com.example.bank_service.entity.Account;
import com.example.bank_service.dto.account.AccountStatusHistoryDTO;
import com.example.bank_service.enums.AccountStatus;

import java.util.List;

public interface AccountStatusHistoryService {

    void logStatusChange(Account account, AccountStatus status, String reason);

    List<AccountStatusHistoryDTO> getByAccountNumber(String accountNumber);
}