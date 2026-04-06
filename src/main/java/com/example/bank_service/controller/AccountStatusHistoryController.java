package com.example.bank_service.controller;

import com.example.bank_service.entity.AccountStatusHistory;
import com.example.bank_service.repository.AccountStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account-status")
@RequiredArgsConstructor
public class AccountStatusHistoryController {

    private final AccountStatusHistoryRepository repository;

    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<AccountStatusHistory>> getHistory(@PathVariable Long accountId) {
        // Tạm thời gọi trực tiếp Repository để xem nhanh
        return ResponseEntity.ok(repository.findAllByAccountIdOrderByChangedAtDesc(accountId));
    }
}