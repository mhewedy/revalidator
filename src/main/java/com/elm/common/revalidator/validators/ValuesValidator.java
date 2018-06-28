package com.elm.common.revalidator.validators;

import com.elm.common.revalidator.annotations.Values;
import com.elm.common.revalidator.util.ApplicationResult;
import com.elm.common.revalidator.util.Util;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class ValuesValidator extends AbstractValidator {

    @Override
    public ApplicationResult handle(String name, Object value, Annotation annotation, Object object, boolean optional) {
        ApplicationResult result = new ApplicationResult();

        if (optional && value == null) {
            return result;
        }

        Values annot = (Values) annotation;
        String[] values = annot.value();

        if (value != null && values != null) {
            for (String valueString : values) {
                if (value.equals(valueString)) {
                    return result;
                }
            }
        }
        result.setAsError(Util.getMessage("value.outOfRange", Arrays.toString(values), Util.getField(name)));
        return result;
    }
}
