package com.revature.reimburse.DAOs;

import com.revature.reimburse.models.Receipts;
import com.revature.reimburse.models.Users;
import com.revature.reimburse.util.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReceiptsDAO {
    Connection con = DatabaseConnection.getCon();

    public String getRecieptByUser(Receipts receipt)throws SQLException {

        PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursements WHERE payment_id = ?");
        ps.setString(1,receipt.getLocation());
        ps.executeUpdate();
        ResultSet rs = ps.getResultSet();
        receipt.setLocation(receipt.getLocation());
        if(!rs.next()){
            //noRecieptException();
        }

        return receipt.getLocation();
    }

    public List<ResultSet> getAllUserReciepts(Users users)throws SQLException{
        List<ResultSet> allReciepts = new ArrayList<>();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM receipt WHERE user_id = ?");
        ps.setString(1,users.getUserID());
        ps.executeUpdate();
        ResultSet rs = ps.getResultSet();
        while(rs.next()){
            allReciepts.add(rs);
        }

        return allReciepts;
    }
}
