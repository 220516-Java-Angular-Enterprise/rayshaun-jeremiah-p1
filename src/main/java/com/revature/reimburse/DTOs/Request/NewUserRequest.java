package com.revature.reimburse.DTOs.Request;

import com.revature.reimburse.models.Users;

public class NewUserRequest {
    private String username;
    private String password;

    private Users.Roles role ;

    public NewUserRequest() {super();}

    public NewUserRequest(String username, String password, Users.Roles role){
        this.username = username;
        this.password = password;
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

    public Users.Roles getRole() {
        return role;
    }

    public void setRole(Users.Roles role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "NewUserRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
