package com.example.bank_service.dto.account;

import lombok.Data;
import java.util.UUID;

@Data
public class AccountResponseDTO {
    private String accountNumber;
    private Double balance;
    private String customerFullName;
}