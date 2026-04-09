package com.example.bank_service.dto.alert;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AlertResponseDTO {
    private Long id;
    private String description;
    private boolean isRead;
    private LocalDateTime timestamp;
    private String accountNumber;
}