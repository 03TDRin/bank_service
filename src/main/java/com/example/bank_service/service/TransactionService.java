package com.example.bank_service.service;

import com.example.bank_service.dto.transaction.TransactionResponseDTO;
import com.example.bank_service.entity.Account;
import com.example.bank_service.enums.TransactionType;

import java.util.List;

public interface TransactionService {
    // Hàm này để các Service khác gọi vào để lưu lịch sử
    void recordTransaction(Account account, Double amount, TransactionType type, String desc);

    // Lấy lịch sử giao dịch của 1 số tài khoản
    List<TransactionResponseDTO> getHistory(String accountNumber);
}