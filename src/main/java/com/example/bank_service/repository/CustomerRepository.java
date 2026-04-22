package com.example.bank_service.repository;

import com.example.bank_service.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByPublicId(String publicId);
    Optional<Customer> findByEmail(String email);
}