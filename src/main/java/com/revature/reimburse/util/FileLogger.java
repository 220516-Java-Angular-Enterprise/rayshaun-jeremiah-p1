package com.revature.reimburse.util;

import com.revature.reimburse.util.CustomException.LogCreationFailedException;

import java.io.IOException;
import java.sql.Date;
import java.util.MissingResourceException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FileLogger extends Logger {

    /**
     * Protected method to construct a logger for a named subsystem.
     * <p>
     * The logger will be initially configured with a null Level
     * and with useParentHandlers set to true.
     *
     * @param name               A name for the logger.  This should
     *                           be a dot-separated name and should normally
     *                           be based on the package name or class name
     *                           of the subsystem, such as java.net
     *                           or javax.swing.  It may be null for anonymous Loggers.
     * @param resourceBundleName name of ResourceBundle to be used for localizing
     *                           messages for this logger.  May be null if none
     *                           of the messages require localization.
     * @throws MissingResourceException if the resourceBundleName is non-null and
     *                                  no corresponding resource can be found.
     */
    protected FileLogger(String name, String resourceBundleName) {
        super(name, resourceBundleName);
    }

    public static Logger getLogger(String classPath) throws LogCreationFailedException {
        Logger l = Logger.getLogger(classPath);
        try {
            String logPath = "logs/ers."+new Date(new java.util.Date().getTime())+".log";
            FileHandler fh = new FileHandler(logPath, true);
            fh.setFormatter(new SimpleFormatter());
            fh.setLevel(Level.ALL);
            l.addHandler(fh);
        } catch(IOException | SecurityException e) {
            throw new LogCreationFailedException("Failed to create log");
        }

        return l;
    }
}
