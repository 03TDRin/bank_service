package com.example.bank_service.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "alerts")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private String message;

    @Column(name = "is_read")
    private boolean isRead;

    @CreationTimestamp
    private LocalDateTime createAt;
}