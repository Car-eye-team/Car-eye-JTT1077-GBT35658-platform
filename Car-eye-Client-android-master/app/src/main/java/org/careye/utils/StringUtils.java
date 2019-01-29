package org.careye.utils;

public class StringUtils {
    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }
}
