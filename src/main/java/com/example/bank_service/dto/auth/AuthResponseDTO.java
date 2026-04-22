package com.example.bank_service.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class AuthResponseDTO {
    private String token;
    private String message;
    private String username;
    private String publicId;
}