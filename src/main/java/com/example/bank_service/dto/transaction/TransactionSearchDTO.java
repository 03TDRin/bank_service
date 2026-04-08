package com.example.bank_service.dto.transaction;

import com.example.bank_service.enums.TransactionType;
import java.time.LocalDateTime;
import lombok.Data;

//Sao kê
@Data
public class TransactionSearchDTO {
    private TransactionType type;
    private Double minAmount;
    private Double maxAmount;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}
