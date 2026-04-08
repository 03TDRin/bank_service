package com.example.bank_service.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AccountStatusHistoryDTO {
    private String status;
    private String reason;
    private LocalDateTime createdAt;
}