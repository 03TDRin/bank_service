package com.example.bank_service.service.impl;

import com.example.bank_service.dto.report.CreatePeriodicalReportRequestDTO;
import com.example.bank_service.dto.report.PeriodicalReportResponseDTO;
import com.example.bank_service.entity.PeriodicalReport;
import com.example.bank_service.entity.Transaction;
import com.example.bank_service.repository.PeriodicalReportRepository;
import com.example.bank_service.repository.TransactionRepository;
import com.example.bank_service.service.PeriodicalReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PeriodicalReportServiceImpl
        implements PeriodicalReportService {

    private final TransactionRepository transactionRepository;

    private final PeriodicalReportRepository reportRepository;

    @Override
    public PeriodicalReportResponseDTO generateReport(
            CreatePeriodicalReportRequestDTO request
    ) {

        List<Transaction> transactions =
                transactionRepository.findByTransactionDateBetween(
                        request.getStartAt(),
                        request.getEndAt()
                );

        Long numberOfTransactions =
                (long) transactions.size();

        BigDecimal totalAmount = transactions.stream()
                .map(transaction ->
                        BigDecimal.valueOf(transaction.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageAmount = BigDecimal.ZERO;

        if (!transactions.isEmpty()) {

            averageAmount = totalAmount.divide(
                    BigDecimal.valueOf(transactions.size()),
                    2,
                    RoundingMode.HALF_UP
            );
        }

        BigDecimal maximumAmount = transactions.stream()
                .map(transaction ->
                        BigDecimal.valueOf(transaction.getAmount()))
                .max(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);

        BigDecimal minimumAmount = transactions.stream()
                .map(transaction ->
                        BigDecimal.valueOf(transaction.getAmount()))
                .min(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);

        PeriodicalReport report = new PeriodicalReport();

        report.setNumberOfTransactions(numberOfTransactions);

        report.setTotalAmount(totalAmount);

        report.setAverageAmount(averageAmount);

        report.setMaximumAmount(maximumAmount);

        report.setMinimumAmount(minimumAmount);

        report.setStartAt(request.getStartAt());

        report.setEndAt(request.getEndAt());

        report.setReportType(request.getReportType());

        report = reportRepository.save(report);

        return mapToDTO(report);
    }

    private PeriodicalReportResponseDTO mapToDTO(
            PeriodicalReport report
    ) {

        PeriodicalReportResponseDTO dto =
                new PeriodicalReportResponseDTO();

        dto.setPublicId(report.getPublicId());

        dto.setNumberOfTransactions(
                report.getNumberOfTransactions()
        );

        dto.setTotalAmount(report.getTotalAmount());

        dto.setAverageAmount(report.getAverageAmount());

        dto.setMaximumAmount(report.getMaximumAmount());

        dto.setMinimumAmount(report.getMinimumAmount());

        dto.setStartAt(report.getStartAt());

        dto.setEndAt(report.getEndAt());

        dto.setTimestamp(report.getTimestamp());

        dto.setReportType(report.getReportType());

        return dto;
    }

    @Override
    public List<PeriodicalReportResponseDTO> getAllReports() {

        return reportRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }
}