package com.example.bank_service.dto.account;

import jakarta.validation.constraints.Min;
import lombok.Data;

//Cập nhật hạn mức chi tiêu
@Data
public class AccountUpdateLimitDTO {
    @Min(value = 0)
    private Double dailyLimit;
}
