package com.revature.reimburse.DTOs.responses;

import com.revature.reimburse.models.Users;

// class is called PrincipalNS because there is already a class called Principal, NS is non security
public class PrincipalNS {

    private String id;
    private String username;
    private String role;

    public PrincipalNS(){
        super();
    }

    public PrincipalNS(String id, String username, String role){
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Principal{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
