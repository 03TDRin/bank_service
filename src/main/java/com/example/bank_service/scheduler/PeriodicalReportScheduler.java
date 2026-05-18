package com.example.bank_service.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PeriodicalReportScheduler {

    @Scheduled(cron = "0 0 0 1 * ?")
    public void generateMonthlyReport() {

        System.out.println("Generate monthly report...");
    }
    //tự tạo report mỗi tháng
}