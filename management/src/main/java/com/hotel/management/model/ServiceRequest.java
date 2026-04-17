package com.hotel.management.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "service_requests")
public class ServiceRequest {
    @Id
    @Column(name = "request_id")
    private String requestId;

    @Column(name = "guest_id")
    private String guestId;

    @Column(name = "request_type")
    private String requestType;

    private String description;

    @Column(name = "request_date")
    private LocalDate requestDate;

    private String status;

    public ServiceRequest() {}

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public String getGuestId() { return guestId; }
    public void setGuestId(String guestId) { this.guestId = guestId; }
    public String getRequestType() { return requestType; }
    public void setRequestType(String requestType) { this.requestType = requestType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDate requestDate) { this.requestDate = requestDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}