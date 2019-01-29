package org.careye.utils;

public class ArrayUtils {

    public static String nullStrToEmpty(Object str) {
        return (str == null ? "" : (str instanceof String ? (String)str : str.toString()));
    }
    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }
    /**
     * is null or its length is 0
     *
     * @param <V>
     * @param sourceArray
     * @return
     */
    public static <V> boolean isEmpty(V[] sourceArray) {
        return (sourceArray == null || sourceArray.length == 0);
    }
}
