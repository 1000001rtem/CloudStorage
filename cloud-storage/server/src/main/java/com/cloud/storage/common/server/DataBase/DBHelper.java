package com.cloud.storage.common.server.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
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
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public Connection getConnection(){
        return this.connection;
    }

    public void disconnectDb() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
