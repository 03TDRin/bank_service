package com.example.bank_service.controller;

import com.example.bank_service.dto.auth.AuthRequestDTO;
import com.example.bank_service.dto.auth.AuthResponseDTO;
import com.example.bank_service.dto.user.UserResponseDTO;
import com.example.bank_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    //Đky tài khoản mới
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequestDTO dto) {
        try {
            return ResponseEntity.ok(authService.register(dto));
        } catch (RuntimeException e) {
            //Trả về lỗi 400 nếu tên đăng nhập đã tồn tại
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Login để lấy Token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO dto) {
        try {
            AuthResponseDTO response = authService.login(dto);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Sai tên đăng nhập hoặc mật khẩu!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    //Lấy in4 cá nhân của người đang login
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(authService.getMyProfile(principal.getName()));
    }
}