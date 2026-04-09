package com.example.bank_service.controller;

import com.example.bank_service.dto.account.*;
import com.example.bank_service.service.AccountService;
import com.example.bank_service.service.AccountStatusHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountStatusHistoryService statusHistoryService;

    //Tạo tk mới
    @PostMapping("/create")
    public ResponseEntity<AccountResponseDTO> create(@Valid @RequestBody AccountCreateDTO dto) {
        return ResponseEntity.ok(accountService.createAccount(dto));
    }

    //Nạp tiền vào tk
    @PostMapping("/deposit")
    public ResponseEntity<AccountResponseDTO> deposit(@RequestParam String accountNumber, @RequestParam Double amount) {
        return ResponseEntity.ok(accountService.deposit(accountNumber, amount));
    }

    //Chuyển khoản giữa các tk
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam String from, @RequestParam String to, @RequestParam Double amount) {
        accountService.transfer(from, to, amount);
        return ResponseEntity.ok("Giao dịch thành công!");
    }

    //Lấy thông tin người nhận trước khi chuyển tiền
    @GetMapping("/receiver-info")
    public ResponseEntity<ReceiverDTO> getReceiverInfo(@RequestParam String accountNumber) {
        return ResponseEntity.ok(accountService.getReceiverInfo(accountNumber));
    }

    //KH xem danh sách tk của mình
    @GetMapping("/my-accounts")
    public ResponseEntity<List<AccountResponseDTO>> getMyAccounts(@RequestParam String email) {
        return ResponseEntity.ok(accountService.getAccountsByCustomer(email));
    }

    //Admin cập nhật trạng thái (Khóa/Mở tk)
    @PutMapping("/{accountNumber}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable String accountNumber,
            @Valid @RequestBody AccountUpdateStatusDTO dto) {
        accountService.updateStatus(accountNumber, dto);
        return ResponseEntity.ok("Cập nhật trạng thái thành công!");
    }

    //Admin cập nhật hạn mức chi tiêu
    @PutMapping("/{accountNumber}/limit")
    public ResponseEntity<String> updateLimit(
            @PathVariable String accountNumber,
            @Valid @RequestBody AccountUpdateLimitDTO dto) {
        accountService.updateLimit(accountNumber, dto);
        return ResponseEntity.ok("Cập nhật hạn mức thành công!");
    }

    //Admin tra cứu tk kèm in4 người dùng
    @GetMapping("/search")
    public ResponseEntity<List<AccountUserSearchDTO>> searchAccounts(AccountSearchDTO dto) {
        return ResponseEntity.ok(accountService.searchAccounts(dto));
    }

    @GetMapping("/{accountNumber}/status-history")
    public ResponseEntity<List<AccountStatusHistoryDTO>> getStatusHistory(
            @PathVariable String accountNumber) {
        return ResponseEntity.ok(statusHistoryService.getByAccountNumber(accountNumber));
    }

}