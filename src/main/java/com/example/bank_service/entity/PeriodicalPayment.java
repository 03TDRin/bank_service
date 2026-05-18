package com.example.bank_service.entity;

import com.example.bank_service.enums.PaymentPeriod;
import com.example.bank_service.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "periodical_payments")
@Data // Gộp hết Getter/Setter/ToString...
@NoArgsConstructor
@AllArgsConstructor
public class PeriodicalPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_account_id")
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "target_account_id")
    private Account targetAccount;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentPeriod period;

    private LocalDate lastProcessedDate;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    //lưu nội dung thanh toán
    @Column(columnDefinition = "nvarchar(255)")
    private String description;
}