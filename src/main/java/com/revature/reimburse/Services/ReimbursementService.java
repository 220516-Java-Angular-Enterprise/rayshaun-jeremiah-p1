package com.revature.reimburse.Services;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.revature.reimburse.DAOs.ReimbursementDAO;
import com.revature.reimburse.models.Reimbursements;
import com.revature.reimburse.models.Reimbursements.Status;
import com.revature.reimburse.models.Users;
import com.revature.reimburse.util.CustomException.InvalidSQLException;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class ReimbursementService {
    private static final Logger logger = Logger.getLogger(ReimbursementService.class.getName());
    private final ReimbursementDAO mReimbDAO;

    public ReimbursementService(ReimbursementDAO rDAO) { mReimbDAO = rDAO; }

    public void createRequest(Reimbursements r) {
        try {
            mReimbDAO.save(r);
        } catch(SQLException se) {
            logger.fine("SQL Update Issue: "+se.getMessage()+
                    "\nTrace: "+ ExceptionUtils.getStackTrace(se));
        }
    }

    public void approveRequest(Reimbursements r, Users resolver) {
        try {
            mReimbDAO.updateStatus(r, Status.APPROVED, resolver);
        } catch (SQLException se) {
            logger.fine("SQL Update Issue: "+se.getMessage()+
                    "\nTrace: "+ ExceptionUtils.getStackTrace(se));
        }
    }

    public void denyRequest(Reimbursements r, Users resolver) {
        try {
            mReimbDAO.updateStatus(r, Status.DENIED, resolver);
        }  catch (SQLException se) {
            logger.fine("SQL Update Issue: "+se.getMessage()+
                    "\nTrace: "+ ExceptionUtils.getStackTrace(se));
        }
    }

    public List<Reimbursements> getAllReimbursements() throws InvalidSQLException {
        try {
            return mReimbDAO.getAll();
        }  catch (SQLException se) {
            logger.info("Failed to gather all reimbursements");
            throw new InvalidSQLException("Reimbursements Table\n"+se.getMessage(), se);
        }
    }

    public List<Reimbursements> getAllReimbursements(Users author) throws InvalidSQLException {
        try {
            return mReimbDAO.getAllForUser(author.getUserID());
        } catch (SQLException se) {
            logger.info("Failed to gather reimbursement for "+author.getUserID());
            throw new InvalidSQLException("Reimbursements Table\n"+se.getMessage(), se);
        }
    }
}
