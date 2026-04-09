package com.example.bank_service.service;

import com.example.bank_service.dto.alert.AlertResponseDTO;
import com.example.bank_service.entity.Account;
import java.util.List;

public interface AlertService {
    void sendNotification(Account account, String message);
    List<AlertResponseDTO> getMyAlerts(String accountNumber);
    void markAsRead(Long id);
}