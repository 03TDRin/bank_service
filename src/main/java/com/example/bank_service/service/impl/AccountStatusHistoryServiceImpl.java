package com.example.bank_service.service.impl;

import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.AccountStatusHistory;
import com.example.bank_service.enums.AccountStatus;
import com.example.bank_service.repository.AccountStatusHistoryRepository;
import com.example.bank_service.service.AccountStatusHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountStatusHistoryServiceImpl implements AccountStatusHistoryService {

    private final AccountStatusHistoryRepository repository;

    @Override
    public void logStatusChange(Account account, AccountStatus status, String reason) {
        AccountStatusHistory history = new AccountStatusHistory();
        history.setAccount(account);
        history.setStatus(status);
        history.setReason(reason);
        repository.save(history);
    }
}