package com.hotel.management.controller;

import com.hotel.management.model.Guest;
import com.hotel.management.service.GuestService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
public class GuestController {

    @Autowired
    private GuestService guestService;

    
    @GetMapping("/")
    public String home() {
        return "index";
    }

    
    @GetMapping("/guest/login")
    public String loginPage() {
        return "guest/login";
    }

    @PostMapping("/guest/login")
    public String login(@RequestParam String email,
                        @RequestParam String phone,
                        HttpSession session, Model model) {
        Optional<Guest> guest = guestService.login(email, phone);
        if (guest.isPresent()) {
            session.setAttribute("loggedInGuest", guest.get());
            return "redirect:/guest/dashboard";
        }
        model.addAttribute("error", "Invalid email or phone. Please try again.");
        return "guest/login";
    }

    @GetMapping("/guest/register")
    public String registerPage(Model model) {
        model.addAttribute("guest", new Guest());
        return "guest/register";
    }

    @PostMapping("/guest/register")
    public String register(@ModelAttribute Guest guest, Model model) {
        if (guestService.emailExists(guest.getEmail())) {
            model.addAttribute("error", "Email already registered. Please login.");
            return "guest/register";
        }
        guestService.registerGuest(guest);
        return "redirect:/guest/login?registered=true";
    }

    
    @GetMapping("/guest/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";
        model.addAttribute("guest", guest);
        return "guest/dashboard";
    }

    
    @GetMapping("/guest/profile")
    public String profile(HttpSession session, Model model) {
        Guest guest = (Guest) session.getAttribute("loggedInGuest");
        if (guest == null) return "redirect:/guest/login";
        model.addAttribute("guest", guest);
        return "guest/profile";
    }

    
    @GetMapping("/guest/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}