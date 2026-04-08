package com.example.bank_service.dto.transaction;

import lombok.Data;

@Data
public class TransactionCreateDTO {
    private String accountNumber;
    private String receiverAccountNumber;
    private Double amount;
    private String location;
    private String description;

}
