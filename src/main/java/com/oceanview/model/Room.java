package com.oceanview.model;

/**
 * Room model - represents a physical room in the hotel
 * Maps to rooms table in database
 */
public class Room {

    private int roomId;
    private String roomNumber;
    private int roomTypeId;
    private RoomType roomType;
    private int floorNumber;
    private RoomStatus status;

    public enum RoomStatus {
        AVAILABLE, OCCUPIED, MAINTENANCE, RESERVED
    }

    public Room() {
        this.status = RoomStatus.AVAILABLE;
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

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    /** Alias for getFloorNumber() - backward compatibility */
    public String getFloor() {
        return String.valueOf(floorNumber);
    }

    /** Alias for setFloorNumber() - backward compatibility */
    public void setFloor(String floor) {
        try {
            this.floorNumber = Integer.parseInt(floor);
        } catch (NumberFormatException e) {
            this.floorNumber = 0;
        }
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public boolean isAvailable() {
        return this.status == RoomStatus.AVAILABLE;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber='" + roomNumber + '\'' +
                ", type=" + (roomType != null ? roomType.getTypeName() : "N/A") +
                ", status=" + status +
                ", floor=" + floorNumber +
                '}';
    }
}
