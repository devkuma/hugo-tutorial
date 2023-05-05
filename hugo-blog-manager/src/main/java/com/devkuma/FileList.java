package com.devkuma;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FileList {

    public static void main(String[] args) throws IOException, ParseException {
        List<File> fileList = FileUtils.scanFile("/Users/user/develop/devkuma-hugo-blog/content/ko");

        fileList = fileList.stream().filter(file -> file.getName().endsWith(".md")).collect(Collectors.toList());

        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat newDtFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+09:00");

        int count = 0;
        int writeCount = 0;
        for (File file : fileList) {
            System.out.println(file.getAbsolutePath());
            count++;

            Path path = Paths.get(file.getAbsolutePath());
            List<String> lines = Files.readAllLines(path);

            StringBuffer writeLines = new StringBuffer();
            boolean write = false;
            boolean startAliases = false;
            for (String line : lines) {

                if (startAliases) {
                    if (line.startsWith("  - ")) {
                        continue;
                    } else {
                        startAliases = false;
                    }
                }

                if (line.startsWith("aliases:")) {
                    startAliases = true;
                    write = true;
                    continue;
                }

                if ((line.startsWith("date:") || line.startsWith("lastmod:")) && line.endsWith(".0")) {

                    // String 타입을 Date 타입으로 변환
                    Date formatDate = dtFormat.parse(line.substring(line.indexOf(":") + 1).trim());
                    // Date타입의 변수를 새롭게 지정한 포맷으로 변환
                    String strNewDtFormat = newDtFormat.format(formatDate);
                    line = line.substring(0, line.indexOf(":") + 1).trim() + " " + strNewDtFormat;
                    write = true;
                }

                System.out.println(line);
                writeLines.append(line).append(System.lineSeparator());
            }

            if (write) {
                byte[] bytes = writeLines.toString().getBytes();
                try {
                    Files.write(path, bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                writeCount++;
            }
        }

        System.out.println(count);
        System.out.println(writeCount);
    }
}
