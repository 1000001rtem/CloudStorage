package com.cloud.storage.common.server.DataBase;

import com.cloud.storage.common.server.ServerMainClass;
import com.cloud.storage.common.server.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserTable implements SQLConstants {

    public static final Logger logger = LogManager.getLogger(ServerMainClass.class.getName());

    private Connection connection;
    private PreparedStatement statement;

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME + " (" +
            ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NICKNAME_COLUMN + " TEXT, " +
            LOGIN_COLUMN + " TEXT, " +
            PASSWORD_COLUMN + " INTEGER);";

    public UserTable() {
        this.connection = DBHelper.getInstance().getConnection();
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        try {
            statement = connection.prepareStatement(CREATE_TABLE);
            statement.execute();
        } catch (SQLException e) {
            logger.error("UserTable", e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.error("UserTable", e);
            }
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
            logger.error("UserTable", e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.error("UserTable", e);
            }
        }
    }

    public boolean isUserExists(String nickName, String login) {
        try {
            statement = connection.prepareStatement("SELECT " + NICKNAME_COLUMN + " FROM " +
                    USER_TABLE_NAME + " WHERE (" +
                    LOGIN_COLUMN + " = '" + login + "' );");
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet == null || resultSet.isClosed() || !resultSet.next()) return false;
            else return true;
        } catch (SQLException e) {
            logger.error("UserTable", e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.error("UserTable", e);
            }
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
            logger.error("UserTable", e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.error("UserTable", e);
            }
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
            logger.error("UserTable", e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.error("UserTable", e);
            }
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
            logger.error("UserTable", e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.error("UserTable", e);
            }
        }
        return -1;
    }
}
