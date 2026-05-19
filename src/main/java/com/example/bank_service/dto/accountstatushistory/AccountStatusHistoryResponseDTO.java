package com.example.bank_service.dto.accountstatushistory;
import com.example.bank_service.enums.AccountStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountStatusHistoryResponseDTO {
    private AccountStatus accountStatus;
    private String accountNumber;
    private LocalDateTime dateTime;
}
