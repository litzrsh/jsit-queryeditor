package me.litz.util;

public class StringUtils {

    public static String trim(String value) {
        if (value == null) return null;
        value = value.trim();
        if ("".equals(value)) return null;
        return value;
    }
}
