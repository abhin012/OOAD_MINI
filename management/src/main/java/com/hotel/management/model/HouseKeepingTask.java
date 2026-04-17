package com.hotel.management.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "housekeeping_tasks")
public class HousekeepingTask {
    @Id
    @Column(name = "task_id")
    private String taskId;

    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(name = "assigned_date")
    private LocalDate assignedDate;

    private String status;

    public HousekeepingTask() {}

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
    public LocalDate getAssignedDate() { return assignedDate; }
    public void setAssignedDate(LocalDate assignedDate) { this.assignedDate = assignedDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}