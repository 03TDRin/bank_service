package com.example.bank_service.repository;

import com.example.bank_service.entity.PeriodicalPayment;
import com.example.bank_service.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodicalPaymentRepository extends JpaRepository<PeriodicalPayment, Long> {

    @EntityGraph(attributePaths = {"sourceAccount", "targetAccount"})
    List<PeriodicalPayment> findByStatus(SubscriptionStatus status);

    List<PeriodicalPayment> findBySourceAccount_Customer_Id(Long customerId);
}