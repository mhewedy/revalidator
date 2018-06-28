package com.elm.common.revalidator.validators;


import com.elm.common.revalidator.util.ApplicationResult;
import com.elm.common.revalidator.util.Util;

import java.lang.annotation.Annotation;

public class TimestampValidator extends AbstractValidator {

    @Override
    public ApplicationResult handle(String name, Object value,
                                    Annotation annotation, Object object, boolean optional) {
        ApplicationResult result = new ApplicationResult();

        if (optional && value == null) {
            return result;
        }

        try {
            if (value == null || ((Long) value) <= 0) {
                result.setAsError(Util.getMessage("invalid.timestamp", Util.getField(name)));
            }
        } catch (ClassCastException ex) {
            throw new RuntimeException("Timestamp applies to Long only: "
                    + Util.getField(name), ex);
        }
        return result;
    }
}
