package com.example.bank_service.service;

import com.example.bank_service.dto.account.AccountCreateDTO;
import com.example.bank_service.dto.account.AccountResponseDTO;
import java.util.List;

public interface AccountService {
    AccountResponseDTO createAccount(AccountCreateDTO dto);
    AccountResponseDTO deposit(String accountNumber, Double amount);
    void transfer(String fromAccount, String toAccount, Double amount);
    List<AccountResponseDTO> getAccountsByCustomer(String email);
}