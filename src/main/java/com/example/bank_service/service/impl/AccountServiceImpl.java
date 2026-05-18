package com.example.bank_service.service.impl;

import com.example.bank_service.service.AccountService;
import com.example.bank_service.dto.account.*;
import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.Customer;
import com.example.bank_service.enums.AccountStatus;
import com.example.bank_service.enums.TransactionType;
import com.example.bank_service.repository.AccountRepository;
import com.example.bank_service.repository.CustomerRepository;
import com.example.bank_service.service.AccountStatusHistoryService;
import com.example.bank_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionService transactionService;
    private final AccountStatusHistoryService statusHistoryService;

    @Override
    @Transactional
    public AccountResponseDTO createAccount(AccountCreateDTO dto) {
        Customer customer = customerRepository.findByPublicId(dto.getCustomerPublicId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng!"));

        Account account = new Account();
        account.setAccountNumber(UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        account.setBalance(dto.getInitialBalance());
        account.setCustomer(customer);
        account.setStatus(AccountStatus.ACTIVE);

        Account saved = accountRepository.save(account);
        statusHistoryService.logStatusChange(saved, AccountStatus.ACTIVE, "Tạo tài khoản mới");

        if (dto.getInitialBalance() > 0) {
            transactionService.recordTransaction(saved, dto.getInitialBalance(),
                    TransactionType.DEPOSIT, "Số dư khởi tạo tài khoản", null);
        }
        return mapToResponseDTO(saved);
    }

    @Override
    @Transactional
    public AccountResponseDTO deposit(String accountNumber, Double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Tài khoản không ở trạng thái hoạt động!");
        }

        account.setBalance(account.getBalance() + amount);
        Account saved = accountRepository.save(account);
        transactionService.recordTransaction(saved, amount, TransactionType.DEPOSIT, "Nạp tiền vào tài khoản", null);
        return mapToResponseDTO(saved);
    }

    @Override
    @Transactional
    public void transfer(String fromAccount, String toAccount, Double amount) {
        Account from = accountRepository.findByAccountNumber(fromAccount)
                .orElseThrow(() -> new RuntimeException("Tài khoản gửi không tồn tại!"));
        Account to = accountRepository.findByAccountNumber(toAccount)
                .orElseThrow(() -> new RuntimeException("Tài khoản nhận không tồn tại!"));

        if (from.getStatus() != AccountStatus.ACTIVE || to.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Tài khoản gửi hoặc nhận đang bị khóa!");
        }

        if (from.getBalance() < amount) {
            throw new RuntimeException("Số dư không đủ!");
        }

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        accountRepository.save(from);
        accountRepository.save(to);

        transactionService.recordTransaction(from, amount, TransactionType.TRANSFER,
                "Chuyển tiền tới " + toAccount, toAccount);
        transactionService.recordTransaction(to, amount, TransactionType.DEPOSIT,
                "Nhận tiền từ " + fromAccount, fromAccount);
    }

    @Override
    public List<AccountResponseDTO> getAccountsByCustomer(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại!"));

        return customer.getAccounts().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateStatus(String accountNumber, AccountUpdateStatusDTO dto) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        account.setStatus(dto.getStatus());
        accountRepository.save(account);
        statusHistoryService.logStatusChange(account, dto.getStatus(), dto.getReason());
    }

    @Override
    @Transactional
    public void updateLimit(String accountNumber, AccountUpdateLimitDTO dto) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));
        account.setDailyLimit(dto.getDailyLimit());
        accountRepository.save(account);
    }

    @Override
    public ReceiverDTO getReceiverInfo(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        ReceiverDTO dto = new ReceiverDTO();
        dto.setAccountNumber(account.getAccountNumber());
        dto.setFullName(account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName());
        return dto;
    }

    @Override
    public List<AccountUserSearchDTO> searchAccounts(AccountSearchDTO dto) {
        return accountRepository.searchAccounts(
                        dto.getAccountNumber(),
                        dto.getKeyword(),
                        Pageable.unpaged()
                ).getContent()
                .stream()
                .map(this::mapToUserSearchDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountUserSearchDTO> searchAccounts(String keyword) {
        return accountRepository.searchAccounts(
                        keyword,
                        keyword,
                        Pageable.unpaged()
                ).getContent()
                .stream()
                .map(this::mapToUserSearchDTO)
                .collect(Collectors.toList());
    }


    private AccountUserSearchDTO mapToUserSearchDTO(Account account) {
        AccountUserSearchDTO dto = new AccountUserSearchDTO();
        dto.setAccountNumber(account.getAccountNumber());
        dto.setCustomerFullName(account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName());
        dto.setEmail(account.getCustomer().getEmail());
        dto.setBalance(account.getBalance());
        dto.setStatus(account.getStatus());
        return dto;
    }

    private AccountResponseDTO mapToResponseDTO(Account account) {
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setAccountNumber(account.getAccountNumber());
        dto.setBalance(account.getBalance());
        dto.setCustomerFullName(account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName());
        return dto;
    }
}