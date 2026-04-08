package com.example.bank_service.dto.account;

import com.example.bank_service.enums.AccountStatus;
import lombok.Data;


@Data
public class AccountUserSearchDTO {
    private String accountNumber;
    private String customerFullName;
    private String email;
    private Double balance;
    private AccountStatus status;
}
