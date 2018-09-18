package com.cloud.storage.common.server;


import com.cloud.storage.common.MessageEncoder;
import com.cloud.storage.common.ServiceCommands;
import com.cloud.storage.common.message.AuthMessage;
import com.cloud.storage.common.message.CommandMessage;
import com.cloud.storage.common.message.FileMessage;
import com.cloud.storage.common.message.RegistrationMessage;

import java.io.FileNotFoundException;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class MessageDecoder implements ServiceCommands {

    public MyFile getFile(FileMessage message) throws FileNotFoundException{
        byte[] bytes = message.getBytes();
        StringBuffer fileName = new StringBuffer();
        int count = 0;
        byte[] checkSum = new byte[MD5_CODE_LENGTH];
        while (bytes[count] != SEPARATE_CODE) {
            fileName.append((char) (bytes[count] & 0x00FF));
            count++;
        }
        for (int i = count + 1, j = 0; j < MD5_CODE_LENGTH; i++, j++) {
            checkSum[j] = bytes[i];
        }
        String checkSumString = MessageEncoder.getMD5String(checkSum);
        count += MD5_CODE_LENGTH;

        byte[] fileBytes = Arrays.copyOfRange(bytes, count+1, bytes.length-1);
        System.out.println(MessageEncoder.getMD5String(MessageEncoder.getMD5(fileBytes)).equals(checkSumString));
        if(MessageEncoder.getMD5String(MessageEncoder.getMD5(fileBytes)).equals(checkSumString)) {
            return new MyFile(fileName.toString(), checkSumString, fileBytes);
        }
        throw new FileNotFoundException("CheckSumm not equals");
    }

    public byte getCommand(CommandMessage message) {
            return message.getBytes()[0];
     }

     public String getFileName (CommandMessage message){
        return new String(Arrays.copyOfRange(message.getBytes(), 1, message.getBytes().length));
     }

    public User getUser(AuthMessage message) {
        byte[] login = new byte[MD5_CODE_LENGTH];
        byte[] pass = new byte[MD5_CODE_LENGTH];
        System.arraycopy(message.getBytes(), 0, login, 0, MD5_CODE_LENGTH);
        System.arraycopy(message.getBytes(), MD5_CODE_LENGTH, pass, 0, MD5_CODE_LENGTH);
        return new User(MessageEncoder.getMD5String(login), MessageEncoder.getMD5String(pass));
    }

    public User getUser(RegistrationMessage message) {
        byte[] login = new byte[MD5_CODE_LENGTH];
        byte[] pass = new byte[MD5_CODE_LENGTH];
        System.arraycopy(message.getBytes(), 0, login, 0, MD5_CODE_LENGTH);
        System.arraycopy(message.getBytes(), MD5_CODE_LENGTH, pass, 0, MD5_CODE_LENGTH);
        byte[] nickNameBytes = Arrays.copyOfRange(message.getBytes(), MD5_CODE_LENGTH * 2, message.getBytes().length);
        String nickName = new String(nickNameBytes);
        return new User(nickName, MessageEncoder.getMD5String(login), MessageEncoder.getMD5String(pass));
    }
}
