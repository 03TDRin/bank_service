package com.example.bank_service.service.impl;

import com.example.bank_service.dto.account.AccountStatusHistoryDTO;
import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.AccountStatusHistory;
import com.example.bank_service.enums.AccountStatus;
import com.example.bank_service.repository.AccountStatusHistoryRepository;
import com.example.bank_service.service.AccountStatusHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<AccountStatusHistoryDTO> getByAccountNumber(String accountNumber) {
        return repository.findByAccount_AccountNumber(accountNumber)
                .stream()
                .map(h -> new AccountStatusHistoryDTO(
                        h.getStatus().name(),
                        h.getReason(),
                        h.getCreatedAt()
                ))
                .toList();
    }
}