package com.example.bank_service.service.impl;

import com.example.bank_service.dto.alert.AlertResponseDTO;
import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.Alert;
import com.example.bank_service.repository.AlertRepository;
import com.example.bank_service.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;

    @Override
    @Transactional
    public void sendNotification(Account account, String message) {
        Alert alert = new Alert();
        alert.setAccount(account);
        alert.setMessage(message);
        alert.setRead(false);
        alertRepository.save(alert);
    }

    @Override
    public List<AlertResponseDTO> getMyAlerts(String accountNumber) {
        return alertRepository.findByAccount_AccountNumberOrderByCreateAtDesc(accountNumber)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo!"));
        alert.setRead(true);
        alertRepository.save(alert);
    }

    private AlertResponseDTO mapToDTO(Alert alert) {
        AlertResponseDTO dto = new AlertResponseDTO();
        dto.setId(alert.getId()); // Dùng ID tạm thay cho publicId
        dto.setDescription(alert.getMessage());
        dto.setRead(alert.isRead());
        dto.setTimestamp(alert.getCreateAt());
        dto.setAccountNumber(alert.getAccount().getAccountNumber());
        return dto;
    }
}