package com.example.bank_service.service.impl;

import com.example.bank_service.dto.accountstatushistory
        .AccountStatusHistoryResponseDTO;
import com.example.bank_service.dto.accountstatushistory
        .AccountStatusHistorySearchDTO;
import com.example.bank_service.dto.accountstatushistory
        .AccountStatusHistoryUserSearchDTO;
import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.AccountStatusHistory;
import com.example.bank_service.enums.AccountStatus;
import com.example.bank_service.repository
        .AccountStatusHistoryRepository;
import com.example.bank_service.service
        .AccountStatusHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountStatusHistoryServiceImpl
        implements AccountStatusHistoryService {

    private final AccountStatusHistoryRepository repository;

    //Admin search ls trạng thái tk
    @Override
    public List<AccountStatusHistoryResponseDTO>
    search(AccountStatusHistorySearchDTO request) {

        List<AccountStatusHistory> histories =
                repository
                        .findByAccount_AccountNumberAndStatusAndChangedAtBetween(
                                request.getAccountNumber(),
                                request.getAccountStatus(),
                                request.getStart(),
                                request.getEnd()
                        );

        return histories.stream()
                .map(this::mapToDTO)
                .toList();
    }

    //User search ls trạng thái
    @Override
    public List<AccountStatusHistoryResponseDTO>
    userSearch(AccountStatusHistoryUserSearchDTO request) {

        List<AccountStatusHistory> histories =
                repository
                        .findByStatusAndChangedAtBetween(
                                request.getStatus(),
                                request.getStart(),
                                request.getEnd()
                        );

        return histories.stream()
                .map(this::mapToDTO)
                .toList();
    }

    //Lưu ls khi thay đổi trạng thái tk
    @Override
    public void logStatusChange(
            Account account,
            AccountStatus status,
            String reason
    ) {

        AccountStatusHistory history =
                new AccountStatusHistory();

        history.setAccount(account);

        history.setStatus(status);

        history.setReason(reason);

        repository.save(history);
    }

    //Lấy toàn bộ ls trạng thái tk
    @Override
    public List<AccountStatusHistoryResponseDTO>
    getAll() {

        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    //Lấy ls theo account number
    @Override
    public List<AccountStatusHistoryResponseDTO>
    getByAccountNumber(String accountNumber) {

        List<AccountStatusHistory> histories =
                repository
                        .findByAccount_AccountNumberOrderByChangedAtDesc(
                                accountNumber
                        );

        return histories.stream()
                .map(this::mapToDTO)
                .toList();
    }

    //Map Entity -> ResponseDTO
    private AccountStatusHistoryResponseDTO mapToDTO(
            AccountStatusHistory history
    ) {

        return AccountStatusHistoryResponseDTO
                .builder()
                .accountStatus(history.getStatus())
                .accountNumber(
                        history.getAccount().getAccountNumber()
                )
                .dateTime(history.getChangedAt())
                .build();
    }

}