package com.elm.common.revalidator.validators;

import com.elm.common.revalidator.annotations.ArabicString;
import com.elm.common.revalidator.util.ApplicationResult;
import com.elm.common.revalidator.util.Util;

import java.lang.annotation.Annotation;

public class ArabicStringValidator extends AbstractValidator {

    @Override
    public ApplicationResult handle(String name, Object value,
                                    Annotation annotation, Object object, boolean optional) {
        ApplicationResult result = new ApplicationResult();

        try {
            if (optional && (value == null || ((String) value).isEmpty())) {
                return result;
            }

            ArabicString annot = (ArabicString) annotation;

            if (value == null
                    || ((String) value).trim().length() == 0
                    || !Util.isArabic((String) value,annot.withNumbers())) {
                result.setAsError(Util.getMessage("not.arabic", Util.getField(name)));
            }
        } catch (ClassCastException ex) {
            throw new RuntimeException("ArabicString applies to Strings only: "
                    + Util.getField(name), ex);
        }
        return result;
    }

}
