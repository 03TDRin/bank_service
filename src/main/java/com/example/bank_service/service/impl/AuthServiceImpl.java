package com.example.bank_service.service.impl;

import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.User;
import com.example.bank_service.dto.auth.AuthRequestDTO;
import com.example.bank_service.enums.AccountStatus;
import com.example.bank_service.repository.AccountRepository;
import com.example.bank_service.repository.UserRepository;
import com.example.bank_service.security.JwtUtils;
import com.example.bank_service.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public String register(AuthRequestDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }

        //Tạo acc mới cho user
        Account newAccount = new Account();
        newAccount.setAccountNumber("VNB" + System.currentTimeMillis()); // Tạo số TK tạm thời từ thời gian
        newAccount.setBalance(50000.0);
        newAccount.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(newAccount);

        //Tạo user và gắn acc vừa tạo
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setAccount(newAccount);
        userRepository.save(user);

        return "Đăng ký thành công! Số tài khoản của bạn là: " + newAccount.getAccountNumber();
    }

    // Inject JwtUtils vào constructor
    private final JwtUtils jwtUtils;

    @Override
    public String login(AuthRequestDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Sai tên đăng nhập hoặc mật khẩu!"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Sai tên đăng nhập hoặc mật khẩu!");
        }

        return jwtUtils.generateToken(user.getUsername());
    }
}