package com.revature.reimburse.DTOs.Request;

import com.revature.reimburse.models.Users;
import com.revature.reimburse.models.Users.Roles;

public class NewUserRequest {


    private String username;
    private String email;
    private String password;
    private String givenName;
    private String surName;

    public NewUserRequest() {super();}

    public NewUserRequest(String uName, String eMail, String pWord, String fName, String sName){
        username = uName;
        password = pWord;
        email = eMail;
        givenName = fName;
        surName = sName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Users takeUser(){return new Users(username, password, email, givenName, surName);}
    @Override
    public String toString() {
        return String.format("NewUserRequest{" +
                "username='%s', email='%s', password='******', full_name='%s'}",
                username, email, givenName.concat(surName));
    }
}
