package com.elm.common.revalidator.validators;


import com.elm.common.revalidator.annotations.Pattern;
import com.elm.common.revalidator.util.ApplicationResult;
import com.elm.common.revalidator.util.Util;

import java.lang.annotation.Annotation;

public class PatternValidator extends AbstractValidator {

    @Override
    public ApplicationResult handle(String name, Object value,
                                    Annotation annotation, Object object, boolean optional) {
        ApplicationResult result = new ApplicationResult();

        try {
            if (optional
                    && (value == null || ((String) value).trim().isEmpty())) {
                return result;
            }

            Pattern annot = (Pattern) annotation;

            String str = (String) value;
            String pattern = annot.value();

            if (str == null || !str.matches(pattern)) {
                result.setAsError(Util.getMessage("invalid.pattern.field",
                        Util.getField(name), annot.value()));
            }
        } catch (ClassCastException ex) {
            throw new RuntimeException("Length applies to Strings only: "
                    + Util.getField(name), ex);
        }
        return result;
    }
}
