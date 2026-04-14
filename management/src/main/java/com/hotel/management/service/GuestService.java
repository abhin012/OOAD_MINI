package com.hotel.management.service;

import com.hotel.management.model.Guest;
import com.hotel.management.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GuestService {

    @Autowired
    private GuestRepository guestRepository;

    public Guest registerGuest(Guest guest) {
        String id = "G" + String.format("%03d", guestRepository.count() + 1);
        guest.setGuestId(id);
        return guestRepository.save(guest);
    }

    public Optional<Guest> login(String email, String phone) {
        return guestRepository.findByEmailAndPhone(email, phone);
    }

    public Optional<Guest> getGuestById(String id) {
        return guestRepository.findById(id);
    }

    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    public boolean emailExists(String email) {
        return guestRepository.findByEmail(email).isPresent();
    }
}