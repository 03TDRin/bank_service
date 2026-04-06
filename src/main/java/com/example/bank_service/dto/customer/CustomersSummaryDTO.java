package com.example.bank_service.dto.customer;

import lombok.Data;
import java.util.UUID;

@Data
public class CustomersSummaryDTO {
    private UUID publicId;
    private String firstName;
    private String lastName;
    private String email;
}