package com.example.bank_service.service;

import com.example.bank_service.dto.accountstatushistory.AccountStatusHistoryResponseDTO;
import com.example.bank_service.dto.accountstatushistory.AccountStatusHistorySearchDTO;
import com.example.bank_service.dto.accountstatushistory.AccountStatusHistoryUserSearchDTO;
import com.example.bank_service.entity.Account;
import com.example.bank_service.enums.AccountStatus;

import java.util.List;

public interface AccountStatusHistoryService {

    //Admin tìm kiếm lịch sử trạng thái tài khoản
    List<AccountStatusHistoryResponseDTO>
    search(AccountStatusHistorySearchDTO request);

    //User tìm kiếm lịch sử trạng thái
    List<AccountStatusHistoryResponseDTO>
    userSearch(AccountStatusHistoryUserSearchDTO request);

    //Lưu lịch sử khi thay đổi trạng thái tài khoản
    void logStatusChange(
            Account account,
            AccountStatus status,
            String reason
    );

    //Lấy toàn bộ lịch sử trạng thái tài khoản
    List<AccountStatusHistoryResponseDTO>
    getAll();

    //Lấy lịch sử theo account number
    List<AccountStatusHistoryResponseDTO>
    getByAccountNumber(String accountNumber);
}