package com.cloud.storage.common.server;

import com.cloud.storage.common.Directorys;
import com.cloud.storage.common.FileInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class ServerUtilities implements Directorys {

    public static final Logger logger = LogManager.getLogger(ServerUtilities.class.getName());

    public static byte[] getFileList() {
        ArrayList<FileInfo> list = new ArrayList<>();
        Path path = Paths.get(SERVER_DIRECTORY);
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    list.add(new FileInfo(file.getFileName().toString(), file.toFile().length(), file.toFile().getAbsolutePath()));
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            logger.error("File Walk", e);
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
            logger.error("Array convert", e);
        }
        return baos.toByteArray();
    }

    public static void sendMessageToClient(ChannelHandlerContext ctx, byte[] message) {
        ctx.write(message);
        ctx.flush();
    }
}



