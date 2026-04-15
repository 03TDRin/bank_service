package com.example.bank_service.service.impl;

import com.example.bank_service.dto.period.PeriodicalPaymentRequest;
import com.example.bank_service.dto.period.PeriodicalPaymentResponse;
import com.example.bank_service.entity.PeriodicalPayment;
import com.example.bank_service.enums.PaymentPeriod;
import com.example.bank_service.enums.SubscriptionStatus;
import com.example.bank_service.repository.PeriodicalPaymentRepository;
import com.example.bank_service.service.PeriodicalPaymentService;
import com.example.bank_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeriodicalPaymentServiceImpl implements PeriodicalPaymentService {

    private final PeriodicalPaymentRepository repository;
    private final TransactionService transactionService;

    @Override
    public PeriodicalPaymentResponse createPayment(PeriodicalPaymentRequest request) {
        PeriodicalPayment payment = new PeriodicalPayment();
        payment.setAmount(request.getAmount());
        payment.setPeriod(request.getPeriod());
        payment.setStatus(SubscriptionStatus.ACTIVE);
        payment.setLastProcessedDate(LocalDate.now());

        repository.save(payment);
        return new PeriodicalPaymentResponse();
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void executeScheduledPayments() {
        log.info("Bắt đầu xử lý các thanh toán định kỳ...");
        List<PeriodicalPayment> activePayments = repository.findByStatus(SubscriptionStatus.ACTIVE);

        for (PeriodicalPayment p : activePayments) {
            if (isDue(p)) {
                try {
                    transactionService.transfer(p.getSourceAccount(), p.getTargetAccount(), p.getAmount());
                    p.setLastProcessedDate(LocalDate.now());
                    repository.save(p);
                    log.info("Thanh toán thành công cho lệnh ID: {}", p.getId());
                } catch (Exception e) {
                    log.error("Lỗi khi xử lý lệnh thanh toán ID: {}. Lỗi: {}", p.getId(), e.getMessage());
                }
            }
        }
    }

    // Xđ xem đã đến ngày phải trả tiền chưa
    private boolean isDue(PeriodicalPayment p) {
        LocalDate lastDate = p.getLastProcessedDate();
        if (lastDate == null) return true;

        return switch (p.getPeriod()) {
            case DAILY -> lastDate.isBefore(LocalDate.now());
            case WEEKLY -> lastDate.plusWeeks(1).isBefore(LocalDate.now()) || lastDate.plusWeeks(1).isEqual(LocalDate.now());
            case MONTHLY -> lastDate.plusMonths(1).isBefore(LocalDate.now()) || lastDate.plusMonths(1).isEqual(LocalDate.now());
        };
    }

    @Override
    public PeriodicalPaymentResponse updateStatus(Long id, String status) {
        return null;
    }

    @Override
    public List<PeriodicalPaymentResponse> getMyPayments(Long customerId) {
        return null;
    }

    @Override
    public void excuteScheduledPayment() {

    }
}