package com.elm.common.revalidator.util;

import com.elm.util.Log;

import javax.xml.bind.JAXBElement;
import java.awt.font.NumericShaper;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * Contains String manipulation functions.<br/>
 * Operations usually include:<br/>
 * - Formatting to readable String.<br/>
 * - Parsing strings to Date or one of the related objects.<br/>
 * - Converting a raw Date (or other related) object to another object.<br/>
 * <p/>
 * <b>Change Log:</b><br/>
 * <code><pre>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * |Developer       | Version   | Date      | Purpose                           |
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * |Nadeem          | 1.0       | 29/04/2009| Initial version                   |
 * |Nadeem          | 1.1       | 29/04/2009| Added the functions:              |
 * |                |           |           | - {@link #formatTwoDigits(long)}      |
 * |                |           |           | - {@link #formatThreeDigits(long)}      |
 * |                |           |           | - {@link #isBegin(String, String)}      |
 * |                |           |           | - {@link #isLength(String, int)}      |
 * |                |           |           | - {@link #isNumeric(String)}      |
 * |                |           |           | - {@link #isNationalNumber(String)}      |
 * |                |           |           | - {@link #isNationalNumber(long)}      |
 * |Nadeem          | 1.2       | 13/05/2009| Added the {@link #trimString(String)} function      |
 * |ABDULLAH		| 1.3		| 09/08/2009| Added the functions:
 * |				| 			|			| - {@link #isNINOrIqamaOrMOIOrBorderNumber(String)} function |
 * |                |           |           | - {@link #isNINNumber(String)}      |
 * |                |           |           | - {@link #isIqamaNumber(String)}      |
 * |                |           |           | - {@link #isMOINumber(String)}      |
 * |                |           |           | - {@link #isBorderNumber(String)}      |
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * </pre></code>
 *
 * @author Nadeem Awad, other developers
 * @since 29/04/2009
 */
public class ElmStringUtils {
    public static String encodeToUTF(String iso) {
        if (iso == null) {
            return null;
        }

        String utf = iso;

        try {
            utf = new String(iso.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.error(ElmStringUtils.class, "[encodeToUTF] isoString (" + iso
                    + ")", e);
        }

        return utf;
    }

    public static String encodeToWindows(String iso) {
        if (iso == null) {
            return null;
        }

        String windows = iso;

        try {
            windows = new String(iso.getBytes("ISO-8859-1"), "WINDOWS-1256");
        } catch (UnsupportedEncodingException e) {
            Log.error(ElmStringUtils.class, "[encodeForWindows] isoString ("
                    + iso + ")", e);
        }

        return windows;
    }

    /**
     * Converts an Exception's stack trace to a String to be used in logging or
     * popup messages.
     *
     * @param ex The Exception
     * @return The Exception stack trace as String.
     * @author Nadeem Awad
     */
    public static String getExceptionDesc(Throwable ex) {
        StringWriter strWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(strWriter);

        ex.printStackTrace(writer);
        return strWriter.toString();
    }

    public static String concatNameSyllables(String... syllables) {
        StringBuffer name = new StringBuffer();
        if (syllables == null) {
            return name.toString();
        }

        for (String syllable : syllables) {
            if (syllable != null && !syllable.trim().equals("")) {
                if (name.length() != 0) {
                    name.append(" ");
                }
                name.append(syllable.trim());
            }
        }
        return name.toString();
    }

    /**
     * @param clazz
     * @return
     * @author Nadeem Awad Extracts the Class name of a given class.
     */
    public static String getClassName(Class<?> clazz) {
        String className = clazz.getName();

        int index = className.lastIndexOf(".");
        if (index > 0 && index < (className.length() + 1)) {
            className = className.substring(index + 1);
        }

        return className;
    }

    /**
     * @param str String that contains English digits
     * @author Khaled Al-Shehri Converts any English digits included in the
     * passed string into their corresponding Arabic digits.
     */
    public static String forceArabicNumbers(String str) {
        NumericShaper shaper = NumericShaper.getShaper(NumericShaper.ARABIC);
        char[] ca = str.toCharArray();
        shaper.shape(ca, 0, ca.length);
        return String.copyValueOf(ca);
    }

    // @Start 1.1
    private static NumberFormat twoDigitsFormatter = new DecimalFormat("00");
    private static NumberFormat threeDigitsFormatter = new DecimalFormat("000");

    public static String formatTwoDigits(long i) {
        return twoDigitsFormatter.format(i);
    }

    public static String formatThreeDigits(long i) {
        return threeDigitsFormatter.format(i);
    }

    public static boolean isNumeric(String text) {
        return text.matches("\\d{1,}");
    }

    public static boolean isBegin(String text, String c) {
        return text.matches("^[" + c + "](.*)");
    }

    public static boolean isLength(String text, int length) {
        if (text.length() == length) {
            return true;
        }
        return false;
    }

    /**
     * Verifies if the passed string follows the MOI format of national numbers
     * (i.e. NIN, Iqama Number, MOI number)
     */
    public static boolean isNationalNumber(String nationalNumber) {
        return isNicNumber(nationalNumber, "127");
    }

    private static boolean isNicNumber(String nicNumber, String prefix) {
        if (!isBegin(nicNumber, prefix) || !isNumeric(nicNumber)
                || !isLength(nicNumber, 10)) {
            return false;
        }

        char[] charArray = nicNumber.toCharArray();
        int[] numArray = new int[10];
        for (int i = 0; i < charArray.length; i++) {
            numArray[i] = Character.getNumericValue(charArray[i]);
        }
        int sum = 0;
        for (int i = 0; i < numArray.length - 1; i++) {
            if (i % 2 != 0) {
                sum += numArray[i];
            } else {
                int oddByTwo = numArray[i] * 2;
                String oddByTwoString = String.valueOf(oddByTwo);
                int[] oddByTwoArray = new int[oddByTwoString.length()];
                int oddByTwoSum = 0;
                for (int j = 0; j < oddByTwoArray.length; j++) {
                    oddByTwoArray[j] = Character.getNumericValue(oddByTwoString
                            .charAt(j));
                    oddByTwoSum += oddByTwoArray[j];
                }
                sum += oddByTwoSum;
            }
        }

        String sumString = String.valueOf(sum);
        int unit = Character.getNumericValue(sumString.charAt(sumString
                .length() - 1));
        if (unit == 0 && numArray[9] == 0) {
            return true;
        } else if ((10 - unit) == numArray[9]) {
            return true;
        }
        return false;
    }

    public static boolean isNationalNumber(long nationalNumber) {
        return isNationalNumber(String.valueOf(nationalNumber));
    }

    /**
     * Verifies if the passed mobile number is in the correct format of Saudi
     * mobile numbers. That is it starts with 9665 and has 12 digits:
     * 9665XXXXXXXX
     */
    public static boolean isMobile(String mobile) {
        if (!isNumeric(mobile)) {
            return false;
        }
        if (mobile.startsWith("9665") && mobile.trim().length() == 12) {
            return true;
        } else {
            return false;
        }
    }

    // @End 1.1

    // @Start 1.2

    /**
     * Checks if a String is null before trimming it. Useful when setting
     * Strings in value objects from a CallableStatement, to eliminate the check
     * for every field to avoid NullPointerExceptions
     *
     * @param str
     * @return
     * @author Nadeem Awad
     */
    public static String trimString(String str) {
        if (str != null) {
            str = str.trim();
        }
        return str;
    }

    // @End 1.2

    // @Start 1.3

    /**
     * Verifies if the passed string follows the MOI format of national numbers
     * (i.e. NIN, Iqama Number, MOI number and Border Number)
     */
    public static boolean isNINOrIqamaOrMOIOrBorderNumber(String nationalNumber) {
        return isNicNumber(nationalNumber, "12357");
    }

    /**
     * Verifies if the passed string follows the MOI format of national numbers
     * (i.e. NIN)
     */
    public static boolean isNINNumber(String nationalNumber) {
        return isNicNumber(nationalNumber, "1");
    }

    /**
     * Verifies if the passed string follows the MOI format of national numbers
     * (i.e. Iqama Number)
     */
    public static boolean isIqamaNumber(String nationalNumber) {
        return isNicNumber(nationalNumber, "2");
    }

    /**
     * Verifies if the passed string follows the MOI format of national numbers
     * (i.e.MOI number)
     */
    public static boolean isMOINumber(String nationalNumber) {
        return isNicNumber(nationalNumber, "7");
    }

    /**
     * Verifies if the passed string follows the MOI format of national numbers
     * (i.e. Border Number)
     */
    public static boolean isBorderNumber(String nationalNumber) {
        return isNicNumber(nationalNumber, "35");
    }

    // @End 1.3

    // start String Arabic validation

    public static boolean isArabic(String text, boolean isMoreWord,
                                   boolean withNumber) {
        boolean flag = true;
        char[] charArr = text.toCharArray();
        if (withNumber) {
            for (int i = 0; i < charArr.length; i++) {
                if (chars.contains(charArr[i])) {
                    flag = true;
                } else if (Character.isWhitespace(charArr[i])) {
                    flag = true;
                } else if (Character.isDigit(charArr[i])) {
                    flag = true;
                } else {
                    flag = false;
                    break;
                }
            }
        } else { // letters and white space only
            for (int i = 0; i < charArr.length; i++) {
                if (chars.contains(charArr[i])) {
                    flag = true;
                } else if (Character.isWhitespace(charArr[i])) {
                    flag = true;
                } else {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    public static boolean isArabic(String text) {
        return isArabic(text, false, false);
    }

    public static final Set<Character> chars = new HashSet<Character>();

    static {
        Character.UnicodeBlock block = Character.UnicodeBlock.ARABIC;
        for (int codePoint = Character.MIN_CODE_POINT; codePoint <= Character.MAX_CODE_POINT; codePoint++) {
            if (block == Character.UnicodeBlock.of(codePoint)) {
                chars.add((char) codePoint);
            }
        }
    }

    public static String getXMLValue(JAXBElement<String> in) {
        if (in != null) {
            return in.getValue();
        } else {
            return null;
        }
    }
}
