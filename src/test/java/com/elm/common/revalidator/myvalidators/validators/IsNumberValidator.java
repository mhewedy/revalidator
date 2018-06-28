package com.elm.common.revalidator.myvalidators.validators;

import com.elm.common.revalidator.util.ApplicationResult;
import com.elm.common.revalidator.util.Util;
import com.elm.common.revalidator.validators.AbstractValidator;

import java.lang.annotation.Annotation;

public class IsNumberValidator extends AbstractValidator {

    @Override
    public ApplicationResult handle(String name, Object value,
                                    Annotation annotation, Object object, boolean optional) {
        ApplicationResult result = new ApplicationResult();

        try {
            if (optional
                    && (value == null || ((String) value).trim().isEmpty())) {
                return result;
            }

            String stringAsNumber = (String) value;

            if (stringAsNumber == null || !stringAsNumber.matches("\\d+")) {
                result.setAsError(Util.getMessage("isNotAnumber", Util.getField(name)));
            }
        } catch (ClassCastException ex) {
            throw new RuntimeException("applies to Strings only: " + name,
                    ex);
        }
        return result;
    }

}
