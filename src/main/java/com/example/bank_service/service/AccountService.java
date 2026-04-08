package com.example.bank_service.service;

import com.example.bank_service.dto.account.AccountCreateDTO;
import com.example.bank_service.dto.account.AccountResponseDTO;
import com.example.bank_service.dto.account.*;
import java.util.List;

public interface AccountService {
    AccountResponseDTO createAccount(AccountCreateDTO dto);
    AccountResponseDTO deposit(String accountNumber, Double amount);
    void transfer(String fromAccount, String toAccount, Double amount);
    List<AccountResponseDTO> getAccountsByCustomer(String email);

    void updateStatus(String accountNumber, AccountUpdateStatusDTO dto);
    void updateLimit(String accountNumber, AccountUpdateLimitDTO dto);
    ReceiverDTO getReceiverInfo(String accountNumber);
    List<AccountUserSearchDTO> searchAccounts(AccountSearchDTO dto);
}