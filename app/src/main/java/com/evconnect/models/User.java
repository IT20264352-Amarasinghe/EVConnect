package com.evconnect.models;

public class User {
    private String nic;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String status;

    // Constructor
    public User(String nic, String name, String email, String phone, String role, String status) {
        this.nic = nic;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = status;
    }

    // Getters
    public String getNic() { return nic; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }
    public String getStatus() { return status; }
}

