package com.hotel.management.service;

import com.hotel.management.model.*;
import com.hotel.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    public List<Room> getAvailableRooms() {
        return roomRepository.findByStatus("AVAILABLE");
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Reservation bookRoom(String guestId, String roomId,
                                LocalDate checkIn, LocalDate checkOut,
                                int nights, int numberOfGuests) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null || !room.getStatus().equals("AVAILABLE")) return null;

        String id = "RES" + String.format("%03d", reservationRepository.count() + 1);
        Reservation res = new Reservation();
        res.setReservationId(id);
        res.setGuestId(guestId);
        res.setRoomId(roomId);
        res.setCheckIn(checkIn);
        res.setCheckOut(checkOut);
        res.setNights(nights);
        res.setNumberOfGuests(numberOfGuests);
        res.setStatus("CONFIRMED");

        room.setStatus("RESERVED");
        roomRepository.save(room);

        return reservationRepository.save(res);
    }

    public List<Reservation> getGuestReservations(String guestId) {
        return reservationRepository.findByGuestId(guestId);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public boolean cancelReservation(String reservationId) {
        Optional<Reservation> opt = reservationRepository.findById(reservationId);
        if (opt.isEmpty()) return false;
        Reservation res = opt.get();
        if (!res.getStatus().equals("CONFIRMED")) return false;

        res.setStatus("CANCELLED");
        reservationRepository.save(res);

        Room room = roomRepository.findById(res.getRoomId()).orElse(null);
        if (room != null) { room.setStatus("AVAILABLE"); roomRepository.save(room); }
        return true;
    }

    public boolean checkIn(String reservationId) {
        Optional<Reservation> opt = reservationRepository.findById(reservationId);
        if (opt.isEmpty()) return false;
        Reservation res = opt.get();
        if (!res.getStatus().equals("CONFIRMED")) return false;

        res.setStatus("CHECKED_IN");
        reservationRepository.save(res);

        Room room = roomRepository.findById(res.getRoomId()).orElse(null);
        if (room != null) { room.setStatus("OCCUPIED"); roomRepository.save(room); }
        return true;
    }

    public boolean checkOut(String reservationId) {
        Optional<Reservation> opt = reservationRepository.findById(reservationId);
        if (opt.isEmpty()) return false;
        Reservation res = opt.get();
        if (!res.getStatus().equals("CHECKED_IN")) return false;

        res.setStatus("CHECKED_OUT");
        reservationRepository.save(res);

        Room room = roomRepository.findById(res.getRoomId()).orElse(null);
        if (room != null) { room.setStatus("CLEANING"); roomRepository.save(room); }
        return true;
    }

    public Optional<Room> getRoomById(String roomId) {
        return roomRepository.findById(roomId);
    }

    public Optional<Reservation> getReservationById(String id) {
        return reservationRepository.findById(id);
    }
}