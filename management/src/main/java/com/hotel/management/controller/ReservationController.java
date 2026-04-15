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

    @Autowired
    private ReservationService reservationService;

    // View available rooms
    @GetMapping("/rooms/available")
    public String availableRooms(Model model, HttpSession session) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";
        model.addAttribute("rooms", reservationService.getAvailableRooms());
        return "reservation/available-rooms";
    }

    // Book room form
    @GetMapping("/reservations/book")
    public String bookForm(Model model, HttpSession session) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";
        model.addAttribute("rooms", reservationService.getAvailableRooms());
        return "reservation/book";
    }

    @PostMapping("/reservations/book")
    public String book(@RequestParam String roomId,
                       @RequestParam String checkIn,
                       @RequestParam String checkOut,
                       @RequestParam int nights,
                       @RequestParam int numberOfGuests,
                       HttpSession session, Model model) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";

        Reservation res = reservationService.bookRoom(
                guest.getGuestId(), roomId,
                LocalDate.parse(checkIn), LocalDate.parse(checkOut),
                nights, numberOfGuests);

        if (res == null) {
            model.addAttribute("error", "Room not available.");
            model.addAttribute("rooms", reservationService.getAvailableRooms());
            return "reservation/book";
        }
        return "redirect:/reservations/my?booked=" + res.getReservationId();
    }

    // My reservations
    @GetMapping("/reservations/my")
    public String myReservations(HttpSession session, Model model) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";
        List<Reservation> list = reservationService.getGuestReservations(guest.getGuestId());
        model.addAttribute("reservations", list);
        return "reservation/my-reservations";
    }

    // Cancel
    @PostMapping("/reservations/cancel")
    public String cancel(@RequestParam String reservationId, HttpSession session) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";
        reservationService.cancelReservation(reservationId);
        return "redirect:/reservations/my";
    }

    // Employee check-in
    @PostMapping("/reservations/checkin")
    public String checkIn(@RequestParam String reservationId) {
        reservationService.checkIn(reservationId);
        return "redirect:/employee/reservations";
    }

    // Employee check-out
    @PostMapping("/reservations/checkout")
    public String checkOut(@RequestParam String reservationId) {
        reservationService.checkOut(reservationId);
        return "redirect:/employee/reservations";
    }
}