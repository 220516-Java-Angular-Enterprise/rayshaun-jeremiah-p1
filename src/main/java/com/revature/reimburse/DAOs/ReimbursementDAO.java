package com.revature.reimburse.DAOs;

import com.revature.reimburse.models.Reimbursements;
import com.revature.reimburse.models.Users;
import com.revature.reimburse.util.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ReimbursementDAO implements CrudDAO<Reimbursements> {
    private static final Logger logger = Logger.getLogger(ReimbursementDAO.class.getName());
    @Override
    public void save(Reimbursements obj) throws SQLException {
        try (Connection con = DatabaseConnection.getInstance().getCon()){
            PreparedStatement ps = con.prepareStatement("INSERT INTO reimbursements (reimb_id,amount,description,author_id, type_id) VALUES (?,?,?,?,?::\"reimb_type\")");
            ps.setString(1, obj.getReimb_id());
            ps.setDouble(2, obj.getAmount());
            ps.setString(3, obj.getDescription());
            ps.setString(4, obj.getAuthor_id());
            ps.setString(5, String.format("%s",obj.getType().name()));
            ps.executeUpdate();
            logger.info("Updated Reimbursement table with object: "+obj.toString());
        } catch(SQLException se) {
            logger.info("Save to Reimbursement Table failed");
            throw se;
        }
    }


    @Override
    public void update(Reimbursements obj) throws SQLException  {
        try (Connection con = DatabaseConnection.getInstance().getCon()){
            PreparedStatement ps = con.prepareStatement("UPDATE reimbursements SET (amount,submitted,resolved,description,payment_id,author_id,resolver_id,status_id,type_id) = (?,?,?,?,?,?,?,?,?) WHERE reimb_id = ?::\"reimb_type\") ");
            ps.setDouble(1, obj.getAmount());
            ps.setTimestamp(2, obj.getSubmitted());
            ps.setTimestamp(3, obj.getResolved());
            ps.setString(4, obj.getDescription());
            // Not set yet: ps.setBlob(5,obj.getReciept());
            ps.setString(5, obj.getPayment_id());
            ps.setString(6, obj.getAuthor_id());
            ps.setString(7, obj.getResolver_id());
            ps.setString(8, obj.getStatus().name());
            ps.setString(9, obj.getType().name());
            //ps.setString();
            ps.executeQuery();
            logger.info("Updated Reimbursement Table where reimb_id = " + obj.getType().name());
        } catch(SQLException se) {
            logger.info("Failed to update Reimbursement Table at row "+obj.getType().name());
            throw se;
        }
    }

    @Override
    public void delete(Reimbursements obj) throws SQLException {

    }

    @Override
    public Reimbursements getByID(String id) throws SQLException {
        Connection con = DatabaseConnection.getInstance().getCon();

        //TODO
        Reimbursements reimbursement = new Reimbursements();

        PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursements WHERE reimb_id = ?::\"reimb_type\") ");
        ps.setString(1,id);
        ps.executeUpdate();
        ResultSet rs = ps.getResultSet();
        if(!rs.next()){
            //NoDataException();

        }

        return getObject(rs);

    }

    @Override
    public Reimbursements getObject(ResultSet rs) throws SQLException {
        try {
            Reimbursements reimburse = new Reimbursements();
            String status = (rs.getString("status_id"));
            Reimbursements.Status strStatus = Reimbursements.Status.valueOf(status);
            String type = rs.getString("type_id");
            Reimbursements.Type strType = Reimbursements.Type.valueOf(type);


            reimburse.setReimb_id(rs.getString("reimb_id"));
            reimburse.setAmount(rs.getDouble("amount"));
            reimburse.setSubmitted(rs.getTimestamp("submitted"));
            reimburse.setResolved(rs.getTimestamp("resolved"));
            reimburse.setDescription(rs.getString("description"));
            reimburse.setReciept(rs.getInt("receipt"));
            reimburse.setPayment_id(rs.getString("payment_id"));
            reimburse.setAuthor_id(rs.getString("author_id"));
            reimburse.setResolver_id(rs.getString("resolver_id"));
            reimburse.setStatus(strStatus);
            reimburse.setType(strType);

            logger.info("Creating Reimbursement object "+reimburse.getReimb_id());
            return reimburse;
        } catch(SQLException se) {
            logger.info("SQL Error encountered creating Reimbursement object");
            throw se;
        }
    }


    @Override
    public List<Reimbursements> getAll() throws SQLException{
        List<Reimbursements> reimburse = new ArrayList<>();

        try (Connection con = DatabaseConnection.getInstance().getCon()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursements");
            ResultSet rs = ps.executeQuery();
            logger.info("Retrieving all Reimbursements");
            while (rs.next()) {

                reimburse.add(getObject(rs));

            }
            return reimburse;
        } catch(SQLException se) {
            logger.info("Error encountered gathering Reimbursements");
            throw se;
        }

    }


    public boolean updateStatus(String reqID, String s, String uID) throws SQLException{

        try (Connection con = DatabaseConnection.getInstance().getCon()){
            PreparedStatement ps = con.prepareStatement("UPDATE reimbursements SET (status_id,resolved,resolver_id) = (?::\"reimb_status\",NOW(),?) WHERE reimb_id = ?");
            ps.setString(1, s);
            ps.setString(2, uID);
            ps.setString(3, reqID);
            logger.info("Successfully updated");
            ps.executeUpdate();
            return true;
        } catch(SQLException se) {
            logger.info("Failed to update reimbursements row "+reqID);
            throw se;
        }
    }

    public boolean confirmStatus(Reimbursements r, Reimbursements.Status s, String resolver_id) throws SQLException{

        try (Connection con = DatabaseConnection.getInstance().getCon()){
            PreparedStatement ps = con.prepareStatement("UPDATE reimbursements SET (status_id,resolved,resolver_id) = (?,NOW(),?) WHERE reimb_id = ?)");
            ps.setString(1, s.name());
            //ps.setLong(2,now.getTime());
            ps.setString(2, resolver_id);
            ps.setString(3, r.getReimb_id());
            logger.info("Successfully updated");
            return true;
        } catch(SQLException se) {
            logger.info("Failed to update reimbursements row "+r.getReimb_id());
            throw se;
        }
    }



    //**** This is to get all the reimbursements for the user who is logged in ****
    public List<Reimbursements> getAllForUser(String id) throws SQLException{
        List<Reimbursements> reimburse = new ArrayList<>();

        try (Connection con = DatabaseConnection.getInstance().getCon()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursements WHERE author_id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            logger.info("Fetching all Reimbursements for user " + id);
            while (rs.next()) {

                reimburse.add(getObject(rs));

            }
            return reimburse;
        } catch(SQLException se) {
            logger.info("Reimbursement retrieval failed.");
            throw se;
        }
    }



}
