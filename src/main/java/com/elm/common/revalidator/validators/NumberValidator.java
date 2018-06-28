package com.elm.common.revalidator.validators;

import com.elm.common.revalidator.annotations.Number;
import com.elm.common.revalidator.util.Util;
import com.elm.resultobjects.ApplicationResult;

import java.lang.annotation.Annotation;

public class NumberValidator extends AbstractValidator {

    @Override
    public ApplicationResult handle(String name, Object value,
                                    Annotation annotation, Object object, boolean optional) {
        ApplicationResult result = new ApplicationResult();

        if (optional && (value == null || Util.isDefaultNumberValue(value))) {
            return result;
        }

        Number annot = (Number) annotation;
        try {
            double dvalue = ((java.lang.Number) value).doubleValue();

            if (value == null || dvalue < annot.min() || dvalue > annot.max()) {
                result.setAsError(Util.getMessage("invalid.number.minmax",
                        annot.min(), annot.max(), Util.getField(name)));
            }
        } catch (ClassCastException ex) {
            throw new RuntimeException(
                    "Number applies to Numbers (up to double) only: " + Util.getField(name),
                    ex);
        }
        return result;
    }
}
