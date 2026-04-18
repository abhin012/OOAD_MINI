package com.hotel.management.controller;

import com.hotel.management.model.*;
import com.hotel.management.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ReservationController {

    @Autowired private ReservationService reservationService;

    @GetMapping("/rooms/available")
    public String availableRooms(Model model, HttpSession session) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";
        model.addAttribute("rooms", reservationService.getAvailableRooms());
        return "reservation/available-rooms";
    }

    @GetMapping("/reservations/book")
    public String bookForm(Model model, HttpSession session) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";
        model.addAttribute("rooms", reservationService.getAvailableRooms());
        // Pass tomorrow's date as the minimum selectable date
        model.addAttribute("minDate", LocalDate.now().plusDays(1).toString());
        return "reservation/book";
    }

    @PostMapping("/reservations/book")
    public String book(@RequestParam String roomId,
                       @RequestParam String checkIn,
                       @RequestParam String checkOut,
                       @RequestParam int numberOfGuests,
                       HttpSession session, Model model) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";

        LocalDate ci = LocalDate.parse(checkIn);
        LocalDate co = LocalDate.parse(checkOut);
        LocalDate today = LocalDate.now();

        // Validate: check-in must be in the future
        if (!ci.isAfter(today)) {
            model.addAttribute("error", "Check-in date must be after today.");
            model.addAttribute("rooms", reservationService.getAvailableRooms());
            model.addAttribute("minDate", today.plusDays(1).toString());
            return "reservation/book";
        }

        // Validate: check-out must be after check-in
        if (!co.isAfter(ci)) {
            model.addAttribute("error", "Check-out date must be after check-in date.");
            model.addAttribute("rooms", reservationService.getAvailableRooms());
            model.addAttribute("minDate", today.plusDays(1).toString());
            return "reservation/book";
        }

        Reservation res = reservationService.bookRoom(
                guest.getGuestId(), roomId, ci, co, numberOfGuests);

        if (res == null) {
            model.addAttribute("error", "Room not available or invalid dates. Please try again.");
            model.addAttribute("rooms", reservationService.getAvailableRooms());
            model.addAttribute("minDate", today.plusDays(1).toString());
            return "reservation/book";
        }
        return "redirect:/reservations/my?booked=" + res.getReservationId();
    }

    @GetMapping("/reservations/my")
    public String myReservations(HttpSession session, Model model) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";
        List<Reservation> list = reservationService.getGuestReservations(guest.getGuestId());
        model.addAttribute("reservations", list);
        return "reservation/my-reservations";
    }

    @PostMapping("/reservations/cancel")
    public String cancel(@RequestParam String reservationId, HttpSession session) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";
        reservationService.cancelReservation(reservationId);
        return "redirect:/reservations/my";
    }

    @PostMapping("/reservations/checkin")
    public String checkIn(@RequestParam String reservationId) {
        reservationService.checkIn(reservationId);
        return "redirect:/employee/reservations";
    }

    @PostMapping("/reservations/checkout")
    public String checkOut(@RequestParam String reservationId) {
        reservationService.checkOut(reservationId);
        return "redirect:/employee/reservations";
    }
}