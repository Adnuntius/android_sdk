package com.adnuntius.android.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public final class ResourceUtils {
    private ResourceUtils() {
    }

    public static String getResourceAsString(final String path) throws IOException {
        final InputStream is = ResourceUtils.class.getResourceAsStream("/" + path);
        if (is == null) {
            throw new IOException("Path " + path + " not found");
        }
        String text = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        return text;
    }
}
