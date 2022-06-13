package com.revature.reimburse.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.reimburse.DTOs.Request.LogInRequest;
import com.revature.reimburse.DTOs.Request.NewReimbursementRequest;
import com.revature.reimburse.DTOs.Request.NewUserRequest;
import com.revature.reimburse.DTOs.responses.PrincipalNS;
import com.revature.reimburse.Services.TokenService;
import com.revature.reimburse.Services.UserService;
import com.revature.reimburse.Services.UserService;
import com.revature.reimburse.util.CustomException.InvalidRequestException;
import com.revature.reimburse.util.annotations.Inject;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserServlet extends HttpServlet {
    @Inject
    private final ObjectMapper mMapper;
    private final UserService mUserService;
    private final TokenService tokenService;


    @Inject
    public UserServlet(ObjectMapper mapper, UserService userService, TokenService tokenService) {
        this.mMapper = mapper;
        this.mUserService = userService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Request a reimbursement


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }


}
