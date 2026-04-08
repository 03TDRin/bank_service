package com.example.bank_service.entity;

import com.example.bank_service.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "account_status_history")
@Data
public class AccountStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    private String reason;

    @CreationTimestamp
    private LocalDateTime changedAt;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @CreationTimestamp
    private LocalDateTime createdAt;

}