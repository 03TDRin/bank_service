package com.example.bank_service.repository;

import com.example.bank_service.entity.PeriodicalReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PeriodicalReportRepository
        extends JpaRepository<PeriodicalReport, Long> {

    PeriodicalReport findByPublicId(UUID publicId);
}