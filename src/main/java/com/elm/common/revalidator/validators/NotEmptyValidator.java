package com.elm.common.revalidator.validators;

import com.elm.common.revalidator.annotations.NotEmpty;
import com.elm.common.revalidator.util.ApplicationResult;
import com.elm.common.revalidator.util.Util;

import java.lang.annotation.Annotation;
import java.util.List;

public class NotEmptyValidator extends AbstractValidator {

    @Override
    public ApplicationResult handle(String name, Object value, Annotation annotation, Object object, boolean optional) {
        ApplicationResult result = new ApplicationResult();

        if (optional && value == null) {
            return result;
        }

        if (value != null) {
            NotEmpty annot = (NotEmpty) annotation;

            if (value instanceof List<?>) {
                List<?> list = (List<?>) value;
                if (list == null || list.isEmpty()) {
                    result.setAsError(Util.getMessage("empty.list", Util.getField(name)));
                } else if (list.size() < annot.min() || list.size() > annot.max()) {
                    result.setAsError(Util.getMessage("invalid.list.minmax", annot.min(), annot.max(), Util.getField(name)));
                }
            } else {
                throw new RuntimeException("NotEmpty applies to Lists only: " + Util.getField(name));
            }
        } else {
            result.setAsError(Util.getMessage("empty.list", Util.getField(name)));
        }
        return result;
    }
}
