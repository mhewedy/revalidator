package com.elm.common.revalidator.util;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class Util {

    private static final Set<Character> ARABIC_CHARACTERS = new HashSet<Character>();

    public final static char[] plateArabicChars = {'\u0623', '\u0628',
            '\u062d', '\u062f', '\u0631', '\u0633', '\u0635', '\u0637',
            '\u0639', '\u0642', '\u0643', '\u0644', '\u0645', '\u0646',
            '\u0647', '\u0648', '\u064a'};

    public static ResourceBundle resourceBundle = null;

    public static boolean isDefaultNumberValue(Object value) {
        String s = String.valueOf(value);
        boolean isZero = Long.valueOf(s) == 0;
        return isZero;
    }

    public static String getMessage(String key, Object... args) {
        String value = getString(key);
        if (args != null && args.length > 0) {
            value = MessageFormat.format(value, args);
        }
        return value;
    }

    private static String getString(String key) {
        if (resourceBundle != null && key != null && resourceBundle.containsKey(key)) {
            return resourceBundle.getString(key);
        }
        return key;
    }

    public static String getField(String key) {
        return "(" + getString(key) + ")";
    }

    public static boolean isArabic(String text, boolean withNumber) {

        for (char ch : text.toCharArray()) {

            boolean validChar = ARABIC_CHARACTERS.contains(ch) ||
                    Character.isWhitespace(ch) ||
                    (withNumber && Character.isDigit(ch));

            if (!validChar) {
                return false;
            }
        }

        return true;
    }

    static {
        Character.UnicodeBlock block = Character.UnicodeBlock.ARABIC;
        for (int codePoint = Character.MIN_CODE_POINT; codePoint <= Character.MAX_CODE_POINT; codePoint++) {
            if (block == Character.UnicodeBlock.of(codePoint)) {
                ARABIC_CHARACTERS.add((char) codePoint);
            }
        }
    }
}
