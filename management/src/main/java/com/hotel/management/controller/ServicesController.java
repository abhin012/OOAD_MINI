package com.hotel.management.controller;

import com.hotel.management.model.*;
import com.hotel.management.service.ServicesService;
import com.hotel.management.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ServicesController {

    @Autowired private ServicesService servicesService;
    @Autowired private EmployeeService employeeService;

    @GetMapping("/services/request")
    public String requestForm(HttpSession session) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";
        return "services/request";
    }

    @PostMapping("/services/request")
    public String submitRequest(@RequestParam String requestType,
                                @RequestParam String description,
                                HttpSession session) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";
        servicesService.submitRequest(guest.getGuestId(), requestType, description);
        return "redirect:/services/my?submitted=true";
    }

    @GetMapping("/services/my")
    public String myRequests(HttpSession session, Model model) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";
        model.addAttribute("requests", servicesService.getGuestRequests(guest.getGuestId()));
        return "services/my-requests";
    }

    @GetMapping("/employee/services")
    public String allRequests(HttpSession session, Model model) {
        Employee emp = (Employee) session.getAttribute("loggedInEmployee");
        if (emp == null) return "redirect:/employee/login";
        model.addAttribute("requests", servicesService.getAllRequests());
        return "services/all-requests";
    }

    @PostMapping("/services/resolve")
    public String resolve(@RequestParam String requestId, HttpSession session) {
        Employee emp = (Employee) session.getAttribute("loggedInEmployee");
        if (emp == null) return "redirect:/employee/login";
        servicesService.resolveRequest(requestId);
        return "redirect:/employee/services";
    }

    @GetMapping("/employee/housekeeping")
    public String housekeepingTasks(HttpSession session, Model model) {
        Employee emp = (Employee) session.getAttribute("loggedInEmployee");
        if (emp == null) return "redirect:/employee/login";
        model.addAttribute("tasks", servicesService.getAllTasks());
        model.addAttribute("staff", employeeService.getHousekeepingStaff());
        return "services/housekeeping";
    }

    @PostMapping("/employee/housekeeping/add")
    public String addTask(@RequestParam String roomNumber,
                          @RequestParam String assignedTo, HttpSession session) {
        Employee emp = (Employee) session.getAttribute("loggedInEmployee");
        if (emp == null) return "redirect:/employee/login";
        servicesService.createTask(roomNumber, assignedTo);
        return "redirect:/employee/housekeeping";
    }

    @PostMapping("/employee/housekeeping/complete")
    public String completeTask(@RequestParam String taskId, HttpSession session) {
        Employee emp = (Employee) session.getAttribute("loggedInEmployee");
        if (emp == null) return "redirect:/employee/login";
        servicesService.markCompleted(taskId);
        return "redirect:/employee/housekeeping";
    }
}