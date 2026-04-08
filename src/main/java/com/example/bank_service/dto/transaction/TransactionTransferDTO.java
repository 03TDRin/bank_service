package com.example.bank_service.dto.transaction;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TransactionTransferDTO {
    @NotBlank(message = "Số tài khoản gửi không được để trống")
    private String fromAccountNumber;

    @NotBlank(message = "Số tài khoản nhận không được để trống")
    private String toAccountNumber;

    @NotBlank(message = "Số tiền không được để trống")
    @Min(value = 10000, message = "Giao dịch tối thiểu là 10.000đ")
    private Double amount;

    private String description;
}
