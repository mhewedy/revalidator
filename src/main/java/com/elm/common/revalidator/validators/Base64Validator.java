package com.elm.common.revalidator.validators;


import com.elm.common.revalidator.util.Util;
import com.elm.resultobjects.ApplicationResult;

import java.lang.annotation.Annotation;

public class Base64Validator extends AbstractValidator {

    @Override
    public ApplicationResult handle(String name, Object value,
                                    Annotation annotation, Object object, boolean optional) {
        ApplicationResult result = new ApplicationResult();

        if (optional && value == null) {
            return result;
        }
        try {
            if (value == null
                    || !((String) value)
                    .matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$")) {

                result.setAsError(Util.getMessage("invalid.base64", Util.getField(name)));
            }
        } catch (ClassCastException ex) {
            throw new RuntimeException("Base64 applies on Strings only: "
                    + Util.getField(name), ex);
        }
        return result;
    }
}
