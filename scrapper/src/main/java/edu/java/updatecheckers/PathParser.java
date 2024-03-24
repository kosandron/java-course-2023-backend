package edu.java.updatecheckers;

import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PathParser {
    private static final String DOMAIN_SEPARATOR = "://";
    private static final String SEPARATOR = "/";

    public static List<String> getPathComponents(String url) {
        String newUrl = url.replace(DOMAIN_SEPARATOR, SEPARATOR);
        return List.of(newUrl.split(SEPARATOR));
    }
}
