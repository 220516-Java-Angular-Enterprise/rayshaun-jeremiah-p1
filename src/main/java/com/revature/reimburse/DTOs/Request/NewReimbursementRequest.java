package com.revature.reimburse.DTOs.Request;

import com.revature.reimburse.models.Reimbursements;
import com.revature.reimburse.models.Users;

import java.sql.Timestamp;
import java.util.Date;

public class NewReimbursementRequest {


    private String author_id;
    private String reimb_id;
    private Double amount;
    private String description;
    private Reimbursements.Type type;
    private Reimbursements.Status status;
    private String email;
    private Timestamp submitted;


   public NewReimbursementRequest(){
        super();
    }



   public NewReimbursementRequest(Double amount, String description, Reimbursements.Type type){
        this.amount = amount;
        this.description = description;
        this.type = type;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Reimbursements.Status getStatus() {
        return status;
    }

    public void setStatus(Reimbursements.Status status) {
        this.status = status;
    }

    public String getReimb_id() {
        return reimb_id;
    }

    public void setReimb_id(String reimb_id) {
        this.reimb_id = reimb_id;
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

    public Reimbursements.Type getType() {
        return type;
    }

    public void setType(Reimbursements.Type type) {
        this.type = type;
    }

    public Timestamp getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Timestamp submitted) {
        this.submitted = submitted;
    }

    public Reimbursements takeReimbursement(){
       return new Reimbursements(amount,description,type);}

    @Override
    public String toString() {
        return "NewReimbursementRequest{" +
                "author_id='" + author_id + '\'' +
                ", reimb_id='" + reimb_id + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", submitted=" + submitted +
                '}';
    }
}
