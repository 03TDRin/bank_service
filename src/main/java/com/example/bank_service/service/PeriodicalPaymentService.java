package com.example.bank_service.service;

import com.example.bank_service.dto.period.PeriodicalPaymentRequest;
import com.example.bank_service.dto.period.PeriodicalPaymentResponse;
import java.util.List;

public interface PeriodicalPaymentService {
    PeriodicalPaymentResponse createPayment(PeriodicalPaymentRequest request);
    PeriodicalPaymentResponse updateStatus(Long id, String status);

    List<PeriodicalPaymentResponse> getMyPayments(Long customerId);

    //được gọi bởi Schedule
    void excuteScheduledPayment();

}
