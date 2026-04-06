package com.example.bank_service.service;

import com.example.bank_service.dto.customer.*;
import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerResponseDTO register(CustomerCreateDTO dto);
    String login(String username, String password);
    CustomerResponseDTO getProfile(UUID publicId);
    CustomerResponseDTO updateProfile(UUID publicId, CustomerUpdateDTO dto);
    List<CustomersSummaryDTO> getAllCustomers();
}