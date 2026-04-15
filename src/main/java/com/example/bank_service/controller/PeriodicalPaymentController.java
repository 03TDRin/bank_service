package com.example.bank_service.controller;

import com.example.bank_service.dto.period.PeriodicalPaymentResponse;
import com.example.bank_service.dto.period.PeriodicalPaymentRequest;
import com.example.bank_service.service.PeriodicalPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/periodical-payments")
public class PeriodicalPaymentController {
    private final PeriodicalPaymentService periodicalPaymentService;
    private PeriodicalPaymentRequest request;

    //tạo mới lệnh thanh toán định kỳ
    @PostMapping
    public ResponseEntity<PeriodicalPaymentResponse> create(@RequestBody PeriodicalPaymentRequest periodicalPaymentRequest){
        return ResponseEntity.ok(periodicalPaymentService.createPayment(request));
    }
    //lấy ds thanh toán của user hiện tại
    @GetMapping("/my-payments")
    public ResponseEntity<List<PeriodicalPaymentResponse>> getMyPayments(){
        //lấy customerId từ SecurityContext của user đang login
        Long customerId = 1L; // Placeholder: Thay bằng ID lấy từ token/security
        return ResponseEntity.ok(periodicalPaymentService.getMyPayments(customerId));
    }
    
    //cập nhật status bằng inactive
    @PatchMapping("/{id}/status")
    public ResponseEntity<PeriodicalPaymentResponse> updateStatus(@PathVariable Long id, @RequestParam String status){
        return ResponseEntity.ok(periodicalPaymentService.updateStatus(id, status));
    }

}
