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
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class RequestServlet extends HttpServlet {
    private final static Logger logger = Logger.getLogger(RequestServlet.class.getName());
    private final ObjectMapper mapper;

    private final TokenService tokenService;
    private final ReimbursementService reimbursementService;

    public RequestServlet(ObjectMapper mapper, ReimbursementService reimbursementService,TokenService tokenService) {
        this.mapper = mapper;

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
            String token = tokenService.generateToken(requester);
            String[] uris = req.getRequestURI().split("/");
            if (requester.getRole().equals("EMPLOYEE")) {
                resp.setStatus(200);

                    //PrincipalNS empRequest = tokenService.extractRequesterDetails(req.getHeader("Authorization"));
                    List<Reimbursements> reimbursements = reimbursementService.getAllReimbursements(user);
                    resp.setHeader("Authorization", token);
                    resp.getWriter().write(mapper.writeValueAsString(reimbursements));
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
            resp.setStatus(201);
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
        if (requester.getRole().equals(Users.Roles.ADMIN)) {
            resp.setStatus(403);
            return;
        }

        List<Reimbursements> reimbursements = null;
        try {
            reimbursements = requester.getRole().equals(Users.Roles.FINANCE_MANAGER) ?
                    reimbursementService.getAllReimbursements() :
                    reimbursementService.getAllReimbursements(new Users(requester.getId(), requester.getUsername(), requester.getRole()));
            resp.setContentType("application/json");
            if(reimbursements.isEmpty()) resp.getWriter().write("<h1>No current reimbursement requests</h1>");
            else resp.getWriter().write(mapper.writeValueAsString(reimbursements));
        } catch (InvalidSQLException e) {
            logger.warning(ExceptionUtils.getStackTrace(e));
        }







    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try{
            PrincipalNS requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));


            if(!requester.getRole().equals("FINANCE_MANAGER")){
                resp.setStatus(403);
                return;
            }
            resp.setStatus(200);
            //String token = tokenService.generateToken(requester);
            NewReimbursementRequest request = mapper.readValue(req.getInputStream(),NewReimbursementRequest.class);
            Reimbursements reimbursement = reimbursementService.resolveReq(request);
            reimbursementService.confirmRequest(reimbursement,requester.getUsername());

        }
        catch(InvalidSQLException e){

            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
