package com.example.bank_service.service;

import com.example.bank_service.dto.transaction.*;
import com.example.bank_service.entity.Account;
import com.example.bank_service.enums.TransactionType;
import java.util.List;

public interface TransactionService {
    //Lấy ls giao dịch của 1 stk
    List<TransactionResponseDTO> getHistory(String accountNumber);

    TransactionResponseDTO transferMoney(TransactionTransferDTO dto);
    List<TransactionResponseDTO> searchTransactions(TransactionSearchDTO dto);

    // Hàm này cho người dùng dùng (có check security)
    //void transfer(TransferRequest request);

    // Hàm này cho con bot dùng (không cần SecurityContext)
    void transfer(Account source, Account target, Double amount);

    // Thêm String receiverNumber vào cuối
    void recordTransaction(Account account, Double amount, TransactionType type, String desc, String receiverNumber);
}