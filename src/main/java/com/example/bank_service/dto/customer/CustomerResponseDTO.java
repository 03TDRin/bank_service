package com.example.bank_service.dto.customer;

import com.example.bank_service.enums.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO {
    private String publicId;
    private String firstName;
    private String lastName;
    private String email;
    private CustomerType type;
}