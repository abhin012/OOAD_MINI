package com.hotel.management.model;

import jakarta.persistence.*;

@Entity
@Table(name = "guests")
public class Guest {
    @Id
    @Column(name = "guest_id")
    private String guestId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;
    private String phone;

    @Column(name = "id_proof")
    private String idProof;

    public Guest() {}

    public Guest(String guestId, String firstName, String lastName,
                 String email, String phone, String idProof) {
        this.guestId = guestId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.idProof = idProof;
    }

    
    public String getGuestId() { return guestId; }
    public void setGuestId(String guestId) { this.guestId = guestId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getIdProof() { return idProof; }
    public void setIdProof(String idProof) { this.idProof = idProof; }

    public String getFullName() { return firstName + " " + lastName; }
}