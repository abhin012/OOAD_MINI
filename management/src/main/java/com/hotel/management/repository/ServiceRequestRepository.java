package com.hotel.management.repository;

import com.hotel.management.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, String> {
    List<ServiceRequest> findByGuestId(String guestId);
    List<ServiceRequest> findByStatus(String status);
}