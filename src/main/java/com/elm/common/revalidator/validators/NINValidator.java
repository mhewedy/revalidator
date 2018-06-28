package com.elm.common.revalidator.validators;


import com.elm.common.revalidator.util.ApplicationResult;
import com.elm.common.revalidator.util.Util;

import java.lang.annotation.Annotation;

public class NINValidator extends AbstractValidator {

    @Override
    public ApplicationResult handle(String name, Object value,
                                    Annotation annotation, Object object, boolean optional) {
        ApplicationResult result = new ApplicationResult();

        try {
            if (optional
                    && (value == null || ((String) value).trim().isEmpty())) {
                return result;
            }

            String id = (String) value;

            if (!isNationalNumber(id)) {
                result.setAsError(Util.getMessage("invaild.nin", Util.getField(name)));
            }
        } catch (ClassCastException ex) {
            throw new RuntimeException("NIN applies to Strings only: " + Util.getField(name),
                    ex);
        }
        return result;
    }

    /**
     * Copy from Tamm Portal
     *
     * @param nationalNumber
     * @return
     */
    private static boolean isNationalNumber(String nationalNumber) {
    	if (nationalNumber == null || nationalNumber.trim().isEmpty()) {
    		return false;
    	}
    	
        if (!isBegin(nationalNumber, "127") || !isNumeric(nationalNumber)
                || !isLength(nationalNumber, 10)) {
            return false;
        }
        char[] charArray = nationalNumber.toCharArray();
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

    private static boolean isBegin(String text, String c) {
        return text.matches("^[" + c + "](.*)");
    }

    private static boolean isNumeric(String text) {
        if (text == null) {
            return false;
        }
        if (isBegin(text, "-")) {
            text = text.substring(1);
        }
        return text.matches("\\d{1,}");
    }

    private static boolean isLength(String text, int length) {
        if (text.length() == length) {
            return true;
        }
        return false;
    }
}
