package com.revature.reimburse.Services;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import com.revature.reimburse.DAOs.ReimbursementDAO;
import com.revature.reimburse.DTOs.Request.NewReimbursementRequest;
import com.revature.reimburse.models.Reimbursements;
import com.revature.reimburse.models.Reimbursements.Status;
import com.revature.reimburse.models.Users;
import com.revature.reimburse.util.CustomException.AuthenticationException;
import com.revature.reimburse.util.CustomException.InvalidRequestException;
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
            logger.warning("SQL Update Issue: "+se.getMessage()+
                    "\nTrace: "+ ExceptionUtils.getStackTrace(se));
        }
    }
    public Reimbursements createRequest(NewReimbursementRequest r) throws SQLException {
        Reimbursements request = r.takeReimbursement();

        mReimbDAO.save(request);
        return request;

    }

    public void resolveRequest(String r_ID, Status s, String resolver_id) {
        try {
            mReimbDAO.updateStatus(r_ID, s.name(), resolver_id);
        } catch (SQLException se) {
            logger.warning("SQL Update Issue: "+se.getMessage()+
                    "\nTrace: "+ ExceptionUtils.getStackTrace(se));
        }
    }

    public Reimbursements resolveReq(NewReimbursementRequest r) throws SQLException {

            Reimbursements reimbursements = mReimbDAO.getByID(r.getReimb_id());
            if (reimbursements == null) throw new AuthenticationException("Reimbursement does not exist");
            mReimbDAO.update(reimbursements);
            return reimbursements;

    }


    public List<Reimbursements> getAllReimbursements() throws InvalidSQLException {
        try {
            return mReimbDAO.getAll();
        }  catch (SQLException se) {
            logger.info("Failed to gather all reimbursements");
            throw new InvalidSQLException("Reimbursements Table\n"+se.getMessage(), se);
        }
    }

    public List<Reimbursements> getAllReimbursements(String authorID) throws InvalidSQLException {
        try {
            return mReimbDAO.getAllForUser(authorID);
        } catch (SQLException se) {
            logger.info("Failed to gather reimbursement for "+authorID);
            throw new InvalidSQLException("Reimbursements Table\n"+se.getMessage(), se);
        }
    }
}
