package com.adnuntius.android.sdk;

public final class StringUtils {
    private StringUtils() {
    }

    public static String paddedString(int v, int size) {
        return paddedString(v + "", size);
    }

    public static String paddedString(String v, int size) {
        if (v.length() >= size) {
            return v;
        }

        final char[] value = new char[size];
        for (int i = 0; i < size - v.length(); i++) {
            value[i] = '0';
        }

        for (int i = size - v.length(), j = 0; i < size; i++) {
            value[i] = v.charAt(j++);
        }
        return new String(value);
    }


    public static void validateNotBlank(String ... values) {
        for (String value : values) {
            if (isBlank(value)) {
                throw new IllegalArgumentException("Value cannot be null, empty or whitespace only");
            }
        }
    }

    public static void validateNotBlank(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        if (value instanceof String && isBlank((String) value)) {
            throw new IllegalArgumentException("Value cannot empty or whitespace only");
        }
    }

    public static boolean isBlank(final String value) {
        return value == null || value.isEmpty() || value.trim().isEmpty();
    }

}
