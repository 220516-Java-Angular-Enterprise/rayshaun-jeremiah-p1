package com.revature.reimburse.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.reimburse.DAOs.ReimbursementDAO;
import com.revature.reimburse.DAOs.UsersDAO;
import com.revature.reimburse.Services.ReimbursementService;
import com.revature.reimburse.Services.TokenService;
import com.revature.reimburse.Services.UserService;
import com.revature.reimburse.Servlets.AuthServlet;
import com.revature.reimburse.Servlets.RequestServlet;
import com.revature.reimburse.Servlets.UserServlet;
import com.revature.reimburse.util.CustomException.KeyCreationException;
import com.revature.reimburse.util.Security.RSA;
import org.apache.commons.lang3.exception.ExceptionUtils;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ContextLoaderListener implements ServletContextListener {
    private static final Logger logger = Logger.getLogger(RSA.class.getName());
    static {
        try (FileInputStream logConfig = new FileInputStream("src/main/resources/log.properties")) {
            LogManager.getLogManager().readConfiguration(logConfig);
            Logger.getLogger("").addHandler(new FileHandler(
                    String.format("logs/ers-%s.log", new Date(new java.util.Date().getTime())),
                    true
            ));
        } catch(IOException fnf) {
            System.err.println("[WARNING]: Could not open log configuration file. Logging to file not configured.\n" +
                    "Trace: "+ ExceptionUtils.getStackTrace(fnf));
        }
    }

    public void contextInitialized(ServletContextEvent sce) {
       System.out.println("\nInitializing Reimbursement Application");

        ObjectMapper mapper = new ObjectMapper();

        try {
            RSA keys = RSA.getKey();
            UserServlet userServlet = new UserServlet(mapper, new UserService(new UsersDAO(), keys), new TokenService(new JwtConfig()));
            AuthServlet authServlet = new AuthServlet(mapper, new UserService(new UsersDAO(), keys), new TokenService(new JwtConfig()));
            RequestServlet reqServlet = new RequestServlet(mapper, new ReimbursementService(new ReimbursementDAO()),new TokenService(new JwtConfig()));
            ServletContext context = sce.getServletContext();
            context.addServlet("UserServlet", userServlet).addMapping("/users/*");
            context.addServlet("AuthServlet", authServlet).addMapping("/auth");
            context.addServlet("RequestServlet", reqServlet).addMapping("/request");
        } catch(KeyCreationException kce) {
            logger.warning("Error on context initialization. "+kce.getMessage()+
                    "\nTrace: "+ ExceptionUtils.getStackTrace(kce));
        }
        logger.info("\n\nInitialization successful.\n");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("\nShutting down Employee Reimbursement System");
    }
}
