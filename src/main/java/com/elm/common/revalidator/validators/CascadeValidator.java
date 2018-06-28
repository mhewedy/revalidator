package com.elm.common.revalidator.validators;

import com.elm.common.revalidator.Validator;
import com.elm.common.revalidator.annotations.Cascade;
import com.elm.common.revalidator.util.ApplicationResult;

import java.lang.annotation.Annotation;
import java.util.Collection;

public class CascadeValidator extends AbstractValidator {

    @Override
    public ApplicationResult handle(String name, Object value, Annotation annotation, Object object, boolean optional) {
        ApplicationResult result = new ApplicationResult();

        Cascade annot = (Cascade) annotation;
        int cascadeSize = annot.value();

        if (value != null) {
            if (value instanceof Collection<?>) {
                Collection<?> valueList = (Collection<?>) value;
                if (valueList != null && !valueList.isEmpty()) {

                    if (cascadeSize == 0 || cascadeSize > valueList.size()) {
                        cascadeSize = valueList.size();
                    }

                    int cntr = 0;
                    for (Object o : valueList) {
                        if (cntr == cascadeSize) {
                            break;
                        }

                        result = Validator.validate(o);
                        if (!result.isSuccessfull()) {
                            return result;
                        }

                        cntr++;
                    }
                }
            } else {
                result = Validator.validate(value);
            }
        }
        return result;
    }
}
