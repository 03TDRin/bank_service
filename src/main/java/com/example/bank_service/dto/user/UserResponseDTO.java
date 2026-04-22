package com.example.bank_service.dto.user;

import lombok.Data;

@Data
public class UserResponseDTO {
    private String username;
    private String accountNumber;
    private Double balance;
    private String publicId;
}