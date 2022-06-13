package com.revature.reimburse.DAOs;

import com.revature.reimburse.models.Users;
import com.revature.reimburse.util.CustomException.InvalidSQLException;
import com.revature.reimburse.util.FileLogger;
import com.revature.reimburse.util.database.DatabaseConnection;
import org.postgresql.core.ConnectionFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UsersDAO implements CrudDAO<Users>{
    private static final Logger logger = FileLogger.getLogger(UsersDAO.class.getName());

    Connection con =  DatabaseConnection.getCon();
    @Override
    public void save(Users obj) throws SQLException{
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO users (user_id,username,email,password,given_name,surname,is_active,role_id) VALUES (?,?,?,?,?,?,?,?)");
            ps.setString(1, obj.getUserID());
            ps.setString(2, obj.getUsername());
            ps.setString(3, obj.getEmail());
            ps.setString(4, obj.getPassword());
            ps.setString(5, obj.getGivenName());
            ps.setString(6, obj.getSurName());
            ps.setString(7, obj.getActive().toString());
            ps.setString(8, obj.getRoles().toString());
            logger.info("Saving user " + obj.getUserID());
            ps.executeUpdate();
        } catch(SQLException se) {
            logger.info("Failed trying to save user "+obj.getUserID());
            throw se;
        }
    }

    @Override
    public void update(Users obj) throws SQLException{
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE users SET (username, email, password, give_name, surname, is_active, role_id) VALUES (?,?,?,?,?,?,?)");
            ps.setString(1, obj.getUsername());
            ps.setString(2, obj.getEmail());
            ps.setString(3, obj.getPassword());
            ps.setString(4, obj.getGivenName());
            ps.setString(5, obj.getSurName());
            ps.setString(6, obj.getActive().toString());
            ps.setString(7, obj.getRoles().toString());
            logger.info("Attempting to save user "+obj.getUserID());
            ps.executeUpdate();
        } catch(SQLException se) {
            logger.info("Failed to save user "+obj.getUserID());
            throw se;
        }

    }

    @Override
    public void delete(Users obj) throws SQLException {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE (user_id) = (?) ");
            ps.setString(1, obj.getUserID());
            logger.info("Deleting user "+obj.getUserID());
            ps.executeUpdate();
        } catch(SQLException se) {
            logger.info("Failed to delete user "+obj.getUserID());
            throw se;
        }
    }

    @Override
    public Users getByID(String id) throws SQLException {
        try {
            Users user = new Users();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE (user_id) = (?)");
            ps.setString(1, id);
            ps.executeUpdate();
            ResultSet rs = ps.getResultSet();
            logger.info("Gathering user " + id);
            if (!rs.next()) {
                //noUserDataException();
            }
            return user;
        } catch(SQLException se) {
            logger.info("Failed to retrieve user "+id);
            throw se;
        }
    }

    @Override
    public Users getObject(ResultSet rs) throws SQLException {
        try {
            Users user = new Users();
            String roleString = rs.getString("role_id");
            Users.Roles roleStatus = Users.Roles.valueOf(roleString);


            user.setUserID(rs.getString("user_id:"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setGivenName(rs.getString("given_name"));
            user.setSurName(rs.getString("surname"));
            user.setActive(rs.getBoolean("is_active"));
            user.setRoles(roleStatus);
            logger.info("Creating user "+user.getUserID());
            return user;
        } catch(SQLException se) {
            logger.info("Failed to create user");
            throw se;
        }
    }


    @Override
    public List<Users> getAll() throws SQLException {
        try {
            List<Users> userList = new ArrayList<>();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users");
            ResultSet rs = ps.executeQuery();

            logger.info("Fetching all users");
            while (rs.next()) {
                userList.add(getObject(rs));
            }
            return userList;
        } catch(SQLException se) {
            logger.info("Failed to fetch all users");
            throw se;
        }
    }

    public List<Users> getAllLike() throws SQLException{
        return null;
    }

    public boolean doesUserExist(String name) throws SQLException{
        //check if username is already used
        return true;
    }

    public boolean doesEmailExist(String mail) throws SQLException {
        return true;
    }

    public Users getUserByUsernameAndPassword(String username, String password){
        Users user = null;
        try(Connection con = DatabaseConnection.getCon()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username = ? AND password = crypt(?, password) AND role_id = ?");
            ps.setString(1,username);
            ps.setString(2,password);

            ResultSet rs = ps.executeQuery();


            if (rs.next()){
                user = new Users(rs.getString("username"),rs.getString("password"));
            }
        }
         catch (SQLException e) {
            throw new InvalidSQLException("An error occured when trying to retrive for the database");
        }

        return user;
    }
}
