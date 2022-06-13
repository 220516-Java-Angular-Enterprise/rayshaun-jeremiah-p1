package com.revature.reimburse.Services;

import com.revature.reimburse.DAOs.UsersDAO;
import com.revature.reimburse.DTOs.Request.LogInRequest;
import com.revature.reimburse.models.Users;
import com.revature.reimburse.util.CustomException.*;
import com.revature.reimburse.util.Security.RSA;

import java.sql.SQLException;

public class UserService {
    private final UsersDAO mUserDAO;
    private final RSA mKey;

    public UserService(UsersDAO uDAO, RSA key) {
        mUserDAO = uDAO;
        mKey = key;
    }

    public Users checkUserValid(LogInRequest request){
        Users user = new Users();
        if(!isValidUsername(request.getUsername())|| !isValidPassword(request.getPassword())) throw new InvalidRequestException("Invalid username or password");
        user = mUserDAO.getUserByUsernameAndPassword(request.getUsername(),request.getPassword());
        if(user == null) throw new AuthenticationException("Invalid credentials");
        return user;
    }

    public boolean isValidUsername(String name) throws InvalidInputException {
        if(name.matches("^(?!(\\d|_))\\w{8,30}")) return true;
        throw new InvalidInputException("username");
    }



    public boolean isDuplicateUsername(String name) throws InvalidInputException, SQLException {
        if(mUserDAO.doesUserExist(name)) throw new DuplicateInputException("username");
        return false;
    }



    public boolean isValidEmail(String mail) throws InvalidInputException {
        if(mail.matches("^(?![@.-]{2})(?!.*@.*@.*)[\\w.-]+@[\\w.-]\\.[a-z]{2,3}")) return true;
        throw new InvalidInputException("email");
    }

    public boolean isDuplicateEmail(String mail) throws DuplicateInputException, SQLException {
        if(mUserDAO.doesEmailExist(mail)) throw new DuplicateInputException("username");
        return false;
    }

    public boolean isValidPassword(String pass) throws InvalidInputException {
        if(pass.matches("^(?=.*\\d+)(?=.*[a-zA-Z]+)(?!.*(.)\\1\\1).{8,}")) return true;
        throw new InvalidInputException("password");
    }



    public void createUser(Users u) throws SQLException {
        u.setPassword(mKey.encrypt(u.getPassword()));
        mUserDAO.save(u);
    }

    public void updateUser(Users u) throws SQLException {
        mUserDAO.update(u);
    }
}
