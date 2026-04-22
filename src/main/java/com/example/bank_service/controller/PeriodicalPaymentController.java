package com.example.bank_service.controller;

import com.example.bank_service.dto.period.*;
import com.example.bank_service.service.PeriodicalPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/periodical-payments")
@RequiredArgsConstructor
public class PeriodicalPaymentController {

    private final PeriodicalPaymentService periodicalPaymentService;

    @PostMapping
    public ResponseEntity<PeriodicalPaymentResponse> create(@RequestBody PeriodicalPaymentRequestDTO request) {
        return ResponseEntity.ok(periodicalPaymentService.createPayment(request));
    }

    @GetMapping("/my-payments")
    public ResponseEntity<List<PeriodicalPaymentResponse>> getMyPayments() {
        Long customerId = 1L;
        return ResponseEntity.ok(periodicalPaymentService.getMyPayments(customerId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PeriodicalPaymentResponse>> search(PeriodicalPaymentSearchDTO dto) {
        return ResponseEntity.ok(periodicalPaymentService.searchPayments(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeriodicalPaymentResponse> update(@PathVariable Long id, @RequestBody PeriodicalPaymentUpdateDTO dto) {
        return ResponseEntity.ok(periodicalPaymentService.updatePayment(id, dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PeriodicalPaymentResponse> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(periodicalPaymentService.updateStatus(id, status));
    }
}