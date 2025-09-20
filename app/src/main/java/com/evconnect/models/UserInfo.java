package com.evconnect.models;

public class UserInfo {
    public String name;
    public String nic;
    public String email;
    public String role;

    public UserInfo(String name, String nic, String email, String role) {
        this.name = name;
        this.nic = nic;
        this.email = email;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

