package com.revature.reimburse.DAOs;

import com.revature.reimburse.models.Users;
import com.revature.reimburse.util.CustomException.InvalidSQLException;
import com.revature.reimburse.util.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UsersDAO implements CrudDAO<Users>{
    private static final Logger logger = Logger.getLogger(UsersDAO.class.getName());

    @Override
    public void save(Users obj) throws SQLException{
        logger.info("Saving user " + obj.getUserID());
        try (Connection con = DatabaseConnection.getInstance().getCon()){
            PreparedStatement ps = con.prepareStatement("INSERT INTO users (user_id,username,email,password,given_name,surname) VALUES (?,?,?,?,?,?)");
            ps.setString(1, obj.getUserID());
            ps.setString(2, obj.getUsername());
            ps.setString(3, obj.getEmail());
            ps.setString(4, obj.getPassword());
            ps.setString(5, obj.getGivenName());
            ps.setString(6, obj.getSurName());
            ps.executeUpdate();
        } catch(SQLException se) {
            logger.info("Failed trying to save user "+obj.getUserID());
            throw se;
        }
    }

    @Override
    public void update(Users obj) throws SQLException{
        try (Connection con = DatabaseConnection.getInstance().getCon()) {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE users SET\n" +
                            "(username, email, password, given_name, surname, is_active, role_id) =\n" +
                            "(?,?,?,?,?,?,?)\n" +
                            "WHERE user_id = ?");
            ps.setString(1, obj.getUsername());
            ps.setString(2, obj.getEmail());
            ps.setString(3, obj.getPassword());
            ps.setString(4, obj.getGivenName());
            ps.setString(5, obj.getSurName());
            ps.setBoolean(6, obj.getActive());
            ps.setString(7, obj.getRoles().name());
            ps.setString(8, obj.getUserID());
            logger.info("Attempting to save user "+obj.getUserID());
            ps.executeUpdate();
        } catch(SQLException se) {
            logger.info("Failed to save user "+obj.getUserID());
            throw se;
        }

    }

    @Override
    public void delete(Users obj) throws SQLException {
        try  (Connection con = DatabaseConnection.getInstance().getCon()) {
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
        try  (Connection con = DatabaseConnection.getInstance().getCon()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE (user_id) = (?)");
            ps.setString(1, id);
            ps.executeUpdate();
            ResultSet rs = ps.getResultSet();
            logger.info("Gathering user " + id);
            if (rs.next()) {
                return getObject(rs);
            }
        } catch(SQLException se) {
            logger.info("Failed to retrieve user "+id);
            throw se;
        }
        return  null;
    }

    @Override
    public Users getObject(ResultSet rs) throws SQLException {
        try (Connection con = DatabaseConnection.getInstance().getCon()) {
            Users user = new Users();

            user.setUserID(rs.getString("user_id"));
            logger.info("Creating user "+user.getUserID());
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setGivenName(rs.getString("given_name"));
            user.setSurName(rs.getString("surname"));
            user.setActive(rs.getBoolean("is_active"));
            user.setRoles(Users.Roles.valueOf(rs.getString("role_id")));
            return user;
        } catch(SQLException se) {
            logger.info("Failed to create user");
            throw se;
        }
    }


    @Override
    public List<Users> getAll() throws SQLException {
        try (Connection con = DatabaseConnection.getInstance().getCon()) {
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

    public boolean doesUserExist(String name) {
        ResultSet rs = null;

        try (Connection con = DatabaseConnection.getInstance().getCon();
             PreparedStatement stmt = con.prepareStatement(
                "SELECT * FROM users WHERE username = ?;");
        ) {
           stmt.setString(1, name);
           rs = stmt.executeQuery();
           logger.info("Querying for "+name);
           if(rs.next() && name.equals(rs.getString("username")))
               return true;
        } catch(SQLException ignore) {}
        finally {
            try {if(rs != null) rs.close();}
            catch(SQLException ignore){}
        }
        return false;

    }

    public boolean doesEmailExist(String mail) throws SQLException {
        ResultSet rs = null;

        try (Connection con = DatabaseConnection.getInstance().getCon();
             PreparedStatement stmt = con.prepareStatement(
                "SELECT * FROM users WHERE email = ?;")
        ) {
            stmt.setString(1, mail);
            rs = stmt.executeQuery();
            if(rs.next() && mail.equals(rs.getString("email")))
                return true;
        } finally {
            if(rs != null) rs.close();
        }
        return false;
    }

    public Users getUserByUsernameAndPassword(String username, String password) throws InvalidSQLException{
        try(Connection con = DatabaseConnection.getInstance().getCon()){
            logger.info("Querying for user "+username+
                    "\nPassword seen as "+password.substring(0,10)+"...");
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            ps.setString(1,username);
            ps.setString(2,password);

            ResultSet rs = ps.executeQuery();


            if (rs.next()){
                return getObject(rs);
            }
            else logger.info("No users retrieved");
        }
         catch (SQLException e) {
            throw new InvalidSQLException("Error querying users table. "+e.getMessage());
        }

        return null;
    }
}
