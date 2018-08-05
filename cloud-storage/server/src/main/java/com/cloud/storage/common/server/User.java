package com.cloud.storage.common.server;

import com.cloud.storage.common.Directorys;
import com.cloud.storage.common.server.DataBase.UserTable;

import java.sql.SQLException;

public class User implements Directorys {

    private UserTable table;
    private String nickName;
    private String login;
    private String password;
    private String userDirectory;

    public User(String nickName, String login, String password) {
        this.nickName = nickName;
        this.login = login;
        this.password = password;
        this.userDirectory = SERVER_DIRECTORY + "/" + nickName;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.nickName = getNickNameFromDB();
    }

    private String getNickNameFromDB() {
        table = new UserTable();
        return table.getNickName(this.login);
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        this.userDirectory = SERVER_DIRECTORY + "/" + nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getUserDirectory() {
        return userDirectory;
    }

    public void createNewUser() {
        table = new UserTable();
        if (!isUserExists(table)) {
            table.createNewUser(this);
        } else {
            System.out.println("user exist");
        }
    }

    private boolean isUserExists(UserTable table) {
        return table.isUserExists(this.nickName, this.login);
    }

    public boolean authorization() {
        table = new UserTable();
        return table.checkAuth(this.login, this.password);
    }

    public int getId() {
        table = new UserTable();
        return table.getUserId(this.nickName);
    }
}
