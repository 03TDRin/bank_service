package com.example.bank_service.dto.account;

import lombok.Data;

//Check nguười nhận trước khi chuyển tien
@Data
public class ReceiverDTO {
    private String accountNumber;
    private String fullName;
}
