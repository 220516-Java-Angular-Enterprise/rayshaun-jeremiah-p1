package com.revature.reimburse.Models;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

enum Status{PENDING, APPROVED, DENIED}

enum Type{LODGING,TRAVEL, FOOD, OTHER}

public class Reimbursements {
    private String reimb_id;
    private Double amount;
    private Timestamp submitted;
    private String description;
    private List<Reimbursements> reciept;
    private String  payment_id;
    private String author_id;
    private String resolver_id;
    private Status status;
    private Type type;

    Reimbursements(){
        super();
    }

    Reimbursements(String reimb_id, Double amount, Timestamp submitted, String description,
                   List<Reimbursements> reciept,String payment_id, String author_id, String resolver_id,Status status, Type type)
    //This is the start of Reimbursments Constructer
    {
     this.reimb_id = reimb_id;
     this.amount = amount;
     this.submitted = submitted;
     this.description = description;
     this.reciept = reciept;
     this.payment_id = payment_id;
     this.author_id = author_id;
     this.resolver_id = resolver_id;
     this.status = status;
     this.type = type;

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

    public Timestamp getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Timestamp submitted) {
        this.submitted = submitted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Reimbursements> getReciept() {
        return reciept;
    }

    public void setReciept(List<Reimbursements> reciept) {
        this.reciept = reciept;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getResolver_id() {
        return resolver_id;
    }

    public void setResolver_id(String resolver_id) {
        this.resolver_id = resolver_id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Reimbursements{" +
                "reimb_id='" + reimb_id + "\n" +
                " amount=" + amount + "\n" +
                " submitted=" + submitted + "\n" +
                " description='" + description + "\n" +
                " reciept=" + reciept + "\n" +
                " payment_id='" + payment_id + "\n" +
                " author_id='" + author_id + "\n" +
                " resolver_id='" + resolver_id + "\n" +
                " status=" + status + "\n" +
                " type=" + type +
                '}';
    }
}
