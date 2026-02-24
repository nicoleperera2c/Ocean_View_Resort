package com.oceanview.model;

import java.math.BigDecimal;

/**
 * RoomType model - represents a category of rooms with pricing
 * Maps to room_types table in database
 */
public class RoomType {

    private int roomTypeId;
    private String typeName;
    private String description;
    private BigDecimal basePrice;
    private int maxOccupancy;
    private String amenities;
    private boolean active;

    public RoomType() {
        this.active = true;
    }

    public RoomType(int roomTypeId, String typeName, int maxOccupancy, BigDecimal basePrice) {
        this();
        this.roomTypeId = roomTypeId;
        this.typeName = typeName;
        this.maxOccupancy = maxOccupancy;
        this.basePrice = basePrice;
    }

    // Getters and Setters
    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    /** Alias for getBasePrice() - backward compatibility */
    public BigDecimal getPricePerNight() {
        return basePrice;
    }

    /** Alias for setBasePrice() - backward compatibility */
    public void setPricePerNight(BigDecimal pricePerNight) {
        this.basePrice = pricePerNight;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    /** Alias for getMaxOccupancy() - backward compatibility */
    public int getCapacity() {
        return maxOccupancy;
    }

    /** Alias for setMaxOccupancy() - backward compatibility */
    public void setCapacity(int capacity) {
        this.maxOccupancy = capacity;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "RoomType{" +
                "typeName='" + typeName + '\'' +
                ", maxOccupancy=" + maxOccupancy +
                ", basePrice=" + basePrice +
                '}';
    }
}
