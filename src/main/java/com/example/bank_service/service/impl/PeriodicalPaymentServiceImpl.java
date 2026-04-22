package com.example.bank_service.service.impl;

import com.example.bank_service.dto.period.PeriodicalPaymentRequestDTO;
import com.example.bank_service.dto.period.PeriodicalPaymentResponse;
import com.example.bank_service.dto.period.PeriodicalPaymentSearchDTO;
import com.example.bank_service.dto.period.PeriodicalPaymentUpdateDTO;
import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.PeriodicalPayment;
import com.example.bank_service.enums.SubscriptionStatus;
import com.example.bank_service.mapper.PeriodicalPaymentMapper;
import com.example.bank_service.repository.AccountRepository;
import com.example.bank_service.repository.PeriodicalPaymentRepository;
import com.example.bank_service.service.PeriodicalPaymentService;
import com.example.bank_service.service.TransactionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeriodicalPaymentServiceImpl implements PeriodicalPaymentService {

    private final PeriodicalPaymentRepository repository;
    private final TransactionService transactionService;
    private final PeriodicalPaymentMapper mapper;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public PeriodicalPaymentResponse createPayment(PeriodicalPaymentRequestDTO request) {
        Account sourceAccount = accountRepository.findById(request.getSourceAccountId())
                .orElseThrow(() -> new RuntimeException("Tài khoản nguồn không tồn tại"));

        Account targetAccount = accountRepository.findById(request.getTargetAccountId())
                .orElseThrow(() -> new RuntimeException("Tài khoản đích không tồn tại"));

        PeriodicalPayment payment = new PeriodicalPayment();
        payment.setAmount(request.getAmount());
        payment.setPeriod(request.getPeriod());
        payment.setStatus(SubscriptionStatus.ACTIVE);
        payment.setLastProcessedDate(LocalDate.now());

        payment.setSourceAccount(sourceAccount);
        payment.setTargetAccount(targetAccount);

        repository.save(payment);
        return mapper.toResponse(payment);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void executeScheduledPayments() {
        log.info("Bắt đầu quy trình quét thanh toán định kỳ...");
        List<PeriodicalPayment> activePayments = repository.findByStatus(SubscriptionStatus.ACTIVE);

        int successCount = 0;
        for (PeriodicalPayment payment : activePayments) {
            if (isDue(payment)) {
                try {
                    transactionService.transfer(
                            payment.getSourceAccount(),
                            payment.getTargetAccount(),
                            payment.getAmount()
                    );
                    payment.setLastProcessedDate(LocalDate.now());
                    repository.save(payment);
                    successCount++;
                    log.info("Thanh toán thành công ID {}: {} -> {}",
                            payment.getId(),
                            payment.getSourceAccount().getAccountNumber(),
                            payment.getTargetAccount().getAccountNumber());
                } catch (Exception e) {
                    log.error("Lỗi khi xử lý lệnh thanh toán ID: {}. Lỗi: {}", payment.getId(), e.getMessage());
                }
            }
        }
        log.info("Quy trình quét hoàn tất. Đã thực hiện thành công: {} lệnh.", successCount);
    }

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
    @Transactional
    public PeriodicalPaymentResponse updateStatus(Long id, String status) {
        PeriodicalPayment payment = (PeriodicalPayment) repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lệnh thanh toán!"));

        payment.setStatus(SubscriptionStatus.valueOf(status.toUpperCase()));
        repository.save(payment);

        return PeriodicalPaymentResponse.builder()
                .id(payment.getId())
                .status(payment.getStatus())
                .build();
    }

    @Override
    public List<PeriodicalPaymentResponse> getMyPayments(Long customerId) {
        return repository.findBySourceAccount_Customer_Id(customerId)
                .stream()
                .map(p -> PeriodicalPaymentResponse.builder()
                        .id(p.getId())
                        .sourceAccountId(p.getSourceAccount().getId())
                        .targetAccountId(p.getTargetAccount().getId())
                        .amount(p.getAmount())
                        .period(p.getPeriod())
                        .status(p.getStatus())
                        .lastProcessedDate(p.getLastProcessedDate())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<PeriodicalPaymentResponse> searchPayments(PeriodicalPaymentSearchDTO dto) {
        return repository.findAll().stream()
                .filter(p -> dto.getStatus() == null || p.getStatus() == dto.getStatus())
                .filter(p -> dto.getPeriod() == null || p.getPeriod() == dto.getPeriod())
                .filter(p -> dto.getMinAmount() == null || p.getAmount() >= dto.getMinAmount())
                .filter(p -> dto.getMaxAmount() == null || p.getAmount() <= dto.getMaxAmount())
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PeriodicalPaymentResponse updatePayment(Long id, PeriodicalPaymentUpdateDTO dto) {
        PeriodicalPayment payment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lệnh!"));
        if (dto.getAmount() != null) payment.setAmount(dto.getAmount());
        if (dto.getPeriod() != null) payment.setPeriod(dto.getPeriod());
        return mapper.toResponse(repository.save(payment));
    }

}