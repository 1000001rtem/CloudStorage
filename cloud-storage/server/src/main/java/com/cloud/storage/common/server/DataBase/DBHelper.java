package com.cloud.storage.common.server.DataBase;

import com.cloud.storage.common.server.ServerMainClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {

    public static final Logger logger = LogManager.getLogger(ServerMainClass.class.getName());

    private Connection connection;
    private static DBHelper ourInstance = new DBHelper();

    public static DBHelper getInstance() {
        return ourInstance;
    }

    private DBHelper() {
    }

    public Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:serverDB.db");
        } catch (ClassNotFoundException e) {
            logger.error("DataBase", e);
        } catch (SQLException e) {
            logger.error("DataBase", e);
        }

        return connection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void disconnectDb() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("DataBase", e);
        }
    }
}
