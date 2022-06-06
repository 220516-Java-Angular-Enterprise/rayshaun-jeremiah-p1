package com.revature.reimburse.DAOs;

import com.revature.reimburse.models.Reimbursements;
import com.revature.reimburse.models.Users;
import com.revature.reimburse.util.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReimbursementDAO implements CrudDAO<Reimbursements> {

    Connection con = DatabaseConnection.getCon();
    @Override
    public void save(Reimbursements obj) throws SQLException{
        PreparedStatement ps = con.prepareStatement("INSERT INTO reimbursements (reimb_id,amount,description,author_id, type_id) VALUES (?,?,?,?,?)");
         ps.setString(1,obj.getReimb_id());
        ps.setDouble(2,obj.getAmount());
        ps.setString(3,obj.getDescription());
        ps.setString(4, obj.getAuthor_id());
        ps.setString(5,obj.getType().name());
        ps.executeUpdate();

    }

    @Override
    public void update(Reimbursements obj) throws SQLException  {
            PreparedStatement ps = con.prepareStatement("UPDATE reimbursements SET (amount,submitted,resolved,description,payment_id,author_id,resolver_id,status_id,type_id) = (?,?,?,?,?,?,?,?,?) WHERE reimb_id = ? ");
            ps.setDouble(1,obj.getAmount());
            ps.setTimestamp(2,obj.getSubmitted());
            ps.setTimestamp(3,obj.getResolved());
            ps.setString(4,obj.getDescription());
            // Not set yet: ps.setBlob(5,obj.getReciept());
            ps.setString(5,obj.getPayment_id());
            ps.setString(6,obj.getAuthor_id());
            ps.setString(7,obj.getResolver_id());
            ps.setString(8,obj.getStatus().name());
            ps.setString(9,obj.getType().name());
            //ps.setString();
            ps.executeQuery();


    }

    @Override
    public void delete(Reimbursements obj) throws SQLException {

    }

    @Override
    public Reimbursements getByID(String id) throws SQLException {

        //TODO
        Reimbursements reimbursement = new Reimbursements();

        PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursements WHERE reimb_id = ? ");
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


        return reimburse;
    }


    @Override
    public List<Reimbursements> getAll() throws SQLException{
        List<Reimbursements> reimburse = new ArrayList<>();

        PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursements");
        ResultSet rs = ps.executeQuery();

        while(rs.next()){

           reimburse.add(getObject(rs));

        }
        return  reimburse;

    }


    public boolean updateStatus(Reimbursements r, Reimbursements.Status s, Users u) throws SQLException{


        PreparedStatement ps = con.prepareStatement("UPDATE reimbursements SET (status_id,resolved,resolver_id) = (?,NOW(),?) WHERE reimb_id = ?");
        ps.setString(1,s.name());
        //ps.setLong(2,now.getTime());
        ps.setString(2,u.getUserID());
        ps.setString(3,r.getReimb_id());
        return true;
    }


    //**** This is to get all the reimbursements for the user who is logged in ****
    public List<Reimbursements> getAllForUser(String id) throws SQLException{
        List<Reimbursements> reimburse = new ArrayList<>();

        PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursements WHERE author_id = ?");
        ps.setString(1,id);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){

            reimburse.add(getObject(rs));

        }
        return  reimburse;

    }



}
