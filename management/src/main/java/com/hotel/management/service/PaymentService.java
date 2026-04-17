package com.hotel.management.service;

import com.hotel.management.model.*;
import com.hotel.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired private InvoiceRepository invoiceRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private ReservationRepository reservationRepository;
    @Autowired private RoomRepository roomRepository;

    public Invoice generateInvoice(String reservationId) {
        Optional<Invoice> existing = invoiceRepository.findByReservationId(reservationId);
        if (existing.isPresent()) return existing.get();

        Reservation res = reservationRepository.findById(reservationId).orElse(null);
        if (res == null) return null;

        Room room = roomRepository.findById(res.getRoomId()).orElse(null);
        if (room == null) return null;

        double subtotal = room.getPricePerNight() * res.getNights();
        double tax = subtotal * 0.18;
        double total = subtotal + tax;

        String id = "INV" + String.format("%03d", invoiceRepository.count() + 1);
        Invoice inv = new Invoice();
        inv.setInvoiceId(id);
        inv.setReservationId(reservationId);
        inv.setIssueDate(LocalDate.now());
        inv.setSubtotal(subtotal);
        inv.setTaxAmount(tax);
        inv.setTotalAmount(total);
        inv.setPaid(false);
        return invoiceRepository.save(inv);
    }

    public Optional<Invoice> getInvoiceByReservation(String reservationId) {
        return invoiceRepository.findByReservationId(reservationId);
    }

    public Optional<Invoice> getInvoiceById(String invoiceId) {
        return invoiceRepository.findById(invoiceId);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Payment processPayment(String invoiceId, String method) {
        Invoice inv = invoiceRepository.findById(invoiceId).orElse(null);
        if (inv == null || inv.isPaid()) return null;

        String id = "PAY" + String.format("%03d", paymentRepository.count() + 1);
        Payment pay = new Payment();
        pay.setPaymentId(id);
        pay.setInvoiceId(invoiceId);
        pay.setAmount(inv.getTotalAmount());
        pay.setPaymentDate(LocalDate.now());
        pay.setMethod(method);
        pay.setStatus("COMPLETED");
        paymentRepository.save(pay);

        inv.setPaid(true);
        invoiceRepository.save(inv);
        return pay;
    }

    public List<Payment> getPaymentsByInvoice(String invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}