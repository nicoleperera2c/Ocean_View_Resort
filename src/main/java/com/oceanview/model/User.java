package com.oceanview.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User entity representing system users
 * Implements Serializable for session storage
 */
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int userId;
    private String username;
    private String passwordHash;
    private String fullName;
    private UserRole role;
    private String email;
    private String phone;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    
    // Enumeration for user roles
    public enum UserRole {
        ADMIN, STAFF, MANAGER
    }
    
    // Constructors
    public User() {}
    
    public User(String username, String passwordHash, String fullName, 
                UserRole role, String email) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
        this.email = email;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { 
        this.passwordHash = passwordHash; 
    }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }
    
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { 
        this.lastLogin = lastLogin; 
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role=" + role +
                ", email='" + email + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return userId == user.userId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(userId);
    }
}
