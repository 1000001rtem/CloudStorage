package com.cloud.storage.common.server;

import com.cloud.storage.common.Directorys;
import com.cloud.storage.common.FileInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerUtilities implements Directorys {

    public static byte[] getFileList() {
        ArrayList<FileInfo> list = new ArrayList<>();
        Path path = Paths.get(SERVER_DIRECTORY);
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    list.add(new FileInfo(file.getFileName().toString(), file.toFile().length()));
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writeToByteArray(list);

    }

    private static byte[] writeToByteArray(ArrayList<FileInfo> list) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
}



