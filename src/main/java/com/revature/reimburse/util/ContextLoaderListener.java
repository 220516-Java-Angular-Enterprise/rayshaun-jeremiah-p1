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


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ContextLoaderListener implements ServletContextListener {
    static {
        try {
            FileInputStream logConfig = new FileInputStream("src/main/resources/log.properties");
            LogManager.getLogManager().readConfiguration(logConfig);
        } catch (IOException e) {
            System.err.println("[WARNING]: Could not open log configuration file. Logging not configured.");
        }
    }

    private static final Logger logger = Logger.getLogger(RSA.class.getName());
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
            logger.fine("Error on context initialization. "+kce.getMessage()+
                    "\nTrace: "+ ExceptionUtils.getStackTrace(kce));
        }
        logger.info("Initialization successful.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("\nShutting down Employee Reimbursement System");
    }
}
