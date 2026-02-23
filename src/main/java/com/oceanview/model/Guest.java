package com.oceanview.model;

import java.time.LocalDateTime;

/**
 * Guest model - represents a hotel guest
 * Maps to guests table in database
 */
public class Guest {

    private int guestId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private String city;
    private String country;
    private String idType;
    private String idNumber;
    private LocalDateTime createdAt;

    public Guest() {
        this.createdAt = LocalDateTime.now();
    }

    public Guest(String firstName, String lastName, String phone, String email) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    // Getters and Setters
    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /** Alias for backward compatibility */
    public LocalDateTime getRegisteredAt() {
        return createdAt;
    }

    /** Alias for backward compatibility */
    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.createdAt = registeredAt;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "guestId=" + guestId +
                ", name='" + getFullName() + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
