package com.revature.reimburse.DTOs.Request;

import com.revature.reimburse.models.Reimbursements;
import com.revature.reimburse.models.Users;

import java.sql.Timestamp;
import java.util.Date;

public class NewReimbursementRequest {
    enum Status{
        PENDING, APPROVED, DENIED
    }
    enum Type{ LODGING, TRAVEL, FOOD, OTHER}

  private String reimb_id;
  private Double amount;
  private String description;
  private Type type;
  private Status status = Status.PENDING;
  private String email;
  private Timestamp submitted;
  private String resolver_id;

   public NewReimbursementRequest(){
        super();
    }

   public NewReimbursementRequest(String reimb_id, Double amount, String description, Type type,Status status, String email,Timestamp submitted){
        this.reimb_id = reimb_id;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.status = status;
        this.email = email;
        this.submitted = submitted;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getReimb_id() {
        return reimb_id;
    }

    public void setReimb_id(String reimb_id) {
        this.reimb_id = reimb_id;
    }

    public String getResolver_id() {
        return resolver_id;
    }

    public void setResolver_id(String resolver_id) {
        this.resolver_id = resolver_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Timestamp getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Timestamp submitted) {
        this.submitted = submitted;
    }

    @Override
    public String toString() {
        return "NewReimbursementRequest{" +
                "reimb_id='" + reimb_id + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", email='" + email + '\'' +
                ", submitted=" + submitted +
                ", resolver_id='" + resolver_id + '\'' +
                '}';
    }
}
