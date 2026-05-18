package com.example.bank_service.dto.report;

import com.example.bank_service.enums.ReportType;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Data
public class CreatePeriodicalReportRequestDTO {

    @NotNull(message = "Start date không được để trống")
    private LocalDateTime startAt;

    @NotNull(message = "End date không được để trống")
    private LocalDateTime endAt;

    @NotNull(message = "Report type không được để trống")
    private ReportType reportType;
}