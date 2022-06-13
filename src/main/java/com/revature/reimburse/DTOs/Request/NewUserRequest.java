package com.revature.reimburse.DTOs.Request;

import com.revature.reimburse.models.Users;

public class NewUserRequest {
    public enum Roles{ADMIN, FINANCE_MANAGER, EMPLOYEE}

    private String username;
    private String password;

    private Roles roles;

    public NewUserRequest() {super();}

    public NewUserRequest(String username, String password, Roles roles){
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Roles getRole() {
        return roles;
    }

    public void setRole(Roles roles) {
        this.roles = roles;
    }

    public Users takeUser(){return new Users(username,password,roles);}
    @Override
    public String toString() {
        return "NewUserRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + roles +
                '}';
    }
}
