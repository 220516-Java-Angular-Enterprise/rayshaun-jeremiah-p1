package com.revature.reimburse.util.database;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static DatabaseConnection connector;
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException ce) {
            logger.warning(ExceptionUtils.getStackTrace(ce));
        }
    }
    private static final Properties prop = new Properties();

    private DatabaseConnection() {
        try {
            prop.load(new FileReader("C:\\Users\\14844\\Desktop\\OfficialP1\\rayshaun-jeremiah-p1\\src\\main\\resources\\database\\db.properties"));
        } catch(IOException ioe) {
            logger.warning(ExceptionUtils.getStackTrace(ioe));
        }
    }

    public static DatabaseConnection getInstance() {
        if (connector == null) connector = new DatabaseConnection();
        return connector;
    }

    public Connection getCon() throws SQLException {
        Connection con = DriverManager.getConnection(prop.getProperty("url"),
                prop.getProperty("username"), prop.getProperty("password"));
        if (con == null) throw new RuntimeException("[SEVERE]: Could not establish connection with database.");
        con.setSchema("ers");
        return con;
    }
}
