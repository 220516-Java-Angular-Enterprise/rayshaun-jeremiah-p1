package com.revature.reimburse.Services;

import com.revature.reimburse.DAOs.UsersDAO;
import com.revature.reimburse.DTOs.Request.LogInRequest;
import com.revature.reimburse.DTOs.Request.NewUserRequest;
import com.revature.reimburse.models.Users;
import com.revature.reimburse.util.CustomException.*;
import com.revature.reimburse.util.Security.RSA;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    private final UsersDAO mUserDAO;
    private final RSA mKey;

    public UserService(UsersDAO uDAO, RSA key) {
        mUserDAO = uDAO;
        mKey = key;
    }


    public Users register(NewUserRequest req)
            throws InvalidRequestException, DuplicateInputException {
        Users u = req.takeUser();
        try {
            logger.info("Received request for new user "+u.getUserID());
            if (!isDuplicateUsername(u.getUsername()) && isValidUsername(u.getUsername()) &&
                    isValidPassword(u.getPassword()) &&
                    !isDuplicateEmail(u.getEmail()) && isValidEmail(u.getEmail())) {
                assert (u.getGivenName().isEmpty());
                assert (u.getSurName().isEmpty());
                logger.info("Input validation complete.");
                u.setPassword(mKey.encrypt(u.getPassword()));
                mUserDAO.save(u);
                logger.info("New user saved to system.");
                return u;
            }
        } catch(DuplicateInputException die) {
            logger.warning("Duplicate information exists.");
            throw die;
        } catch (InvalidInputException ie) {
            logger.warning("Error with user input");
            throw new InvalidRequestException(ie.getMessage());
        }catch(AssertionError err) {
            logger.warning("Names are empty "+ExceptionUtils.getStackTrace(err));
        }  catch (SQLException se){
            logger.warning("SQL Issue. "+se.getMessage()+" Trace:\n"+ ExceptionUtils.getStackTrace(se));
        }

        return null;
    }

    public Users checkUserValid(LogInRequest request){
        if(!isValidUsername(request.getUsername())|| !isValidPassword(request.getPassword())) throw new InvalidRequestException("Invalid username or password");
        Users user = mUserDAO.getUserByUsernameAndPassword(request.getUsername(),mKey.encrypt(request.getPassword()));
        if(user == null) throw new AuthenticationException("Invalid credentials");
        return user;
    }

    public boolean isValidUsername(String name) throws InvalidInputException {
        logger.info("Checking username validity");
        if(name.matches("^(?!(\\d|_))\\w{8,30}")) return true;
        throw new InvalidInputException("username");
    }



    public boolean isDuplicateUsername(String name)
            throws DuplicateInputException {
        logger.info("Checking for existing username");
        if(mUserDAO.doesUserExist(name)) throw new DuplicateInputException("username");
        return false;
    }



    public boolean isValidEmail(String mail) throws InvalidInputException {
        logger.info("Email written in proper format?");
        if(mail.matches("^(?![@.-_]{2})(?!.*@.*@.*)[\\w.-]+@[\\w.-]+\\.[a-z]{2,3}$")) return true;
        throw new InvalidInputException("email");
    }

    public boolean isDuplicateEmail(String mail) throws DuplicateInputException, SQLException {
        logger.info("Checking for duplicate email.");
        if(mUserDAO.doesEmailExist(mail)) throw new DuplicateInputException("email");
        return false;
    }

    public boolean isValidPassword(String pass) throws InvalidInputException {
        logger.info("Valid plain text password?");
        if(pass.matches("^(?=.*\\d+)(?=.*[a-zA-Z]+)(?!.*(.)\\1\\1).{8,}")) return true;
        throw new InvalidInputException("password");
    }



    public void createUser(Users u) throws SQLException {
        u.setPassword(mKey.encrypt(u.getPassword()));
        mUserDAO.save(u);
    }

    public void updateUser(Users u) throws SQLException {
        u.setPassword(mKey.encrypt(u.getPassword()));
        mUserDAO.update(u);
    }
}
