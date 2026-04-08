package com.example.bank_service.controller;

import com.example.bank_service.dto.transaction.*;
import com.example.bank_service.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    //Lấy lsu giao dịch
    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<List<TransactionResponseDTO>> getHistory(@PathVariable String accountNumber) {
        return ResponseEntity.ok(transactionService.getHistory(accountNumber));
    }

    //Chuyển khoản
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transfer(@Valid @RequestBody TransactionTransferDTO dto) {
        return ResponseEntity.ok(transactionService.transferMoney(dto));
    }

    //Tìm kiếm và lọc giao dịch
    @PostMapping("/search")
    public ResponseEntity<List<TransactionResponseDTO>> search(@RequestBody TransactionSearchDTO dto) {
        return ResponseEntity.ok(transactionService.searchTransactions(dto));
    }
}