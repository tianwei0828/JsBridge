package com.tw.refreshprogressjsbridge.utils;


public final class StringUtil {

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        if ("null".equalsIgnoreCase(str)) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }


    /**
     * Check if a String is not whitespace, empty ("") or null.
     *
     * @param str the String to check, may be null
     * @return the input <code>str<code/> if it's not blank, otherwise
     * throw {@link IllegalArgumentException}.
     * @throws IllegalArgumentException if the input <code>str<code/> is blank.
     */
    public static String requireNotBlank(String str) {
        if (isBlank(str)) {
            throw new IllegalArgumentException("Blank string");
        } else {
            return str;
        }
    }

    /**
     * Check if a String is not whitespace, empty ("") or null.
     *
     * @param str the String to check, may be null
     * @return the input <code>str<code/> if it's not blank, otherwise
     * throw {@link IllegalArgumentException}.
     * @throws IllegalArgumentException if the input <code>str<code/> is blank.
     */
    public static String requireNotBlank(String str, String msg) {
        if (isBlank(str)) {
            throw new IllegalArgumentException(msg);
        } else {
            return str;
        }
    }

    public static void checkNotBlank(String str) {
        if (isBlank(str)) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkNotBlank(String str, String msg) {
        if (isBlank(str)) {
            throw new IllegalArgumentException(msg);
        }
    }
}
