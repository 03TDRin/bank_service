package com.example.bank_service.service;

import com.example.bank_service.dto.period.PeriodicalPaymentRequestDTO;
import com.example.bank_service.dto.period.PeriodicalPaymentResponse;
import com.example.bank_service.dto.period.PeriodicalPaymentSearchDTO;
import com.example.bank_service.dto.period.PeriodicalPaymentUpdateDTO;

import java.util.List;

public interface PeriodicalPaymentService {
    PeriodicalPaymentResponse createPayment(PeriodicalPaymentRequestDTO request);
    PeriodicalPaymentResponse updateStatus(Long id, String status);

    List<PeriodicalPaymentResponse> getMyPayments(Long customerId);

    void executeScheduledPayments();
    List<PeriodicalPaymentResponse> searchPayments(PeriodicalPaymentSearchDTO dto);
    PeriodicalPaymentResponse updatePayment(Long id, PeriodicalPaymentUpdateDTO dto);
}