package com.example.bank_service.dto.account;

import com.example.bank_service.enums.AccountStatus;
import lombok.Data;

//Tìm kiếm tk
@Data
public class AccountSearchDTO {
    private String accountNumber;
    private AccountStatus status;
    private String customerName;
}
