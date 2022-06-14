package com.revature.reimburse.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.reimburse.DTOs.Request.NewUserRequest;
import com.revature.reimburse.DTOs.responses.PrincipalNS;
import com.revature.reimburse.Services.TokenService;
import com.revature.reimburse.Services.UserService;
import com.revature.reimburse.models.Users;
import com.revature.reimburse.util.CustomException.DuplicateInputException;
import com.revature.reimburse.util.CustomException.InvalidRequestException;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class UserServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(UserServlet.class.getName());

    private final ObjectMapper mapper;
    private final UserService userService;

    private final TokenService tokenService;

    public UserServlet(ObjectMapper mapper, UserService userService, TokenService tokenService){
        this.mapper = mapper;

        this.userService = userService;
        this.tokenService = tokenService;
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Initiating post request.");

        try{
            NewUserRequest userRequest = mapper.readValue(req.getInputStream(),NewUserRequest.class);
            String[] uris = req.getRequestURI().split("/");

            if(uris.length == 4 && uris[3].equals("username")){
                PrincipalNS requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));


                if(requester == null){
                    resp.setStatus(401);
                    return;
                }

                if(requester.getRole().equals("EMPLOYEE")){
                    resp.setStatus(200);


                    return;
                }

                if(requester.getRole().equals("ADMIN")){
                    resp.setStatus(200);

                    return;
                }

                if(requester.getRole().equals("FINANCE_MANAGER")){
                    resp.setStatus(200);

                    return;
                }
            }
            logger.info("Requesting to create new user.");
            Users createdUser = userService.register(userRequest);
            resp.setStatus(201);
            resp.setContentType("application/json");
            resp.getWriter().write(mapper.writeValueAsString(createdUser.getUserID()));

        }
        catch (InvalidRequestException e){
            logger.warning(e.getMessage()+ ExceptionUtils.getStackTrace(e));
            resp.setStatus(404);
        }
        catch (DuplicateInputException de) {
            logger.warning(de.getMessage());
            resp.setStatus(409);
        }
        catch(Exception e){
            e.printStackTrace();
            resp.setStatus(500);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("<h1>Users works!</h1>");
    }
}
