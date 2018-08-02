package com.cloud.storage.common.client;


import com.cloud.storage.common.MessageEncoder;
import com.cloud.storage.common.ServiceCommands;
import com.cloud.storage.common.message.CommandMessage;
import com.cloud.storage.common.message.FileMessage;

import java.util.Arrays;

public class MessageDecoder implements ServiceCommands {


    public MyFile getFile(FileMessage message) {
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

        byte[] fileBytes = Arrays.copyOfRange(bytes, count + 1, bytes.length);

        return new MyFile(fileName.toString(), checkSumString, fileBytes);
    }

    public byte getCommand(CommandMessage message) {
        return message.getBytes()[0];
    }
}

