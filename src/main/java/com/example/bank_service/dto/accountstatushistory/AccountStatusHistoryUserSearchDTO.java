package com.example.bank_service.dto.accountstatushistory;
import com.example.bank_service.enums.AccountStatus;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AccountStatusHistoryUserSearchDTO {
    private AccountStatus status;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime start;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime end;
}
