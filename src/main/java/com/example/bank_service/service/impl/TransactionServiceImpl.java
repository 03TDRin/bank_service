package com.example.bank_service.service.impl;

import com.example.bank_service.dto.transaction.*;
import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.Transaction;
import com.example.bank_service.enums.TransactionType;
import com.example.bank_service.repository.AccountRepository;
import com.example.bank_service.repository.TransactionRepository;
import com.example.bank_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void recordTransaction(Account account, Double amount, TransactionType type, String desc) {
        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setAmount(amount);
        tx.setType(type);
        tx.setDescription(desc);
        transactionRepository.save(tx);
    }

    @Override
    @Transactional
    public TransactionResponseDTO transferMoney(TransactionTransferDTO dto) {
        //Tìm tk
        Account fromAcc = accountRepository.findByAccountNumber(dto.getFromAccountNumber())
                .orElseThrow(() -> new RuntimeException("Tài khoản gửi không tồn tại!"));
        Account toAcc = accountRepository.findByAccountNumber(dto.getToAccountNumber())
                .orElseThrow(() -> new RuntimeException("Tài khoản nhận không tồn tại!"));

        //Check số dư
        if (fromAcc.getBalance() < dto.getAmount()) {
            throw new RuntimeException("Số dư không đủ để thực hiện giao dịch!");
        }

        //Trừ tiền bên gửi, cộng tiền bên nhận
        fromAcc.setBalance(fromAcc.getBalance() - dto.getAmount());
        toAcc.setBalance(toAcc.getBalance() + dto.getAmount());

        accountRepository.save(fromAcc);
        accountRepository.save(toAcc);

        //Lưu lsu cho cả 2
        recordTransaction(fromAcc, dto.getAmount(), TransactionType.TRANSFER,
                "Chuyển tiền đến " + dto.getToAccountNumber() + ": " + dto.getDescription());

        recordTransaction(toAcc, dto.getAmount(), TransactionType.DEPOSIT,
                "Nhận tiền từ " + dto.getFromAccountNumber() + ": " + dto.getDescription());

        log.info("Giao dịch chuyển khoản thành công từ {} sang {}", dto.getFromAccountNumber(), dto.getToAccountNumber());

        //Trả về kết quả
        return mapEntityToResponseDTO(transactionRepository.findFirstByAccountOrderByTransactionDateDesc(fromAcc));
    }

    @Override
    public List<TransactionResponseDTO> getHistory(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        return transactionRepository.findByAccountOrderByTransactionDateDesc(account)
                .stream()
                .map(this::mapEntityToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponseDTO> searchTransactions(TransactionSearchDTO dto) {
        return transactionRepository.findAll().stream()
                .filter(tx -> (dto.getType() == null || tx.getType() == dto.getType()))
                .filter(tx -> (dto.getMinAmount() == null || tx.getAmount() >= dto.getMinAmount()))
                .filter(tx -> (dto.getMaxAmount() == null || tx.getAmount() <= dto.getMaxAmount()))
                .map(this::mapEntityToResponseDTO)
                .collect(Collectors.toList());
    }

    private TransactionResponseDTO mapEntityToResponseDTO(Transaction tx) {
        if (tx == null) return null;

        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setTransactionId(tx.getTransactionId());
        dto.setAccountNumber(tx.getAccount().getAccountNumber());
        dto.setType(tx.getType());
        dto.setAmount(tx.getAmount());
        dto.setDescription(tx.getDescription());
        dto.setTransactionDate(tx.getTransactionDate());

        return dto;
    }

}