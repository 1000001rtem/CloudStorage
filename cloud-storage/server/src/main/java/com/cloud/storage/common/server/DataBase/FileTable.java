package com.cloud.storage.common.server.DataBase;

import com.cloud.storage.common.server.MyFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FileTable extends DBHelper {
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
        this.connection = connect();
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        try {
            statement = connection.prepareStatement(CREATE_FILE_TABLE);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createNewFile(MyFile file, int userId) {
        try { //: todo: добавить проверку на существование
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
        }
    }

    @Override
    protected Connection connect() {
        return super.connect();
    }

    @Override
    public void disconnectDb() {
        super.disconnectDb();
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

