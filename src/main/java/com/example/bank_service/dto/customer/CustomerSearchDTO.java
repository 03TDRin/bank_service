package com.example.bank_service.dto.customer;

import com.example.bank_service.enums.CustomerType;
import lombok.Data;

@Data
public class CustomerSearchDTO {
    private String keyword;
    private CustomerType type;
}