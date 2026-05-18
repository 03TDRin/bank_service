package com.example.bank_service.dto.account;

import com.example.bank_service.enums.AccountStatus;
import lombok.Data;

@Data
public class AccountSearchDTO {
    private String accountNumber;
    private String keyword;
//    private AccountStatus status;
//    private String customerName;
}
