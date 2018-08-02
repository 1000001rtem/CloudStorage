package com.cloud.storage.common;

public interface ServiceCommands {
    byte FILE_SIZE_SEPARATE = -9;
    byte REGISTRATION_CODE = -8;
    byte AUTHORIZATION_CODE = -7;
    byte CLOSE_CONNECTION = -6;
    byte SEPARATE_CODE = -5;
    byte DOWNLOAD_FILE_FROM_SERVER = -4;
    byte COMMAND_CODE = -3;
    byte FILE_CODE = -2;
    byte FILE_LIST_CODE = 0;

    byte ERROR = 1;
    byte WRONG_LOG_OR_PASS = 2;
    byte AUTH_SUCCESS = 3;
    byte REG_SUCCESS = 4;
    byte USER_EXIST = 5;
    byte FILE_EXIST = 6;


    byte MD5_CODE_LENGTH = 16;

    int SERVER_PORT = 8189;
}
