package com.hotel.management.repository;

import com.hotel.management.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Optional<Employee> findByEmployeeIdAndPassword(String employeeId, String password);
    List<Employee> findByRole(String role);
}