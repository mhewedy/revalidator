package com.elm.common.revalidator.validators;

import com.elm.common.revalidator.annotations.NotNull;
import com.elm.common.revalidator.util.Util;
import com.elm.resultobjects.ApplicationResult;

import java.lang.annotation.Annotation;

public class NotNullValidator extends AbstractValidator {

    @Override
    public ApplicationResult handle(String name, Object value, Annotation annotation, Object object, boolean optional) {
        ApplicationResult result = new ApplicationResult();

        if (optional && value == null) {
            return result;
        }

        if (value == null) {
            result.setAsError(Util.getMessage("null.field", Util.getField(name)));
            return result;
        }

        NotNull annot = (NotNull) annotation;
        if (annot.checkEmptyString()) {
            if (((String) value).trim().length() == 0) {

                result.setAsError(Util.getMessage("null.field", Util.getField(name)));
                return result;
            }
        }

        return result;
    }

}
