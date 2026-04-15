package com.example.bank_service.dto.period;

import com.example.bank_service.enums.SubscriptionStatus;
import  com.example.bank_service.enums.PaymentPeriod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeriodicalPaymentResponse {
    private Long id;
    private Long sourceAccountId;
    private Long targetAccountId;
    private Double amount;
    private PaymentPeriod period;
    private SubscriptionStatus status;
    private LocalDate lastProcessedDate;
}
