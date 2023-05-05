package com.devkuma;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UrlValidator {
    public static void main(String[] args) throws IOException {
        List<File> fileList = FileUtils.scanFile("/Users/user/develop/devkuma-hugo-blog/content/ko");

        fileList = fileList.stream()
                           .filter(file -> file.getName().endsWith(".md"))
                           .collect(Collectors.toList());

        Map<String, Integer> urlMap = new HashMap<>();

        for (File file : fileList) {
            System.out.println(file.getAbsolutePath());

            Path path = Paths.get(file.getAbsolutePath());
            List<String> lines = Files.readAllLines(path);

            String url = null;
            for (String line : lines) {
                if (line.startsWith("url:")) {
                    url = line.substring(line.indexOf(":") + 1).trim();
                    if (url.startsWith("\"")) {
                        url = url.substring(1, url.length() - 1);
                    }
                }
            }

            int count = 0;
            if (urlMap.containsKey(url)) {
                count = urlMap.get(url) + 1;
            }
            urlMap.put(url, count);
        }

        List<String> urlList = urlMap.keySet()
                                     .stream()
                                     .filter(url -> urlMap.get(url).intValue() > 1)
                                     .collect(Collectors.toList());

        System.out.println("start---");
        for (String url : urlList) {
            System.out.println(url);
        }
        System.out.println("end---");
    }
}
