package me.litz.util;

public class SessionUtils {

    public static final String DEFAULT_USERNAME = "jisung";

    private static String username = DEFAULT_USERNAME;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String value) {
        if (value == null) {
            username = DEFAULT_USERNAME;
        } else {
            value = value.trim();
            if ("".equals(value)) username = DEFAULT_USERNAME;
            else {
                username = value;
            }
        }
    }
}
