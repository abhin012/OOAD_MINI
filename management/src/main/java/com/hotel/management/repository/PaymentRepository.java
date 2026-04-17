package com.hotel.management.repository;

import com.hotel.management.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByInvoiceId(String invoiceId);
}