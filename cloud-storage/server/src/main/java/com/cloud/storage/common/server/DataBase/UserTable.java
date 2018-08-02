package com.cloud.storage.common.server.DataBase;

import com.cloud.storage.common.server.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserTable extends DBHelper {

    private Connection connection;
    private PreparedStatement statement;

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME + " (" +
            ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NICKNAME_COLUMN + " TEXT, " +
            LOGIN_COLUMN + " TEXT, " +
            PASSWORD_COLUMN + " INTEGER);";

    public UserTable() {
        this.connection = connect();
        createTableIfNotExists();
    }

    @Override
    protected Connection connect() {
        return super.connect();
    }

    private void createTableIfNotExists() {
        try {
            statement = connection.prepareStatement(CREATE_TABLE);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createNewUser(User user) {
        try {
            statement = connection.prepareStatement("INSERT INTO " + USER_TABLE_NAME + "(" +
                    NICKNAME_COLUMN + ", " +
                    LOGIN_COLUMN + ", " +
                    PASSWORD_COLUMN + ") " +
                    "VALUES (?,?,?);");
            statement.setString(1, user.getNickName());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isUserExists(String nickName, String login) {
        try {
            statement = connection.prepareStatement("SELECT " + NICKNAME_COLUMN + " FROM " +
                    USER_TABLE_NAME + " WHERE (" +
                    LOGIN_COLUMN + " = '" + login + "' );");
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.isClosed()) return false;
            else return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkAuth(String login, String password) {
        try {
            statement = connection.prepareStatement("SELECT " + PASSWORD_COLUMN + " FROM " +
                    USER_TABLE_NAME + " WHERE (" +
                    LOGIN_COLUMN + " = '" + login + "' );");
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet != null && !resultSet.isClosed()) {
                String resultPass = resultSet.getString(1);
                if (resultPass.equals(password)) return true;
            } else return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getNickName(String login) {
        try {
            statement = connection.prepareStatement("SELECT " + NICKNAME_COLUMN + " FROM " +
                    USER_TABLE_NAME + " WHERE (" +
                    LOGIN_COLUMN + " = '" + login + "' );");
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            if (resultSet != null && !resultSet.isClosed()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getUserId(String nickName) {
        try {
            statement = connection.prepareStatement("SELECT " + ID_COLUMN + " FROM " +
                    USER_TABLE_NAME + " WHERE (" +
                    NICKNAME_COLUMN + " = '" + nickName + "' );");
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void disconnectDb() {
        super.disconnectDb();
        try {
            this.statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
