package com.cloud.storage.common.client;

import com.cloud.storage.common.MessageEncoder;
import com.cloud.storage.common.ServiceCommands;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Network implements ServiceCommands {
    private Socket socket = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;
    private SendQueue queue;
    private boolean flag = true;
    private MessageEncoder encoder;

    private Controllers controller;

    private static Network ourInstance = new Network();

    public static Network getInstance() {
        return ourInstance;
    }

    public Network() {
        this.encoder = new MessageEncoder();
        try {
            this.socket = new Socket("localhost", SERVER_PORT);
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.queue = new SendQueue();

        new Thread(() -> {
            queue = new SendQueue();
            synchronized (this) {
                try {
                    while (true) {
                        while (flag) {
                            try {
                                rusume();
                                wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        while (!flag) {
                            out.write(queue.remove());
                            out.flush();
                            if (queue.isEmpty()) {
                                suspend();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeCinnection();
                }
            }
        }).start();

        new Thread(() -> {
            try {
                while (true) {
                    byte b;
                    ArrayList<Byte> sizeBytes = new ArrayList<>();
                    while ((b = in.readByte()) != FILE_SIZE_SEPARATE) {
                        sizeBytes.add((byte) b);
                    }
                    byte[] message = new byte[getSize(sizeBytes)];
                    int n = 0;
                    int count = 0;
                    byte[] buf = new byte[1024];
                    while ((n = in.read(buf)) != -1) {
                        System.arraycopy(buf, 0, message, count, n);
                        count = count + n;
                        if (count >= message.length) {
                            break;
                        }
                    }
                    new MessageHandler(message, controller);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeCinnection();
            }
        }).start();
    }

    private int getSize(ArrayList<Byte> sizeBytes) {
        byte[] arr = new byte[sizeBytes.size()];
        for (int i = 0; i < sizeBytes.size(); i++) {
            arr[i] = sizeBytes.get(i);
        }
        sizeBytes = null;
        return Integer.valueOf(new String(arr));
    }

    private synchronized void rusume() {
        if (!queue.isEmpty()) {
            flag = false;
            notify();
        }
    }

    private synchronized void suspend() {
        flag = true;
    }

    public void sendAuthInfo(String login, String pass) {
        queue.insert(encoder.getMessage(login, pass));
        rusume();
    }

    public void sendRegInfo(){
        queue.insert(encoder.getMessage("1000001rtem", "login", "pass"));
        rusume();
    }

    public void sendFile(File file) {
        queue.insert(encoder.getMessage(file));
        rusume();
    }

    public void downLoadFile(String path) {
        queue.insert(encoder.getMessage(ServiceCommands.DOWNLOAD_FILE_FROM_SERVER,path));
        rusume();
    }

    public void deleteFileFromServer (String path){
        queue.insert(encoder.getMessage(ServiceCommands.DELETE_FILE_FROM_SERVER,path));
        rusume();
    }

    public void closeCinnection() {
        queue.insert(encoder.getMessage(CLOSE_CONNECTION));
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setController(Controllers controller) {
        this.controller = controller;
    }
}
