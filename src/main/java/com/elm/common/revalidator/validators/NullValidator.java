package com.elm.common.revalidator.validators;



import com.elm.resultobjects.ApplicationResult;

import java.lang.annotation.Annotation;

public class NullValidator extends AbstractValidator {

    @Override
    public ApplicationResult handle(String name, Object value, Annotation annotation, Object object, boolean optional) {
        return new ApplicationResult(ApplicationResult.ERROR_CODE, "no such Validator");
    }

}
