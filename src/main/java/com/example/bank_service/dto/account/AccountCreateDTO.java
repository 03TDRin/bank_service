package com.example.bank_service.dto.account;

import lombok.Data;
import java.util.UUID;

@Data
public class AccountCreateDTO {
    private Double initialBalance;
    private UUID customerPublicId;
}