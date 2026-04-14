package com.hotel.management.repository;

import com.hotel.management.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, String> {
    Optional<Guest> findByEmailAndPhone(String email, String phone);
    Optional<Guest> findByEmail(String email);
}