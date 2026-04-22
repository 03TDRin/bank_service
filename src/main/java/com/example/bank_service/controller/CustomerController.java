package com.example.bank_service.controller;

import com.example.bank_service.dto.customer.CustomerResponseDTO;
import com.example.bank_service.dto.customer.CustomerUpdateDTO;
import com.example.bank_service.dto.customer.CustomersSummaryDTO;
import com.example.bank_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    //Lấy thông tin chi tiết hồ sơ qua Public ID
    @GetMapping("/profile/{publicId}")
    public ResponseEntity<CustomerResponseDTO> getProfile(@PathVariable String publicId) {
        return ResponseEntity.ok(customerService.getProfile(publicId));
    }

    //Cập nhật thông tin KH
    @PutMapping("/update/{publicId}")
    public ResponseEntity<CustomerResponseDTO> updateProfile(
            @PathVariable String publicId,
            @RequestBody CustomerUpdateDTO updateDTO) {
        return ResponseEntity.ok(customerService.updateProfile(publicId, updateDTO));
    }

    //Lấy danh sách tất cả KH (Admin)
    @GetMapping("/all")
    public ResponseEntity<List<CustomersSummaryDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }
}