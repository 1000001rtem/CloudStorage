package com.cloud.storage.common.server.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBHelper {
    private Connection connection;
    public static final String ID_COLUMN = "id";

    public static final String FILE_TABLE_NAME = "file_table";
    public static final String USER_ID = "user_id";
    public static final String FILE_NAME = "file_name";
    public static final String CHECKSUM = "checksum";
    public static final String FILE_PATH = "path";
    public static final String FILE_SIZE = "size";

    public static final String USER_TABLE_NAME = "user_table";
    public static final String NICKNAME_COLUMN = "nickname";
    public static final String LOGIN_COLUMN = "login";
    public static final String PASSWORD_COLUMN = "password";


    protected Connection connect() {
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

    public void disconnectDb() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
