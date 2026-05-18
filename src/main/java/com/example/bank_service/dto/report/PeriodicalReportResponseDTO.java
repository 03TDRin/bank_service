package com.example.bank_service.dto.report;

import com.example.bank_service.enums.ReportType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PeriodicalReportResponseDTO {

    private UUID publicId;

    private Long numberOfTransactions;

    private BigDecimal totalAmount;

    private BigDecimal averageAmount;

    private BigDecimal maximumAmount;

    private BigDecimal minimumAmount;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private LocalDateTime timestamp;

    private ReportType reportType;
}