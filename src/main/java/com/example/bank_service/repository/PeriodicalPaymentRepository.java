package com.example.bank_service.repository;

import com.example.bank_service.entity.PeriodicalPayment;
import com.example.bank_service.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PeriodicalPaymentRepository {
    //tìm lệnh thanh toán ở trạng thái active
    List<PeriodicalPayment> findByStatus(SubscriptionStatus status);

    //tìm lệnh thanh toán của 1 KH
    List<PeriodicalPayment> findBySourceAccount_Customer_Id(Long customerId);

    void save(PeriodicalPayment payment);
}
