package com.example.bank_service.service;

import com.example.bank_service.dto.report.CreatePeriodicalReportRequestDTO;
import com.example.bank_service.dto.report.PeriodicalReportResponseDTO;

import java.util.List;

public interface PeriodicalReportService {

    PeriodicalReportResponseDTO generateReport(
            CreatePeriodicalReportRequestDTO request
    );

    List<PeriodicalReportResponseDTO> getAllReports();
}