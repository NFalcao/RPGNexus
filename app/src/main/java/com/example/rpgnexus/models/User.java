package com.example.rpgnexus.models;

import java.io.Serializable;

public class User implements Serializable {
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    private String id;
    private String email;
    private String displayName;
    private String role;
    private boolean isBlocked;

    public User() {
        this.role = ROLE_USER;
        this.isBlocked = false;
    }

    public User(String id, String email, String displayName, String role, boolean isBlocked) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.role = role != null ? role : ROLE_USER;
        this.isBlocked = isBlocked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isAdmin() {
        return ROLE_ADMIN.equalsIgnoreCase(this.role);
    }
}
