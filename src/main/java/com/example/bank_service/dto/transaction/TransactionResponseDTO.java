package com.example.bank_service.dto.transaction;

import com.example.bank_service.enums.TransactionType;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionResponseDTO {
    private UUID transactionId;
    private TransactionType type;
    private Double amount;
    private String description;
    private LocalDateTime transactionDate;
}