package com.example.bank_service.service;

import com.example.bank_service.dto.auth.*;

public interface AuthService {
    String register(AuthRequestDTO dto);
    String login(AuthRequestDTO dto);
}