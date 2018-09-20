package com.cloud.storage.common.client;

import com.cloud.storage.common.Directorys;
import com.cloud.storage.common.FileInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class ClientUtilities implements Directorys {

    public static final Logger logger = LogManager.getLogger(ClientUtilities.class.getName());

    private static ObservableList<FileInfo> clientTableList = FXCollections.observableArrayList();
    private static ObservableList<FileInfo> serverTableList = FXCollections.observableArrayList();

    public static ObservableList<FileInfo> fillClientTableList() {
        clientTableList.clear();

        Path path = Paths.get(CLIENT_DIRECTORY);
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    clientTableList.add(new FileInfo(file.getFileName().toString(), file.toFile().length(), file.toFile().getAbsolutePath()));
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            logger.error("Walk Files", e);
        }
        return clientTableList;
    }

    public static void fillServerTable(byte[] message) {
        ObservableList<FileInfo> tableList = readFromByteArray(message);
        serverTableList.clear();
        for (int i = 0; i < tableList.size(); i++) {
            serverTableList.add(tableList.get(i));
        }
    }

    private static ObservableList<FileInfo> readFromByteArray(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ArrayList<FileInfo> object = new ArrayList<>();

        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(bais);
            object = (ArrayList<FileInfo>) in.readObject();
        } catch (IOException e) {
            logger.error("Array Convert", e);
        } catch (ClassNotFoundException e) {
            logger.error("Array Convert", e);
        }
        ObservableList<FileInfo> list = FXCollections.observableArrayList(object);

        return list;
    }

    public static ObservableList<FileInfo> getServerTableList() {
        return serverTableList;
    }

    private static void clearClientFileList() {
        clientTableList.clear();
    }

    private static void clearServerFileList() {
        serverTableList.clear();
    }

    public static void refreshClientFileList() {
        clearClientFileList();
        fillClientTableList();
    }

    public static void refreshServerFileList(byte[] message) {
        clearServerFileList();
        fillServerTable(message);
    }
}
