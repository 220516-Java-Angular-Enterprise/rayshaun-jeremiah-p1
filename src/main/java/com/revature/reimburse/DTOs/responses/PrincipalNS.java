package com.revature.reimburse.DTOs.responses;

import com.revature.reimburse.DTOs.Request.NewReimbursementRequest;
import com.revature.reimburse.models.Users;
import com.revature.reimburse.models.Users.Roles;

import java.sql.Timestamp;

// class is called PrincipalNS because there is already a class called Principal, NS is non security
public class PrincipalNS {

    private String id;
    private String username;
    private Roles roles;




    public PrincipalNS(){
        super();
    }

    public PrincipalNS(Users user){
        this.id = user.getUserID();
        this.username = user.getUsername();
        this.roles = user.getRoles();
    }

    public PrincipalNS(String id, String username, Roles roles){
        this.id = id;
        this.username = username;
        this.roles = roles;
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

    public Roles getRole() {
        return roles;
    }

    public void setRole(Roles role) {
        this.roles = role;
    }

    @Override
    public String toString() {
        return "Principal{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", role='" + roles + '\'' +
                '}';
    }
}
