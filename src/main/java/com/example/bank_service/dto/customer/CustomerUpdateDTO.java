package com.example.bank_service.dto.customer;

import lombok.Data;

@Data
public class CustomerUpdateDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
}