package com.hotel.management.controller;

import com.hotel.management.model.*;
import com.hotel.management.service.PaymentService;
import com.hotel.management.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Controller
public class PaymentController {

    @Autowired private PaymentService paymentService;
    @Autowired private ReservationService reservationService;

    // View invoice for a specific reservation
    @GetMapping("/payments/invoice/{reservationId}")
    public String viewInvoice(@PathVariable String reservationId,
                               HttpSession session, Model model) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        Employee emp = (Employee) session.getAttribute("loggedInEmployee");
        if (guest == null && emp == null) return "redirect:/guest/login";

        Invoice inv = paymentService.generateInvoice(reservationId);
        if (inv == null) return "redirect:/reservations/my";

        Optional<Reservation> res = reservationService.getReservationById(reservationId);
        Optional<Room> room = res.isPresent() ?
                reservationService.getRoomById(res.get().getRoomId()) : Optional.empty();

        model.addAttribute("invoice", inv);
        model.addAttribute("reservation", res.orElse(null));
        model.addAttribute("room", room.orElse(null));
        model.addAttribute("payments", paymentService.getPaymentsByInvoice(inv.getInvoiceId()));
        return "payment/invoice";
    }

    // Pay page
    @GetMapping("/payments/pay/{invoiceId}")
    public String payPage(@PathVariable String invoiceId,
                          HttpSession session, Model model) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";
        Optional<Invoice> inv = paymentService.getInvoiceById(invoiceId);
        if (inv.isEmpty()) return "redirect:/reservations/my";
        if (inv.get().isPaid()) return "redirect:/reservations/my?alreadypaid=true";
        model.addAttribute("invoice", inv.get());
        return "payment/pay";
    }

    @PostMapping("/payments/pay")
    public String pay(@RequestParam String invoiceId,
                      @RequestParam String method,
                      HttpSession session) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";
        Payment pay = paymentService.processPayment(invoiceId, method);
        if (pay == null) return "redirect:/payments/pay/" + invoiceId + "?error=true";
        return "redirect:/payments/success/" + pay.getPaymentId();
    }

    // Payment success
    @GetMapping("/payments/success/{paymentId}")
    public String success(@PathVariable String paymentId, Model model) {
        model.addAttribute("paymentId", paymentId);
        return "payment/success";
    }

    // Guest — my payments (only show THIS guest's invoices)
    @GetMapping("/payments/my")
    public String myPayments(HttpSession session, Model model) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";

        // Get only reservations belonging to this guest
        List<Reservation> myReservations = reservationService.getGuestReservations(guest.getGuestId());

        // Build list of invoices for this guest's reservations only
        List<Invoice> myInvoices = new ArrayList<>();
        List<Reservation> reservationsWithInvoices = new ArrayList<>();

        for (Reservation res : myReservations) {
            Optional<Invoice> inv = paymentService.getInvoiceByReservation(res.getReservationId());
            if (inv.isPresent()) {
                myInvoices.add(inv.get());
                reservationsWithInvoices.add(res);
            }
        }

        model.addAttribute("invoices", myInvoices);
        model.addAttribute("reservations", myReservations);
        return "payment/my-payments";
    }
}