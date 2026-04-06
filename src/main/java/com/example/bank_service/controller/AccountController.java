package com.example.bank_service.controller;

import com.example.bank_service.dto.account.AccountCreateDTO;
import com.example.bank_service.dto.account.AccountResponseDTO;
import com.example.bank_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<AccountResponseDTO> create(@RequestBody AccountCreateDTO dto) {
        return ResponseEntity.ok(accountService.createAccount(dto));
    }

    @PostMapping("/deposit")
    public ResponseEntity<AccountResponseDTO> deposit(@RequestParam String accountNumber, @RequestParam Double amount) {
        return ResponseEntity.ok(accountService.deposit(accountNumber, amount));
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam String from, @RequestParam String to, @RequestParam Double amount) {
        accountService.transfer(from, to, amount);
        return ResponseEntity.ok("Giao dịch thành công!");
    }

    @GetMapping("/my-accounts")
    public ResponseEntity<List<AccountResponseDTO>> getMyAccounts(@RequestParam String email) {
        return ResponseEntity.ok(accountService.getAccountsByCustomer(email));
    }
}