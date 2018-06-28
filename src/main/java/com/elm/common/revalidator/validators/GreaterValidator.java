package com.elm.common.revalidator.validators;

import com.elm.common.revalidator.annotations.Greater;
import com.elm.common.revalidator.util.Util;
import com.elm.resultobjects.ApplicationResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class GreaterValidator extends AbstractValidator {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public ApplicationResult handle(String name, Object value,
                                    Annotation annotation, Object object, boolean optional) {
        ApplicationResult result = new ApplicationResult();

        if (optional && value == null) {
            return result;
        }

        Greater annot = (Greater) annotation;

        try {
            String firstField = annot.firstField();
            String secondField = annot.secondField();

            Field firstFieldObj = value.getClass().getDeclaredField(firstField);
            Field secondFieldObj = value.getClass().getDeclaredField(
                    secondField);

            firstFieldObj.setAccessible(true);
            secondFieldObj.setAccessible(true);

            Object firstVal = firstFieldObj.get(value);
            Object secondVal = secondFieldObj.get(value);

            if (firstVal == null || secondVal == null) {
                result.setAsError(Util.getMessage("empty.greater.fields",
                        Util.getField(firstField), Util.getField(secondField)));
            }

            if (((Comparable) firstVal).compareTo(((Comparable) secondVal)) <= 0) {
                result.setAsError(Util.getMessage("should.come.before",
                        Util.getField(firstField), Util.getField(secondField)));
            }

        } catch (ClassCastException ex) {
            throw new RuntimeException("Greater applies to Comparables only: "
                    + Util.getField(name), ex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
