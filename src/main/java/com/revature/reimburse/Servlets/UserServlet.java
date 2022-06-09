package com.revature.reimburse.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.reimburse.Services.TokenService;
import com.revature.reimburse.services.UserService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserServlet extends HttpServlet {

    private final ObjectMapper mapper;
    private final UserService userService;
    private final TokenService tokenService;

    public UserServlet(ObjectMapper mapper, UserService userService, TokenService tokenService){
        this.mapper = mapper;
        this.userService = userService;
        this.tokenService = tokenService;
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
