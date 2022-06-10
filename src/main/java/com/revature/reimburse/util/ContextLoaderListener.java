package com.revature.reimburse.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.reimburse.DAOs.UsersDAO;
import com.revature.reimburse.Services.TokenService;
import com.revature.reimburse.Services.UserService;
import com.revature.reimburse.Servlets.AuthServlet;
import com.revature.reimburse.Servlets.UserServlet;
import com.revature.reimburse.util.Security.RSA;
//import com.revature.reimburse.Services.UserService;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
       System.out.println("\nInitializing Reimbursement Application");

        ObjectMapper mapper = new ObjectMapper();

        UserServlet userServlet = new UserServlet(mapper, new UserService(new UsersDAO(), RSA.getKey()), new TokenService(new JwtConfig()));
        AuthServlet authServlet = new AuthServlet(mapper, new UserService(new UsersDAO(), RSA.getKey()), new TokenService(new JwtConfig()));

        ServletContext context = sce.getServletContext();
        context.addServlet("UserServlet", userServlet).addMapping("/users/*");
        context.addServlet("AuthServlet", authServlet).addMapping("/auth");
        System.out.println("\nInitializing Employee Reimbursement System");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("\nShutting down Reimbursement Application");
        System.out.println("\nShutting down Employee Reimbursement System");
    }
}
