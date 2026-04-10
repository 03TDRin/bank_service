package com.example.bank_service.service;

import com.example.bank_service.dto.auth.*;
import com.example.bank_service.dto.user.UserResponseDTO;

public interface AuthService {
    String register(AuthRequestDTO dto);
    AuthResponseDTO login(AuthRequestDTO dto);
    UserResponseDTO getMyProfile(String username);
}