package com.example.bank_service.dto.transaction;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull; // Dùng cái này cho Double
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TransactionTransferDTO {
    @NotBlank(message = "Số tài khoản gửi không được để trống")
    private String fromAccountNumber;

    @NotBlank(message = "Số tài khoản nhận không được để trống")
    private String toAccountNumber;

    @NotNull(message = "Số tiền không được để trống")
    @Min(value = 1000, message = "Giao dịch tối thiểu là 1.000đ")
    private Double amount;

    private String description;
}