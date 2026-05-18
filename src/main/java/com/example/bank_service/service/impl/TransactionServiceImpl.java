package com.example.bank_service.service.impl;

import com.example.bank_service.dto.transaction.*;
import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.Transaction;
import com.example.bank_service.enums.TransactionType;
import com.example.bank_service.repository.AccountRepository;
import com.example.bank_service.repository.TransactionRepository;
import com.example.bank_service.service.TransactionService;
import com.example.bank_service.service.AlertService;
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
    private final AlertService alertService;

    @Override
    @Transactional
    public void recordTransaction(Account account, Double amount, TransactionType type, String desc, String receiverNumber) {
        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setAmount(amount);
        tx.setType(type);
        tx.setDescription(desc);
        tx.setReceiverNumber(receiverNumber);
        tx.setTransactionDate(LocalDateTime.now());

        transactionRepository.save(tx);
        alertService.sendNotification(account, desc);
    }

    @Override
    @Transactional
    public TransactionResponseDTO transferMoney(TransactionTransferDTO dto) {
        Account fromAcc = accountRepository.findByAccountNumber(dto.getFromAccountNumber())
                .orElseThrow(() -> new RuntimeException("Tài khoản gửi không tồn tại!"));
        Account toAcc = accountRepository.findByAccountNumber(dto.getToAccountNumber())
                .orElseThrow(() -> new RuntimeException("Tài khoản nhận không tồn tại!"));

        if (fromAcc.getBalance() < dto.getAmount()) {
            throw new RuntimeException("Số dư không đủ để thực hiện giao dịch!");
        }

        fromAcc.setBalance(fromAcc.getBalance() - dto.getAmount());
        toAcc.setBalance(toAcc.getBalance() + dto.getAmount());

        accountRepository.save(fromAcc);
        accountRepository.save(toAcc);

        String msgFrom = String.format("TK %s: -%,.0fđ. GD: Chuyển tiền đến %s. ND: %s. Số dư: %,.0fđ",
                fromAcc.getAccountNumber(), dto.getAmount(), toAcc.getAccountNumber(),
                dto.getDescription(), fromAcc.getBalance());
        recordTransaction(fromAcc, dto.getAmount(), TransactionType.TRANSFER, msgFrom, toAcc.getAccountNumber());

        String msgTo = String.format("TK %s: +%,.0fđ. GD: Nhận tiền từ %s. ND: %s. Số dư: %,.0fđ",
                toAcc.getAccountNumber(), dto.getAmount(), fromAcc.getAccountNumber(),
                dto.getDescription(), toAcc.getBalance());
        recordTransaction(toAcc, dto.getAmount(), TransactionType.DEPOSIT, msgTo, fromAcc.getAccountNumber());

        log.info("Giao dịch thành công: {} -> {}", dto.getFromAccountNumber(), dto.getToAccountNumber());

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

    @Override
    @Transactional
    public void transfer(Account source, Account target, Double amount) {
        if (source.getBalance() < amount) {
            log.error("Giao dịch thất bại: Số dư tài khoản {} không đủ", source.getAccountNumber());
            throw new RuntimeException("Số dư không đủ cho lệnh thanh toán định kỳ!");
        }

        source.setBalance(source.getBalance() - amount);
        target.setBalance(target.getBalance() + amount);

        accountRepository.save(source);
        accountRepository.save(target);

        String msgFrom = String.format("Thanh toán định kỳ đến %s. Số tiền: %,.0fđ", target.getAccountNumber(), amount);
        recordTransaction(source, amount, TransactionType.TRANSFER, msgFrom, target.getAccountNumber());

        String msgTo = String.format("Nhận thanh toán định kỳ từ %s. Số tiền: %,.0fđ", source.getAccountNumber(), amount);
        recordTransaction(target, amount, TransactionType.DEPOSIT, msgTo, source.getAccountNumber());

        log.info("Bot đã thực hiện chuyển tiền thành công: {} -> {}", source.getAccountNumber(), target.getAccountNumber());
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
        dto.setReceiverNumber(tx.getReceiverNumber()); // Đừng quên map cái này ra DTO nhé bà!

        return dto;
    }
}