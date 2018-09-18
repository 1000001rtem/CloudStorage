package com.cloud.storage.common;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


// Стрктура сообщения с файлом:
// размер сообщения
// разделитель
// служебный байт определяющий тип сообщения
// байты имени файла
// байт разделитель
// 32 байта контрольной суммы
// байты самого файла

// Стрктура сообщения с командой:
// размер сообщения
// разделитель
// служебный байт определяющий тип сообщения
// байт команды

// Стрктура сообщения с авторизацией:
// размер сообщения
// разделитель
// служебный байт определяющий тип сообщения
// байты хеша логина
// байты хеша пароля

// Стрктура сообщения с регистрацией:
// размер сообщения
// разделитель
// служебный байт определяющий тип сообщения
// байты хеша логина
// байты хеша пароля
// байты никнейма

// Стрктура сообщения с запросом на скачивание файла с сервера:
// размер сообщения
// разделитель
// служебный байт определяющий тип сообщения
// байты имени файла

public class MessageEncoder implements ServiceCommands {


    public byte[] getMessage(File file) {
        byte[] fileBytes;
        byte[] nameBytes;
        byte[] checkSumBytes;
        byte[] messageBytes;
        nameBytes = file.getName().getBytes();
        fileBytes = new byte[(int) file.length()];
        readFileBytes(file, fileBytes);
        checkSumBytes = getMD5(fileBytes);
        messageBytes = new byte[nameBytes.length + fileBytes.length + checkSumBytes.length + 3];

        String size = String.valueOf(messageBytes.length);
        messageBytes = addMessageSizeInfo(messageBytes, size);

        messageBytes[size.getBytes().length + 1] = FILE_CODE;
        collectBytes(messageBytes, size.getBytes().length, nameBytes, checkSumBytes, fileBytes);
        return messageBytes;
    }

    public byte[] getMessage(byte command) {
        byte[] messageBytes = new byte[4];
        messageBytes[0] = 50; //ASCII code for "3"
        messageBytes[1] = FILE_SIZE_SEPARATE;
        messageBytes[2] = COMMAND_CODE;
        messageBytes[3] = command;
        return messageBytes;
    }

    public byte[] getMessage(String login, String pass) {
        byte[] messageBytes = new byte[MD5_CODE_LENGTH * 2 + 1];

        String size = String.valueOf(messageBytes.length);
        messageBytes = addMessageSizeInfo(messageBytes, size);

        messageBytes[size.getBytes().length + 1] = AUTHORIZATION_CODE;
        System.arraycopy(getMD5(login.getBytes()), 0, messageBytes, size.getBytes().length + 2, MD5_CODE_LENGTH);
        System.arraycopy(getMD5(pass.getBytes()), 0, messageBytes, MD5_CODE_LENGTH + size.getBytes().length + 2, MD5_CODE_LENGTH);// 2 = 2 служебных байта
        return messageBytes;
    }

    public byte[] getMessage(String nickName, String login, String pass) {
        byte[] messageBytes = new byte[MD5_CODE_LENGTH * 2 + 1 + nickName.getBytes().length];

        String size = String.valueOf(messageBytes.length);
        messageBytes = addMessageSizeInfo(messageBytes, size);

        messageBytes[size.getBytes().length + 1] = REGISTRATION_CODE;
        System.arraycopy(getMD5(login.getBytes()), 0, messageBytes, size.getBytes().length + 2, MD5_CODE_LENGTH);
        System.arraycopy(getMD5(pass.getBytes()), 0, messageBytes, MD5_CODE_LENGTH + size.getBytes().length + 2, MD5_CODE_LENGTH);
        System.arraycopy(nickName.getBytes(), 0, messageBytes, MD5_CODE_LENGTH * 2 + size.getBytes().length + 2, nickName.getBytes().length);
        return messageBytes;
    }

    public byte[] getMessage(String fileName) {
        byte[] messageBytes = new byte[2 + fileName.getBytes().length];

        String size = String.valueOf(messageBytes.length);
        messageBytes = addMessageSizeInfo(messageBytes, size);

        messageBytes[size.getBytes().length + 1] = COMMAND_CODE;
        messageBytes[size.getBytes().length + 2] = DOWNLOAD_FILE_FROM_SERVER;
        System.arraycopy(fileName.getBytes(), 0, messageBytes, size.getBytes().length + 3, fileName.getBytes().length);
        return messageBytes;
    }

    public byte[] getFileListMessage(byte[] arr) {
        byte[] messageBytes = new byte[arr.length + 1];

        String size = String.valueOf(messageBytes.length);
        messageBytes = addMessageSizeInfo(messageBytes, size);

        messageBytes[size.getBytes().length + 1] = FILE_LIST_CODE;
        System.arraycopy(arr, 0, messageBytes, size.getBytes().length + 2, arr.length);
        return messageBytes;
    }

    private byte[] addMessageSizeInfo(byte[] messageBytes, String size) {
        messageBytes = new byte[messageBytes.length + 1 + size.getBytes().length];
        System.arraycopy(size.getBytes(), 0, messageBytes, 0, size.getBytes().length);
        messageBytes[size.getBytes().length] = FILE_SIZE_SEPARATE;
        return messageBytes;
    }

    private void readFileBytes(File file, byte[] fileBytes) {
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
            in.read(fileBytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void collectBytes(byte[] messageBytes, int size, byte[] nameBytes, byte[] checkSumBytes, byte[] fileBytes) {
        System.arraycopy(nameBytes, 0, messageBytes, 2 + size, nameBytes.length);
        messageBytes[2 + nameBytes.length + size] = SEPARATE_CODE;
        System.arraycopy(checkSumBytes, 0, messageBytes, 3 + nameBytes.length + size, checkSumBytes.length);
        System.arraycopy(fileBytes, 0, messageBytes, 3 + nameBytes.length + size + checkSumBytes.length, fileBytes.length);
    }

    public static byte[] getMD5(byte[] arr) {
        MessageDigest messageDigest = null;
        byte[] digest = arr;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(digest);
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);

        while (md5Hex.length() < 32) {
            md5Hex = "0" + md5Hex;
        }

        return digest;
    }

    public static String getMD5String(byte[] digest) {
        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);
        while (md5Hex.length() < 32) {
            md5Hex = "0" + md5Hex;
        }
        md5Hex = md5Hex.substring(0, 32);
        return md5Hex;
    }

    public static void print(byte [] arr){
        for (int i = 0; i < 50; i++) {
            if(i%10==0) System.out.println();
            System.out.print(arr[i]);
        }
        System.out.println(arr[arr.length-1]);
        System.out.println(arr.length);
    }
}
