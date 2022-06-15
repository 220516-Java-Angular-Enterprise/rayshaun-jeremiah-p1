package com.revature.reimburse.DTOs.Request;

import com.revature.reimburse.models.Reimbursements;

import java.sql.Timestamp;

public class ResolveRequest {
    private String request_id;
    private Reimbursements.Status status;

    public ResolveRequest() {}

    public ResolveRequest(String request_id, Reimbursements.Status s){
        this.request_id = request_id;
        this.status = s;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public Reimbursements.Status getStatus() {
        return status;
    }

    public void setStatus(Reimbursements.Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Update_Request {\n"+
                "reimb_id='%s',\n" +
                "status='%s'\n}",
                request_id, status);
    }
}