package com.hotel.management.repository;

import com.hotel.management.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    Optional<Invoice> findByReservationId(String reservationId);
}