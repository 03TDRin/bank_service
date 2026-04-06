package com.example.bank_service.dto.customer;

import lombok.Data;
import java.util.UUID;

@Data
public class CustomerProfileRequest {
    private UUID publicId;
}