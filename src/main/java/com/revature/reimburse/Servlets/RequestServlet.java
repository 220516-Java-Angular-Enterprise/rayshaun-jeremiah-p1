package com.revature.reimburse.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.reimburse.DTOs.Request.NewReimbursementRequest;
import com.revature.reimburse.DTOs.Request.NewUserRequest;
import com.revature.reimburse.DTOs.Request.ResolveRequest;
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


            if (requester.getRole().equals(Users.Roles.EMPLOYEE)) {
                NewReimbursementRequest reimburseReq = mapper.readValue(req.getInputStream(),NewReimbursementRequest.class);
                Reimbursements request = reimburseReq.takeReimbursement();
                request.setAuthor_id(requester.getId());
                reimbursementService.createRequest(request);
                resp.setContentType("application/json");
                resp.getWriter().write(mapper.writeValueAsString(request.getReimb_id()));
                resp.setStatus(200);
                return;


            }
            else resp.setStatus(403);

        } catch (InvalidRequestException e) {
            resp.setStatus(404);
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
                    reimbursementService.getAllReimbursements(requester.getId());
            resp.setContentType("application/json");
            if(reimbursements.isEmpty()) resp.getWriter().write("<h1>No current reimbursement requests</h1>");
            else resp.getWriter().write(mapper.writeValueAsString(reimbursements));
        } catch (InvalidSQLException e) {
            logger.warning(ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            PrincipalNS requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));


            if (!requester.getRole().equals(Users.Roles.FINANCE_MANAGER)) {
                resp.setStatus(403);
                return;
            }
            ResolveRequest resolveReq = mapper.readValue(req.getInputStream(), ResolveRequest.class);
            reimbursementService.resolveRequest(resolveReq.getRequest_id(), resolveReq.getStatus(), requester.getId());
            resp.setContentType("application/json");
            resp.getWriter().write(mapper.writeValueAsString(resolveReq));
            resp.setStatus(200);

        }
        catch(InvalidSQLException e){

            e.printStackTrace();
        }
    }
}
