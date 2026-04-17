package com.hotel.management.controller;

import com.hotel.management.model.*;
import com.hotel.management.service.PaymentService;
import com.hotel.management.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
public class PaymentController {

    @Autowired private PaymentService paymentService;
    @Autowired private ReservationService reservationService;

    @GetMapping("/payments/invoice/{reservationId}")
    public String viewInvoice(@PathVariable String reservationId,
                               HttpSession session, Model model) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        Employee emp = (Employee) session.getAttribute("loggedInEmployee");
        if (guest == null && emp == null) return "redirect:/guest/login";

        Invoice inv = paymentService.generateInvoice(reservationId);
        Optional<Reservation> res = reservationService.getReservationById(reservationId);
        Optional<Room> room = res.isPresent() ?
                reservationService.getRoomById(res.get().getRoomId()) : Optional.empty();

        model.addAttribute("invoice", inv);
        model.addAttribute("reservation", res.orElse(null));
        model.addAttribute("room", room.orElse(null));
        model.addAttribute("payments", paymentService.getPaymentsByInvoice(inv.getInvoiceId()));
        return "payment/invoice";
    }

    @GetMapping("/payments/pay/{invoiceId}")
    public String payPage(@PathVariable String invoiceId,
                          HttpSession session, Model model) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";
        model.addAttribute("invoice", paymentService.getInvoiceById(invoiceId).orElse(null));
        return "payment/pay";
    }

    @PostMapping("/payments/pay")
    public String pay(@RequestParam String invoiceId, @RequestParam String method,
                      HttpSession session) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";
        Payment pay = paymentService.processPayment(invoiceId, method);
        if (pay == null) return "redirect:/payments/pay/" + invoiceId + "?error=true";
        return "redirect:/payments/success/" + pay.getPaymentId();
    }

    @GetMapping("/payments/success/{paymentId}")
    public String success(@PathVariable String paymentId, Model model) {
        model.addAttribute("paymentId", paymentId);
        return "payment/success";
    }

    @GetMapping("/payments/my")
    public String myPayments(HttpSession session, Model model) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";
        model.addAttribute("invoices", paymentService.getAllInvoices());
        return "payment/my-payments";
    }
}