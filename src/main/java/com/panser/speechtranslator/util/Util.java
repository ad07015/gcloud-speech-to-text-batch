package com.panser.speechtranslator.util;

import java.util.Map;
import java.util.stream.Collectors;

public class Util {
    public static Map<String, String> filterNonEmptyMapEntries(Map<String, String> map) {
        return map.entrySet().stream().filter(entry -> !entry.getValue().isBlank())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
