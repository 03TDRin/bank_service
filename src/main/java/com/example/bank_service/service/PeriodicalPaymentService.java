package com.example.bank_service.service;

import com.example.bank_service.dto.period.PeriodicalPaymentRequestDTO;
import com.example.bank_service.dto.period.PeriodicalPaymentResponse;
import java.util.List;

public interface PeriodicalPaymentService {
    PeriodicalPaymentResponse createPayment(PeriodicalPaymentRequestDTO request);
    PeriodicalPaymentResponse updateStatus(Long id, String status);

    List<PeriodicalPaymentResponse> getMyPayments(Long customerId);

    void executeScheduledPayments();
}