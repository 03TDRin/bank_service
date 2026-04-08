package com.example.bank_service.dto.account;

import com.example.bank_service.enums.AccountStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

//Khi admin muốn khóa/mở tk -> gửi trạng thái mới và lí do
@Data
public class AccountUpdateStatusDTO {
    @NotNull
    private AccountStatus status;
    private String reason;
}
