package com.example.bank_service.mapper;

import com.example.bank_service.dto.period.PeriodicalPaymentResponse;
import com.example.bank_service.entity.PeriodicalPayment;
import org.springframework.stereotype.Component;

@Component
public class PeriodicalPaymentMapper {
    public PeriodicalPaymentResponse toResponse(PeriodicalPayment p) {
        if (p == null) return null;
        return PeriodicalPaymentResponse.builder()
                .id(p.getId())
                .sourceAccountId(p.getSourceAccount().getId())
                .targetAccountId(p.getTargetAccount().getId())
                .amount(p.getAmount())
                .period(p.getPeriod())
                .status(p.getStatus())
                .lastProcessedDate(p.getLastProcessedDate())
                .build();
    }
}
