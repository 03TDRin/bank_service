package com.example.bank_service.dto.period;

import com.example.bank_service.enums.PaymentPeriod;
import lombok.Data;

@Data
public class PeriodicalPaymentRequestDTO {
    private Long sourceAccountId;
    private Long targetAccountId;
    private Double amount;
    private PaymentPeriod period;
}
