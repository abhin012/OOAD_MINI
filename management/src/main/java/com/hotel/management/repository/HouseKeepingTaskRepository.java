package com.hotel.management.repository;

import com.hotel.management.model.HousekeepingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HousekeepingTaskRepository extends JpaRepository<HousekeepingTask, String> {
    List<HousekeepingTask> findByStatus(String status);
    List<HousekeepingTask> findByAssignedTo(String assignedTo);
}