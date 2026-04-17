package com.example.bank_service.repository;

import com.example.bank_service.entity.PeriodicalPayment;
import com.example.bank_service.enums.SubscriptionStatus;
import org.apache.logging.log4j.simple.internal.SimpleProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.ScopedValue;
import java.util.List;

@Repository
public interface PeriodicalPaymentRepository {
    //tìm lệnh thanh toán ở trạng thái active
    List<PeriodicalPayment> findByStatus(SubscriptionStatus status);

    //tìm lệnh thanh toán của 1 KH
    List<PeriodicalPayment> findBySourceAccount_Customer_Id(Long customerId);

    void save(PeriodicalPayment payment);

    <T> ScopedValue<T> findById(Long id);

    SimpleProvider.Config findAll();
}
