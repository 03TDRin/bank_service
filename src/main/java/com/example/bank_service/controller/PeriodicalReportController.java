package com.example.bank_service.controller;

import com.example.bank_service.dto.ApiResponse;
import com.example.bank_service.dto.report.CreatePeriodicalReportRequestDTO;
import com.example.bank_service.dto.report.PeriodicalReportResponseDTO;
import com.example.bank_service.service.PeriodicalReportService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class PeriodicalReportController {

    private final PeriodicalReportService service;

    @PostMapping("/generate")
    @Operation(summary = "Generate periodical report")
    public ResponseEntity<ApiResponse<PeriodicalReportResponseDTO>>
    generateReport(

            @Valid
            @RequestBody
            CreatePeriodicalReportRequestDTO request
    ) {

        PeriodicalReportResponseDTO response =
                service.generateReport(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        response,
                        "200",
                        "Generate report successfully"
                )
        );
    }

    @GetMapping
    @Operation(summary = "Get all periodical reports")
    public ResponseEntity<ApiResponse<List<PeriodicalReportResponseDTO>>>
    getAllReports() {

        List<PeriodicalReportResponseDTO> reports =
                service.getAllReports();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        reports,
                        "200",
                        "Get all reports successfully"
                )
        );
    }
}