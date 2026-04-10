package com.example.bank_service.service.impl;

import com.example.bank_service.dto.auth.AuthRequestDTO;
import com.example.bank_service.dto.auth.AuthResponseDTO; // Import DTO mới
import com.example.bank_service.dto.user.UserResponseDTO;
import com.example.bank_service.entity.Account;
import com.example.bank_service.entity.User;
import com.example.bank_service.enums.AccountStatus;
import com.example.bank_service.repository.AccountRepository;
import com.example.bank_service.repository.UserRepository;
import com.example.bank_service.security.JwtUtils;
import com.example.bank_service.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager; // Cần thiết để login chuẩn Security

    @Override
    @Transactional
    public String register(AuthRequestDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }

        //Tạo Account mới cho user
        Account newAccount = new Account();
        //Tạo stk: VNB + timestamp
        newAccount.setAccountNumber("VNB" + System.currentTimeMillis());
        newAccount.setBalance(50000.0); // Tặng 50k làm vốn nè
        newAccount.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(newAccount);

        //Tạo User và gắn Account vừa tạo
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // Luôn nhớ mã hóa pass
        user.setAccount(newAccount);
        userRepository.save(user);

        return "Đăng ký thành công! Số tài khoản của bạn là: " + newAccount.getAccountNumber();
    }

    @Override
    public AuthResponseDTO login(AuthRequestDTO dto) {
        //Xác thực bằng AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        //Nếu không lỗi, tạo JWT Token
        //Dùng username làm subject cho token
        String token = jwtUtils.generateToken(dto.getUsername());

        return AuthResponseDTO.builder()
                .token(token)
                .username(dto.getUsername())
                .message("Đăng nhập thành công vào hệ thống BVBank!")
                .build();
    }

    @Override
    public UserResponseDTO getMyProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        UserResponseDTO dto = new UserResponseDTO();
        dto.setUsername(user.getUsername());

        if (user.getAccount() != null) {
            dto.setAccountNumber(user.getAccount().getAccountNumber());
            dto.setBalance(user.getAccount().getBalance());
        }

        return dto;
    }
}