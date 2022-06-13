package com.revature.reimburse.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.reimburse.DTOs.Request.NewReimbursementRequest;
import com.revature.reimburse.DTOs.Request.NewUserRequest;
import com.revature.reimburse.DTOs.responses.PrincipalNS;
import com.revature.reimburse.Services.ReimbursementService;
import com.revature.reimburse.Services.TokenService;
import com.revature.reimburse.Services.UserService;
import com.revature.reimburse.models.Reimbursements;
import com.revature.reimburse.models.Users;
import com.revature.reimburse.util.CustomException.InvalidRequestException;
import com.revature.reimburse.util.CustomException.InvalidSQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

public class RequestServlet extends HttpServlet {

    private final ObjectMapper mapper;
    private final UserService userService;
    private final TokenService tokenService;
    private final ReimbursementService reimbursementService;

    public RequestServlet(ObjectMapper mapper, UserService userService, TokenService tokenService, ReimbursementService reimbursementService) {
        this.mapper = mapper;
        this.userService = userService;
        this.tokenService = tokenService;

        this.reimbursementService = reimbursementService;
    }

    //This is used to create a request
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            PrincipalNS requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));
            Users user = mapper.readValue(req.getInputStream(), Users.class);
            NewReimbursementRequest reimburseReq = mapper.readValue(req.getInputStream(),NewReimbursementRequest.class);
            //Reimbursements request = mapper.readValue(req.getInputStream(), Reimbursements.class);
            String[] uris = req.getRequestURI().split("/");
            if (requester.getRole().equals("EMPLOYEE")) {
                resp.setStatus(200);
                if (uris.length == 5 && uris[4].equals("amount")) {
                    PrincipalNS empRequest = tokenService.extractRequesterDetails(req.getHeader("Authorization"));
                    List<Reimbursements> reimbursements = reimbursementService.getAllReimbursements(user);
                }
                return;
            }

            if (requester.getRole().equals("ADMIN")) {
                return;
            }

            if (requester.getRole().equals("FINANCE_MANAGER")) {
                List<Reimbursements> reimbursement = reimbursementService.getAllReimbursements();
                resp.setContentType("application/json");
                resp.getWriter().write(mapper.writeValueAsString(reimbursement));
                return;
            }

          Reimbursements createdRequest = reimbursementService.createRequest(reimburseReq);

            resp.setContentType("application/json");
            resp.getWriter().write(mapper.writeValueAsString(createdRequest));


        } catch (InvalidRequestException e) {
            resp.setStatus(404);
        } catch (SQLException e) {
            resp.setStatus(500);
            e.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrincipalNS requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));

        if (requester == null) {
            resp.setStatus(401);
            return;
        }
        if (requester.getRole().equals("ADMIN")) {
            resp.setStatus(401);
            return;
        }


        try {
            List<Reimbursements> reimbursements = reimbursementService.getAllReimbursements();
            resp.setContentType("application/json");
            resp.getWriter().write(mapper.writeValueAsString(reimbursements));
        } catch (InvalidSQLException e) {

        }


    }
}
