package com.example.bank_service.service.impl;

import com.example.bank_service.dto.customer.*;
import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.Customer;
import com.example.bank_service.entity.User;
import com.example.bank_service.repository.CustomerRepository;
import com.example.bank_service.repository.UserRepository;
import com.example.bank_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CustomerResponseDTO register(CustomerCreateDTO dto) {
        //Tạo User
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());

        //Tạo Customer
        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setType(dto.getType());
        customer.setUser(user);

        //Tạo 1 tk mặc định
        Account defaultAccount = new Account();
        defaultAccount.setAccountNumber(UUID.randomUUID().toString().substring(0, 8));
        defaultAccount.setBalance(0.0);
        defaultAccount.setCustomer(customer);

        customer.setAccounts(Collections.singletonList(defaultAccount));

        //Lưu DB
        Customer savedCustomer = customerRepository.save(customer);

        return mapToResponseDTO(savedCustomer);
    }

    @Override
    public String login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password))
                .map(user -> "Đăng nhập thành công!")
                .orElse("Sai thông tin đăng nhập!");
    }

    @Override
    public CustomerResponseDTO getProfile(String publicId) {
        Customer customer = customerRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng!"));
        return mapToResponseDTO(customer);
    }

    @Override
    @Transactional
    public CustomerResponseDTO updateProfile(String publicId, CustomerUpdateDTO dto) {
        Customer customer = customerRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng để cập nhật!"));

        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setEmail(dto.getEmail());

        Customer updatedCustomer = customerRepository.save(customer);
        return mapToResponseDTO(updatedCustomer);
    }

    @Override
    public List<CustomersSummaryDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customer -> {
                    CustomersSummaryDTO summary = new CustomersSummaryDTO();
                    summary.setPublicId(customer.getPublicId());
                    summary.setFirstName(customer.getFirstName());
                    summary.setLastName(customer.getLastName());
                    summary.setEmail(customer.getEmail());
                    return summary;
                })
                .collect(Collectors.toList());
    }

    private CustomerResponseDTO mapToResponseDTO(Customer customer) {
        CustomerResponseDTO response = new CustomerResponseDTO();
        response.setPublicId(customer.getPublicId());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setEmail(customer.getEmail());
        response.setType(customer.getType());
        return response;
    }
}