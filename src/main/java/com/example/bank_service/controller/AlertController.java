package com.example.bank_service.controller;

import com.example.bank_service.dto.alert.AlertResponseDTO;
import com.example.bank_service.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AlertController {

    private final AlertService alertService;

    //Lấy dsach thông báo của 1 tk
    @GetMapping("/{accountNumber}")
    public ResponseEntity<List<AlertResponseDTO>> getMyAlerts(@PathVariable String accountNumber) {
        List<AlertResponseDTO> alerts = alertService.getMyAlerts(accountNumber);
        return ResponseEntity.ok(alerts);
    }

    //Đánh dấu một thông báo là đã đọc
    @PatchMapping("/{id}/read")
    public ResponseEntity<String> markAsRead(@PathVariable Long id) {
        alertService.markAsRead(id);
        return ResponseEntity.ok("Đã đánh dấu thông báo " + id + " là đã đọc!");
    }
}