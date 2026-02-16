package com.oceanview.model;

public class Room {
    
    private int roomId;
    private String roomNumber;
    private int roomTypeId;
    private RoomType roomType;
    private RoomStatus status;
    private String floor;
    private boolean active;
    
    public enum RoomStatus {
        AVAILABLE, OCCUPIED, RESERVED, MAINTENANCE, CLEANING
    }
    
    public Room() {
        this.status = RoomStatus.AVAILABLE;
        this.active = true;
    }
    
    public Room(int roomId, String roomNumber, int roomTypeId) {
        this();
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomTypeId = roomTypeId;
    }
    
    // Getters and Setters
    public int getRoomId() {
        return roomId;
    }
    
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public int getRoomTypeId() {
        return roomTypeId;
    }
    
    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }
    
    public RoomType getRoomType() {
        return roomType;
    }
    
    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
        if (roomType != null) {
            this.roomTypeId = roomType.getRoomTypeId();
        }
    }
    
    public RoomStatus getStatus() {
        return status;
    }
    
    public void setStatus(RoomStatus status) {
        this.status = status;
    }
    
    public String getFloor() {
        return floor;
    }
    
    public void setFloor(String floor) {
        this.floor = floor;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public boolean isAvailable() {
        return this.status == RoomStatus.AVAILABLE && this.active;
    }
    
    @Override
    public String toString() {
        return "Room{" +
                "roomNumber='" + roomNumber + '\'' +
                ", type=" + (roomType != null ? roomType.getTypeName() : "N/A") +
                ", status=" + status +
                ", floor='" + floor + '\'' +
                '}';
    }
}
