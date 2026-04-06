package com.example.bank_service.service.impl;

import com.example.bank_service.dto.account.AccountCreateDTO;
import com.example.bank_service.dto.account.AccountResponseDTO;
import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.Customer;
import com.example.bank_service.enums.TransactionType;
import com.example.bank_service.repository.AccountRepository;
import com.example.bank_service.repository.CustomerRepository;
import com.example.bank_service.service.AccountService;
import com.example.bank_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
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
    private final TransactionService transactionService; // Inject thêm để lưu lịch sử

    @Override
    @Transactional
    public AccountResponseDTO createAccount(AccountCreateDTO dto) {
        Customer customer = customerRepository.findByPublicId(dto.getCustomerPublicId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng!"));

        Account account = new Account();
        account.setAccountNumber(UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        account.setBalance(dto.getInitialBalance());
        account.setCustomer(customer);

        Account saved = accountRepository.save(account);

        if (dto.getInitialBalance() > 0) {
            transactionService.recordTransaction(saved, dto.getInitialBalance(),
                    TransactionType.DEPOSIT, "Số dư khởi tạo tài khoản");
        }

        return mapToResponseDTO(saved);
    }

    @Override
    @Transactional
    public AccountResponseDTO deposit(String accountNumber, Double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        account.setBalance(account.getBalance() + amount);
        Account saved = accountRepository.save(account);

        //Lưu lịch sử nạp tiền
        transactionService.recordTransaction(saved, amount,
                TransactionType.DEPOSIT, "Nạp tiền vào tài khoản tại quầy/ATM");

        return mapToResponseDTO(saved);
    }

    @Override
    @Transactional
    public void transfer(String fromAccountNo, String toAccountNo, Double amount) {
        Account from = accountRepository.findByAccountNumber(fromAccountNo)
                .orElseThrow(() -> new RuntimeException("Tài khoản gửi không tồn tại!"));
        Account to = accountRepository.findByAccountNumber(toAccountNo)
                .orElseThrow(() -> new RuntimeException("Tài khoản nhận không tồn tại!"));

        if (from.getBalance() < amount) {
            throw new RuntimeException("Số dư không đủ để thực hiện chuyển khoản!");
        }

        //Cập nhật số dư
        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        accountRepository.save(from);
        accountRepository.save(to);

        //Lưu lsử cho người gửi
        transactionService.recordTransaction(from, amount,
                TransactionType.TRANSFER, "Chuyển tiền đến số tài khoản: " + toAccountNo);

        //Lưu lsử cho người nhận
        transactionService.recordTransaction(to, amount,
                TransactionType.DEPOSIT, "Nhận tiền từ số tài khoản: " + fromAccountNo);
    }

    @Override
    public List<AccountResponseDTO> getAccountsByCustomer(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email khách hàng không tồn tại!"));

        return customer.getAccounts().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private AccountResponseDTO mapToResponseDTO(Account account) {
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setAccountNumber(account.getAccountNumber());
        dto.setBalance(account.getBalance());
        dto.setCustomerFullName(account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName());
        return dto;
    }
}