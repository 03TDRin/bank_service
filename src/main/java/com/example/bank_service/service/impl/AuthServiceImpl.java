package com.example.bank_service.service.impl;

import com.example.bank_service.dto.auth.AuthRequestDTO;
import com.example.bank_service.dto.auth.AuthResponseDTO;
import com.example.bank_service.dto.user.UserResponseDTO;
import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.Customer;
import com.example.bank_service.entity.User;
import com.example.bank_service.enums.AccountStatus;
import com.example.bank_service.repository.AccountRepository;
import com.example.bank_service.repository.CustomerRepository; // Nhớ thêm repo này
import com.example.bank_service.repository.UserRepository;
import com.example.bank_service.security.JwtUtils;
import com.example.bank_service.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public String register(AuthRequestDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }

        String sharedPublicId = UUID.randomUUID().toString();

        Account newAccount = new Account();
        newAccount.setAccountNumber("VNB" + System.currentTimeMillis());
        newAccount.setBalance(50000.0);
        newAccount.setStatus(AccountStatus.ACTIVE);
        Account savedAccount = accountRepository.save(newAccount);

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setAccount(savedAccount);
        user.setPublicId(sharedPublicId);
        User savedUser = userRepository.save(user);

        Customer customer = new Customer();
        customer.setPublicId(sharedPublicId);
        customer.setUser(savedUser);

        customer.setFirstName("Họ");
        customer.setLastName("Tên");
        customer.setEmail(dto.getUsername() + "@bvbank.com.vn");
        customer.setPhoneNumber("0" + (100000000 + (long)(Math.random() * 900000000L)));

        customer.setType(com.example.bank_service.enums.CustomerType.INDIVIDUAL);

        customerRepository.save(customer);

        return "Đăng ký thành công! STK của bạn là: " + savedAccount.getAccountNumber();
    }
    @Override
    public AuthResponseDTO login(AuthRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        String token = jwtUtils.generateToken(dto.getUsername());

        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow();

        return AuthResponseDTO.builder()
                .token(token)
                .username(dto.getUsername())
                .publicId(user.getPublicId())
                .message("Đăng nhập thành công vào hệ thống BVBank!")
                .build();
    }

    @Override
    public UserResponseDTO getMyProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        UserResponseDTO dto = new UserResponseDTO();
        dto.setUsername(user.getUsername());
        dto.setPublicId(user.getPublicId());

        if (user.getAccount() != null) {
            dto.setAccountNumber(user.getAccount().getAccountNumber());
            dto.setBalance(user.getAccount().getBalance());
        }

        return dto;
    }
}