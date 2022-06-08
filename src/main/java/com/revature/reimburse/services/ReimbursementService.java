package com.revature.reimburse.services;

import java.sql.SQLException;
import java.util.List;

import com.revature.reimburse.DAOs.ReimbursementDAO;
import com.revature.reimburse.models.Reimbursements;
import com.revature.reimburse.models.Reimbursements.Status;
import com.revature.reimburse.models.Users;

public class ReimbursementService {
    private final ReimbursementDAO mReimbDAO;

    public ReimbursementService(ReimbursementDAO rDAO) { mReimbDAO = rDAO; }

    public void createRequest(Reimbursements r) throws SQLException {
        mReimbDAO.save(r);
    }

    public void approveRequest(Reimbursements r, Users resolver) throws SQLException {
        mReimbDAO.updateStatus(r, Status.APPROVED, resolver);
    }

    public void denyRequest(Reimbursements r, Users resolver) throws SQLException {
        mReimbDAO.updateStatus(r, Status.DENIED, resolver);
    }

    public List<Reimbursements> getAllReimbursements() throws SQLException {
        return mReimbDAO.getAll();
    }

    public List<Reimbursements> getAllReimbursements(Users author) throws SQLException {
        return mReimbDAO.getAllForUser(author.getUserID());
    }
}
