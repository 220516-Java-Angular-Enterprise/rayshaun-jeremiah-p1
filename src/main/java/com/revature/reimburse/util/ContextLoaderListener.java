package com.revature.reimburse.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.reimburse.DAOs.UsersDAO;
import com.revature.reimburse.Services.TokenService;
import com.revature.reimburse.Services.UserService;
import com.revature.reimburse.Servlets.AuthServlet;
import com.revature.reimburse.Servlets.UserServlet;
import com.revature.reimburse.util.CustomException.KeyCreationException;
import com.revature.reimburse.util.Security.RSA;
import org.apache.commons.lang3.exception.ExceptionUtils;
//import com.revature.reimburse.Services.UserService;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.logging.Logger;

public class ContextLoaderListener implements ServletContextListener {
    private static final Logger logger = FileLogger.getLogger(RSA.class.getName());
    public void contextInitialized(ServletContextEvent sce) {
       System.out.println("\nInitializing Reimbursement Application");

        ObjectMapper mapper = new ObjectMapper();

        try {
            UserServlet userServlet = new UserServlet(mapper, new UserService(new UsersDAO(), RSA.getKey()), new TokenService(new JwtConfig()));
            AuthServlet authServlet = new AuthServlet(mapper, new UserService(new UsersDAO(), RSA.getKey()), new TokenService(new JwtConfig()));

            ServletContext context = sce.getServletContext();
            context.addServlet("UserServlet", userServlet).addMapping("/users/*");
            context.addServlet("AuthServlet", authServlet).addMapping("/auth");
        } catch(KeyCreationException kce) {
            logger.info("Error on context initialization. "+kce.getMessage()+
                    "\nTrace: "+ ExceptionUtils.getStackTrace(kce));
        }
        System.out.println("\nInitializing Employee Reimbursement System");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("\nShutting down Employee Reimbursement System");
    }
}
