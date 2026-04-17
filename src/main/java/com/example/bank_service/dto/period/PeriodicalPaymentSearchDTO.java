package com.example.bank_service.dto.period;

import com.example.bank_service.enums.PaymentPeriod;
import com.example.bank_service.enums.SubscriptionStatus;
import lombok.Data;

@Data
public class PeriodicalPaymentSearchDTO {
    private Double minAmount;
    private Double maxAmount;
    private PaymentPeriod period;
    private SubscriptionStatus status;
}
