package com.community.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    public static void createPath(String uploadPath) {
        Path path = Paths.get(uploadPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                System.out.println("Create directory: " + uploadPath);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Couldn't initialize storage", e);
            }
        }
    }

}
