package com.example.bank_service.service;

import com.example.bank_service.entity.Account;
import com.example.bank_service.enums.AccountStatus;
import java.util.List;

public interface AccountStatusHistoryService {
    void logStatusChange(Account account, AccountStatus status, String reason);
    //Rảnh thì thêm hàm lấy lịch sử trạng thái
}