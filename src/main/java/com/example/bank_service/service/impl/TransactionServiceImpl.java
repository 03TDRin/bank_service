package com.example.bank_service.service.impl;

import com.example.bank_service.dto.transaction.TransactionResponseDTO;
import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.Transaction;
import com.example.bank_service.enums.TransactionType;
import com.example.bank_service.repository.AccountRepository;
import com.example.bank_service.repository.TransactionRepository;
import com.example.bank_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    public void recordTransaction(Account account, Double amount, TransactionType type, String desc) {
        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setAmount(amount);
        tx.setType(type);
        tx.setDescription(desc);
        transactionRepository.save(tx);
    }

    @Override
    public List<TransactionResponseDTO> getHistory(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        return transactionRepository.findByAccountOrderByTransactionDateDesc(account)
                .stream()
                .map(tx -> {
                    TransactionResponseDTO dto = new TransactionResponseDTO();
                    dto.setTransactionId(tx.getTransactionId());
                    dto.setType(tx.getType());
                    dto.setAmount(tx.getAmount());
                    dto.setDescription(tx.getDescription());
                    dto.setTransactionDate(tx.getTransactionDate());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}