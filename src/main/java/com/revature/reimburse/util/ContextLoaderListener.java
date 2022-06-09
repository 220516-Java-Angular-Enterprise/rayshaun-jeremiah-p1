package com.revature.reimburse.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.reimburse.DAOs.UsersDAO;
import com.revature.reimburse.Services.TokenService;
import com.revature.reimburse.Servlets.AuthServlet;
import com.revature.reimburse.Servlets.UserServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
       System.out.println("\nInitializing Reimbursement Application");

        ObjectMapper mapper = new ObjectMapper();

        UserServlet userServlet = new UserServlet(mapper, new com.revature.reimburse.services.UserService(new UsersDAO()), new TokenService(new JwtConfig()));
        AuthServlet authServlet = new AuthServlet(mapper, new com.revature.reimburse.services.UserService(new UsersDAO()), new TokenService(new JwtConfig()));

        ServletContext context = sce.getServletContext();
        context.addServlet("UserServlet", userServlet).addMapping("/users/*");
        context.addServlet("AuthServlet", authServlet).addMapping("/auth");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("\nShutting down Reimbursement Application");
    }
}
