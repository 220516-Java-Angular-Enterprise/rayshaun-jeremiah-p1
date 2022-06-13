package com.revature.reimburse.models;


import com.revature.reimburse.DTOs.Request.NewUserRequest;

public class Users {




    public enum Roles{ ADMIN, FINANCE_MANAGER, EMPLOYEE }
    private String userID;
    private String username;
    private String password;
    private String email;
    private String givenName;
    private String surName;
    private Boolean isActive;
    private Roles roles;



   public Users(){
        super();
    }

    public Users(String userID, String username, String password, Roles roles) {
    this.userID = userID;
    this.username = username;
    this.password = password;
    this.roles = roles;
   }

    public Users(String username, String password, Roles roles) {
       this.username = username;
       this.password = password;
       this.roles = roles;
    }

    public Users(String username, String password) {
       this.username = username;
       this.password = password;
    }

    public Users(String userID, String username,String password, String email,
          String givenName, String surName, Boolean isActive, Roles roles)

            
    {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.givenName = givenName;
        this.surName = surName;
        this.isActive = isActive;
        this.roles = roles;

    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }


    @Override
    public String toString() {
        return "Users{" +
                "userID='" + userID + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", givenName='" + givenName + '\'' +
                ", surName='" + surName + '\'' +
                ", isActive=" + isActive +
                ", roles=" + roles +
                '}';
    }
}
