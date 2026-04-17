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
    public void recordTransaction(Account account, Double amount, TransactionType type, String desc) {
        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setAmount(amount);
        tx.setType(type);
        tx.setDescription(desc);
        transactionRepository.save(tx);

        //gửi tbao có nd được soạn ở transfer money
        alertService.sendNotification(account, desc);
    }

    @Override
    @Transactional
    public TransactionResponseDTO transferMoney(TransactionTransferDTO dto) {
        //Tìm tk
        Account fromAcc = accountRepository.findByAccountNumber(dto.getFromAccountNumber())
                .orElseThrow(() -> new RuntimeException("Tài khoản gửi không tồn tại!"));
        Account toAcc = accountRepository.findByAccountNumber(dto.getToAccountNumber())
                .orElseThrow(() -> new RuntimeException("Tài khoản nhận không tồn tại!"));

        //Ktra số dư
        if (fromAcc.getBalance() < dto.getAmount()) {
            throw new RuntimeException("Số dư không đủ để thực hiện giao dịch!");
        }

        //Trừ tiền bên gửi, cộng tiền bên nhận
        fromAcc.setBalance(fromAcc.getBalance() - dto.getAmount());
        toAcc.setBalance(toAcc.getBalance() + dto.getAmount());

        accountRepository.save(fromAcc);
        accountRepository.save(toAcc);

        //Soạn nd thông báo cho người gửi
        String msgFrom = String.format("TK %s: -%,.0fđ. GD: Chuyển tiền đến %s. ND: %s. Số dư: %,.0fđ",
                fromAcc.getAccountNumber(), dto.getAmount(), toAcc.getAccountNumber(),
                dto.getDescription(), fromAcc.getBalance());

        recordTransaction(fromAcc, dto.getAmount(), TransactionType.TRANSFER, msgFrom);

        //Soạn cho người nhận
        String msgTo = String.format("TK %s: +%,.0fđ. GD: Nhận tiền từ %s. ND: %s. Số dư: %,.0fđ",
                toAcc.getAccountNumber(), dto.getAmount(), fromAcc.getAccountNumber(),
                dto.getDescription(), toAcc.getBalance());

        recordTransaction(toAcc, dto.getAmount(), TransactionType.DEPOSIT, msgTo);

        log.info("Giao dịch thành công: {} -> {}", dto.getFromAccountNumber(), dto.getToAccountNumber());

        //Trả về kq giao dịch mới nhất của người gửi
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

    private void performTransfer(Account fromAcc, Account toAcc, Double amount, String description){
        fromAcc.setBalance(fromAcc.getBalance() - amount);
        toAcc.setBalance(toAcc.getBalance() + amount);

        accountRepository.save(fromAcc);
        accountRepository.save(toAcc);

        String msgFrom = String.format("Chuyển tiền đến %s. Số tiền: %,.0fđ", toAcc.getAccountNumber(), amount);
        recordTransaction(fromAcc, amount, TransactionType.TRANSFER, msgFrom);
        String msgTo = String.format("Nhận tiền từ %s. Số tiền: %,.0fđ", fromAcc.getAccountNumber(), amount);
        recordTransaction(toAcc, amount, TransactionType.DEPOSIT, msgTo);
    }
    @Override
    @Transactional
    public void transfer(Account source, Account target, Double amount) {
        if (source.getBalance() < amount) {
            log.error("Giao dịch thất bại: Số dư tài khoản {} không đủ", source.getAccountNumber());
            throw new RuntimeException("Số dư không đủ cho lệnh thanh toán định kỳ!");
        }

        performTransfer(source, target, amount, "Thanh toán định kỳ");
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

        return dto;
    }
}