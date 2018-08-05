package com.cloud.storage.common.server.DataBase;

import com.cloud.storage.common.server.MyFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileTable implements SQLConstants{
    private Connection connection;
    private PreparedStatement statement;

    private static final String CREATE_FILE_TABLE = "CREATE TABLE IF NOT EXISTS " + FILE_TABLE_NAME + " (" +
            ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USER_ID + " INTEGER, " +
            FILE_NAME + " TEXT, " +
            FILE_SIZE + " INTEGER, " +
            CHECKSUM + " TEXT, " +
            FILE_PATH + " TEXT);";

    public FileTable() {
        this.connection = DBHelper.getInstance().getConnection();
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        try {
            statement = connection.prepareStatement(CREATE_FILE_TABLE);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean createNewFile(MyFile file, int userId) {
        if (!isFileExists(file.getFileName(), userId)) {
            try {
                statement = connection.prepareStatement("INSERT INTO " + FILE_TABLE_NAME + "(" +
                        USER_ID + ", " +
                        FILE_NAME + ", " +
                        FILE_SIZE + ", " +
                        CHECKSUM + ", " +
                        FILE_PATH + ") " +
                        "VALUES (?,?,?,?,?);");
                statement.setInt(1, userId);
                statement.setString(2, file.getFileName());
                statement.setInt(3, file.getBytes().length / 1024);
                statement.setString(4, file.getCheckSum());
                statement.setString(5, file.getPath());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    private boolean isFileExists(String fileName, int userId){
        try {
            statement = connection.prepareStatement("SELECT " + FILE_NAME + " FROM " +
                    FILE_TABLE_NAME + " WHERE (" +
                    USER_ID + " = '" + userId + "' );");
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet != null && !resultSet.isClosed()) {
                while (resultSet.next()) {
                    if(resultSet.getString(1).equals(fileName)){
                        return true;
                    }
                }
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}

