package com.hotel.management.service;

import com.hotel.management.model.Employee;
import com.hotel.management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Optional<Employee> login(String employeeId, String password) {
        return employeeRepository.findByEmployeeIdAndPassword(employeeId, password);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<Employee> getHousekeepingStaff() {
        return employeeRepository.findByRole("HOUSEKEEPING");
    }
}