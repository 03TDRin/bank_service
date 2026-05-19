package com.example.bank_service.controller;

import com.example.bank_service.dto.ApiResponse;
import com.example.bank_service.dto.accountstatushistory
        .AccountStatusHistoryResponseDTO;
import com.example.bank_service.dto.accountstatushistory
        .AccountStatusHistorySearchDTO;
import com.example.bank_service.dto.accountstatushistory
        .AccountStatusHistoryUserSearchDTO;
import com.example.bank_service.service
        .AccountStatusHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account-status-history")
@RequiredArgsConstructor
public class AccountStatusHistoryController {

    private final AccountStatusHistoryService service;

    //Lấy toàn bộ lịch sử trạng thái tài khoản
    @GetMapping
    @Operation(summary = "Get all account status history")
    public ResponseEntity<
            ApiResponse<List<AccountStatusHistoryResponseDTO>>
            > getAll() {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        service.getAll(),
                        "200",
                        "Get all account status history successfully"
                )
        );
    }

    //Lấy lịch sử theo account number
    @GetMapping("/{accountNumber}")
    @Operation(summary = "Get account status history by account number")
    public ResponseEntity<
            ApiResponse<List<AccountStatusHistoryResponseDTO>>
            > getByAccountNumber(

            @PathVariable
            String accountNumber
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        service.getByAccountNumber(accountNumber),
                        "200",
                        "Get account status history successfully"
                )
        );
    }

    //Admin tìm kiếm lịch sử trạng thái tài khoản
    @PostMapping("/search")
    @Operation(summary = "Search account status history")
    public ResponseEntity<
            ApiResponse<List<AccountStatusHistoryResponseDTO>>
            > search(

            @RequestBody
            AccountStatusHistorySearchDTO request
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        service.search(request),
                        "200",
                        "Search history successfully"
                )
        );
    }

    //User tìm kiếm lịch sử trạng thái
    @PostMapping("/user-search")
    @Operation(summary = "User search account status history")
    public ResponseEntity<
            ApiResponse<List<AccountStatusHistoryResponseDTO>>
            > userSearch(

            @RequestBody
            AccountStatusHistoryUserSearchDTO request
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        service.userSearch(request),
                        "200",
                        "User search history successfully"
                )
        );
    }
}