package com.example.bank_service.repository;

import com.example.bank_service.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    //Tìm kiếm KH theo Public ID
    Optional<Customer> findByPublicId(UUID publicId);

    //Tìm kiếm theo Email
    Optional<Customer> findByEmail(String email);
}