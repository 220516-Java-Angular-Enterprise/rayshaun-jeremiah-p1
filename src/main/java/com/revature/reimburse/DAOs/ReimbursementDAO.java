package com.revature.reimburse.DAOs;

import com.revature.reimburse.models.Reimbursements;
import com.revature.reimburse.util.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ReimbursementDAO implements CrudDAO<Reimbursements> {

    Connection con = DatabaseConnection.getCon();
    @Override
    public void save(Reimbursements obj) {


    }

    @Override
    public void update(Reimbursements obj) throws SQLException  {
            PreparedStatement ps = con.prepareStatement("UPDATE reimbursements SET (amount,submitted,resolved,description,payment_id,author_id,resolver_id,status_id,type_id) VALUES (?,?,?,?,?,?,?,?,?) WHERE reimb_id = ? ");
            ps.setDouble(1,obj.getAmount());
            ps.setTimestamp(2,obj.getSubmitted());
            ps.setTimestamp(3,obj.getResolved());
            ps.setString(4,obj.getDescription());
            // Not set yet: ps.setBlob(5,obj.getReciept());
            ps.setString(5,obj.getPayment_id());
            ps.setString(6,obj.getAuthor_id());
            ps.setString(7,obj.getResolver_id());
            //ps.setString(8,obj.getStatus());
            //ps.setString();
            ps.executeQuery();


    }

    @Override
    public void delete(Reimbursements obj) {

    }

    @Override
    public Reimbursements getByID(String id) {
        return null;
    }

    @Override
    public Reimbursements getByEmail(String id) {
        return null;
    }

    @Override
    public List<Reimbursements> getAll() {
        return null;
    }
}
