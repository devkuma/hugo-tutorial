package com.devkuma;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MarkdownCollector {
    public static void main(String[] args) throws IOException {

        String filePath = "/Users/user/develop/devkuma-hugo-blog/content/ko/130_programming_JVM/50_JUnit";
        List<File> fileList = FileUtils.scanFile(filePath);

        fileList = fileList.stream()
                           .filter(file -> file.getName().endsWith(".md"))
                           .collect(Collectors.toList());

        StringBuffer sbAllDoc = new StringBuffer();

        for (File file : fileList) {
            System.out.println(file.getAbsolutePath());

            Path path = Paths.get(file.getAbsolutePath());
            List<String> lines = Files.readAllLines(path);

            StringBuffer sbDoc = new StringBuffer();
            String title = null;
            int dashCount = 0;
            for (String line : lines) {
                if (line.equals("---")) {
                    dashCount++;
                    if (dashCount == 2) {
                        continue;
                    }
                } else if (line.startsWith("title:") || line.startsWith("linkTitle:")) {
                    title = line.substring(line.indexOf(":") + 1).trim();
                    if (title.startsWith("\"")) {
                        title = title.substring(1, title.length() - 1);
                    }
                }

                if (dashCount > 1) {
                    if (sbDoc.length() == 0) {
                        sbDoc.append("# " + title).append(System.lineSeparator());
                    } else if (line.startsWith("![")) {
                        line = line.substring(0, line.indexOf("](") + 2) + "/Users/user/develop/devkuma-hugo-blog/static" + line.substring(
                                line.indexOf("](") + 2);
                    }

                    sbDoc.append(line).append(System.lineSeparator());
                }
            }
            sbAllDoc.append(sbDoc).append(System.lineSeparator());
        }

        Path path = Paths.get(filePath + "/all.md");
        byte[] bytes = sbAllDoc.toString().getBytes();
        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}