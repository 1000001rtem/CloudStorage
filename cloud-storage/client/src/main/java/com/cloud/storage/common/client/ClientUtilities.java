package com.cloud.storage.common.client;

import com.cloud.storage.common.Directorys;
import com.cloud.storage.common.FileInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class ClientUtilities implements Directorys {
    private static ObservableList<FileInfo> clientTableList = FXCollections.observableArrayList();
    private static ObservableList<FileInfo> serverTableList = FXCollections.observableArrayList();

    public static ObservableList<FileInfo> fillClientTableList() {
        clientTableList.clear();

        Path path = Paths.get(CLIENT_DIRECTORY);
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    clientTableList.add(new FileInfo(file.getFileName().toString(), file.toFile().length()));
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clientTableList;
    }

    public static void fillServerTable(byte [] message){
        ObservableList<FileInfo> tableList = readFromByteArray(message);
        serverTableList.clear();
        for (int i = 0; i < tableList.size(); i++) {
            serverTableList.add(tableList.get(i));
        }
    }

    private static ObservableList<FileInfo> readFromByteArray(byte[] bytes)  {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ArrayList<FileInfo> object = new ArrayList<>();

        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(bais);
            object = (ArrayList<FileInfo>) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ObservableList<FileInfo> list = FXCollections.observableArrayList(object);

        return list;
    }

    public static ObservableList<FileInfo> getServerTableList (){
        return serverTableList;
    }
}
