package com.revature.reimburse.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.reimburse.DTOs.Request.LogInRequest;
import com.revature.reimburse.DTOs.responses.PrincipalNS;
import com.revature.reimburse.Services.TokenService;
import com.revature.reimburse.Services.UserService;
import com.revature.reimburse.util.CustomException.AuthenticationException;
import com.revature.reimburse.util.CustomException.InvalidRequestException;
import com.revature.reimburse.util.Security.RSA;
import org.apache.commons.lang3.exception.ExceptionUtils;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class AuthServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AuthServlet.class.getName());
    private final ObjectMapper mapper;
    private final UserService userService;
    private final TokenService tokenService;

    public AuthServlet(ObjectMapper mapper, UserService userService, TokenService tokenService) {
        this.mapper = mapper;
        this.userService = userService;
        this.tokenService = tokenService;
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        logger.info("Initiating post request.");
        try{
            LogInRequest request = mapper.readValue(req.getInputStream(),LogInRequest.class);
            PrincipalNS principal = new PrincipalNS(userService.checkUserValid(request));

            String token = tokenService.generateToken(principal);
            resp.setHeader("Authorization", token);
            resp.setContentType("application/json");

            logger.info("User validated.");
            resp.getWriter().write(mapper.writeValueAsString(principal));


        }
       catch(InvalidRequestException e){
            logger.warning(e.getMessage()+"\nTrace: "+ ExceptionUtils.getStackTrace(e));
            resp.setStatus(404);

       }
        catch (AuthenticationException e){
            logger.warning(e.getMessage()+"\nTrace: "+ ExceptionUtils.getStackTrace(e));
            resp.setStatus(401);
        }
        catch(Exception e){
            logger.warning(e.getMessage()+"\nTrace: "+ ExceptionUtils.getStackTrace(e));
            resp.setStatus(500);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        resp.getWriter().write("<h1>Authorizaiton test header </h1>");
    }
}
