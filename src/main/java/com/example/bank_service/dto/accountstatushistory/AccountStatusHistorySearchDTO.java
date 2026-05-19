package com.example.bank_service.dto.accountstatushistory;
import com.example.bank_service.entity.Account;
import com.example.bank_service.enums.AccountStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AccountStatusHistorySearchDTO {
    private String accountNumber;
    private AccountStatus accountStatus;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime start;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime end;
}
