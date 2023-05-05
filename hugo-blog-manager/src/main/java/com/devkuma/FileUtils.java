package com.devkuma;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileUtils {


    public static List<File> scanFile(String filePath) {
        final List<File> fileList = new ArrayList<>();

        final File[] files = new File(filePath).listFiles();
        //Arrays.stream(files).sorted();
        Arrays.sort(files);
        for (File file : files) {
            if (file.isFile()) {
                fileList.add(file);
            } else {
                fileList.addAll(scanFile(file.getAbsolutePath()));
            }
        }
        return fileList;
    }
}
