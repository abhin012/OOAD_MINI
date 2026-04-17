package com.hotel.management.service;

import com.hotel.management.model.*;
import com.hotel.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ServicesService {

    @Autowired private ServiceRequestRepository serviceRequestRepository;
    @Autowired private HousekeepingTaskRepository housekeepingTaskRepository;

    public ServiceRequest submitRequest(String guestId, String type, String description) {
        String id = "REQ" + String.format("%03d", serviceRequestRepository.count() + 1);
        ServiceRequest req = new ServiceRequest();
        req.setRequestId(id);
        req.setGuestId(guestId);
        req.setRequestType(type);
        req.setDescription(description);
        req.setRequestDate(LocalDate.now());
        req.setStatus("OPEN");
        return serviceRequestRepository.save(req);
    }

    public List<ServiceRequest> getGuestRequests(String guestId) {
        return serviceRequestRepository.findByGuestId(guestId);
    }

    public List<ServiceRequest> getAllRequests() {
        return serviceRequestRepository.findAll();
    }

    public boolean resolveRequest(String requestId) {
        Optional<ServiceRequest> opt = serviceRequestRepository.findById(requestId);
        if (opt.isEmpty()) return false;
        ServiceRequest req = opt.get();
        req.setStatus("RESOLVED");
        serviceRequestRepository.save(req);
        return true;
    }

    public HousekeepingTask createTask(String roomNumber, String assignedTo) {
        String id = "TASK" + String.format("%03d", housekeepingTaskRepository.count() + 1);
        HousekeepingTask task = new HousekeepingTask();
        task.setTaskId(id);
        task.setRoomNumber(roomNumber);
        task.setAssignedTo(assignedTo);
        task.setAssignedDate(LocalDate.now());
        task.setStatus("PENDING");
        return housekeepingTaskRepository.save(task);
    }

    public List<HousekeepingTask> getAllTasks() {
        return housekeepingTaskRepository.findAll();
    }

    public boolean markCompleted(String taskId) {
        Optional<HousekeepingTask> opt = housekeepingTaskRepository.findById(taskId);
        if (opt.isEmpty()) return false;
        HousekeepingTask task = opt.get();
        task.setStatus("COMPLETED");
        housekeepingTaskRepository.save(task);
        return true;
    }
}