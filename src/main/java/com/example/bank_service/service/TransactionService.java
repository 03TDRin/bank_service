package com.example.bank_service.service;

import com.example.bank_service.dto.transaction.*;
import com.example.bank_service.entity.Account;
import com.example.bank_service.enums.TransactionType;
import java.util.List;

public interface TransactionService {
    //Các Service khác gọi vào để lưu ls
    void recordTransaction(Account account, Double amount, TransactionType type, String desc);
    //Lấy ls giao dịch của 1 số tài khoản
    List<TransactionResponseDTO> getHistory(String accountNumber);

    TransactionResponseDTO transferMoney(TransactionTransferDTO dto);
    List<TransactionResponseDTO> searchTransactions(TransactionSearchDTO dto);

}