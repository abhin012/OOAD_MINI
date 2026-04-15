package com.hotel.management.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @Column(name = "room_id")
    private String roomId;

    @Column(name = "room_number")
    private String roomNumber;

    private int floor;

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "price_per_night")
    private double pricePerNight;

    private String status;

    public Room() {}

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public int getFloor() { return floor; }
    public void setFloor(int floor) { this.floor = floor; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}