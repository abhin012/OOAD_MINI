package com.hotel.management.controller;

import com.hotel.management.model.*;
import com.hotel.management.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
public class EmployeeController {

    @Autowired private EmployeeService employeeService;
    @Autowired private ReservationService reservationService;
    @Autowired private GuestService guestService;
    @Autowired private PaymentService paymentService;

    @GetMapping("/employee/login")
    public String loginPage() { return "employee/login"; }

    @PostMapping("/employee/login")
    public String login(@RequestParam String employeeId, @RequestParam String password,
                        HttpSession session, Model model) {
        Optional<Employee> emp = employeeService.login(employeeId, password);
        if (emp.isPresent()) {
            session.setAttribute("loggedInEmployee", emp.get());
            return "redirect:/employee/dashboard";
        }
        model.addAttribute("error", "Invalid credentials.");
        return "employee/login";
    }

    @GetMapping("/employee/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Employee emp = (Employee) session.getAttribute("loggedInEmployee");
        if (emp == null) return "redirect:/employee/login";
        model.addAttribute("employee", emp);
        model.addAttribute("totalGuests", guestService.getAllGuests().size());
        model.addAttribute("totalReservations", reservationService.getAllReservations().size());
        model.addAttribute("availableRooms", reservationService.getAvailableRooms().size());
        return "employee/dashboard";
    }

    @GetMapping("/employee/guests")
    public String guests(HttpSession session, Model model) {
        Employee emp = (Employee) session.getAttribute("loggedInEmployee");
        if (emp == null) return "redirect:/employee/login";
        model.addAttribute("guests", guestService.getAllGuests());
        return "employee/guests";
    }

    @GetMapping("/employee/reservations")
    public String reservations(HttpSession session, Model model) {
        Employee emp = (Employee) session.getAttribute("loggedInEmployee");
        if (emp == null) return "redirect:/employee/login";
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "employee/reservations";
    }

    @GetMapping("/employee/payments")
    public String payments(HttpSession session, Model model) {
        Employee emp = (Employee) session.getAttribute("loggedInEmployee");
        if (emp == null) return "redirect:/employee/login";
        model.addAttribute("invoices", paymentService.getAllInvoices());
        return "employee/payments";
    }

    @GetMapping("/employee/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}