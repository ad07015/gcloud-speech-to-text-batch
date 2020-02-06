package com.panser.speechtranslator.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FileReaderUtil {
    public static Map<String, String> keyValueFileToMap(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        Pattern DELIMITER = Pattern.compile("=");
        return lines.stream().map(DELIMITER::split)
                .flatMap(a -> IntStream.range(0, a.length - 1).filter(i -> i % 2 == 0)
                        .mapToObj(i -> new AbstractMap.SimpleEntry<>(a[i], a[i + 1])))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a));
    }
}
