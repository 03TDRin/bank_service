package com.example.bank_service.service;

import com.example.bank_service.dto.customer.*;
import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerResponseDTO register(CustomerCreateDTO dto);
    String login(String username, String password);
    CustomerResponseDTO getProfile(String publicId);
    CustomerResponseDTO updateProfile(String publicId, CustomerUpdateDTO dto);
    List<CustomersSummaryDTO> getAllCustomers();
}