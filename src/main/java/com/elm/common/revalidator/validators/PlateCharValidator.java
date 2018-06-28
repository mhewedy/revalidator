package com.elm.common.revalidator.validators;



import com.elm.common.revalidator.util.Util;
import com.elm.resultobjects.ApplicationResult;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

public class PlateCharValidator extends AbstractValidator {

    @Override
    public ApplicationResult handle(String name, Object value,
                                    Annotation annotation, Object object, boolean optional) {
        ApplicationResult result = new ApplicationResult();

        if (optional && (value == null || ((String) value).trim().isEmpty())) {
            return result;
        }

        try {
            if (value == null
                    || ((String) value).trim().length() != 1
                    || !getListPlateChars().contains(
                    ((String) value).trim().charAt(0))) {
                result.setAsError(Util.getMessage("invalid.platechar",
                        value, Util.getField(name)));
            }
        } catch (ClassCastException ex) {
            throw new RuntimeException("PlateChar applies to Strings only: "
                    + Util.getField(name), ex);
        }
        return result;
    }

    private static ArrayList<Character> listPlateChars = null;

    public static ArrayList<Character> getListPlateChars() {
        if (listPlateChars == null) {
            listPlateChars = new ArrayList<Character>();
            for (int i = 0; i < Util.plateArabicChars.length; i++) {
                listPlateChars.add(Util.plateArabicChars[i]);
            }
        }
        return listPlateChars;
    }

}
