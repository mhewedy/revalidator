package com.elm.common.revalidator.validators;

import com.elm.common.revalidator.Validator;
import com.elm.common.revalidator.annotations.Requires;
import com.elm.common.revalidator.annotations.Requires.Require;
import com.elm.common.revalidator.util.ApplicationResult;
import com.elm.common.revalidator.util.Util;
import org.apache.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class RequiresValidator extends AbstractValidator {

    private static Logger log = Logger.getLogger(RequiresValidator.class);

    @Override
    public ApplicationResult handle(String name, Object value,
                                    Annotation annotation, Object object, boolean optional) {
        ApplicationResult result = new ApplicationResult();

        if (optional && value == null) {
            return result;
        }
        result = validateRequires((Requires) annotation, name, value, object);

        if (result.isSuccessfull()) {
            result = validateNotRequires((Requires) annotation, name, value,
                    object);
        }

        return result;
    }

    private ApplicationResult validateRequires(Requires requires, String name,
                                               Object value, Object object) {
        ApplicationResult result = new ApplicationResult();
        Require[] requireArr = requires.require();

        for (Require require : requireArr) {

            if (value.equals(require.value())) {
                String[] forFields = require.forFields();

                result = validateFields(forFields, object);

                if (!result.isSuccessfull()) {
                    return result;
                }
            }
        }
        return result;
    }

    private ApplicationResult validateNotRequires(Requires requires,
                                                  String name, Object value, Object object) {
        Require[] notRequireArr = requires.notRequire();

        Map<String, Set<String>> map = getMapOfRequiredFieldsAlongWithValues(notRequireArr);

        List<String> requiredFields = findRequiredFields(map, value);

        ApplicationResult result = removeRequiredFieldsWithValue(requiredFields, object);

        if (!requiredFields.isEmpty()) {
            result.setAsError(Util.getMessage("required.field",
                    Arrays.toString(requiredFields.toArray(new String[]{})),
                    value, name, result.getResultMessage()));
        }
        return result;
    }

    private Map<String, Set<String>> getMapOfRequiredFieldsAlongWithValues(
            Require[] notRequireArr) {
        Map<String, Set<String>> map = new HashMap<String, Set<String>>();

        for (Require require : notRequireArr) {
            String reqValue = require.value();
            String[] forFields = require.forFields();

            for (String f : forFields) {
                Set<String> l = new HashSet<String>();
                if (map.containsKey(f)) {
                    l = map.get(f);

                }
                l.add(reqValue);
                map.put(f, l);
            }
        }

        return map;
    }

    private List<String> findRequiredFields(Map<String, Set<String>> map,
                                            Object value) {
        List<String> requiredFields = new ArrayList<String>();
        for (String field : map.keySet()) {
            if (!map.get(field).contains(String.valueOf(value))) {
                requiredFields.add(field);
            }
        }
        return requiredFields;
    }

    private ApplicationResult removeRequiredFieldsWithValue(
            List<String> requiredFields, Object object) {
        ApplicationResult result = new ApplicationResult();

        Iterator<String> requiredFieldIter = requiredFields.iterator();

        while (requiredFieldIter.hasNext()) {
            result = validateField(requiredFieldIter.next(), object);
            if (result.isSuccessfull()) {
                requiredFieldIter.remove();
            } else {
                return result;
            }
        }

        return result;
    }

    private ApplicationResult validateField(String fieldName, Object object) {
        ApplicationResult result = new ApplicationResult();
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            return Validator.validateField(field, object);
        } catch (Exception e) {
            log.info("Exception in RequiresValidator", e);
        }
        return result;
    }

    private ApplicationResult validateFields(String[] fieldNames, Object object) {
        ApplicationResult result = new ApplicationResult();
        for (String fieldName : fieldNames) {
            try {
                Field field = object.getClass().getDeclaredField(fieldName);
                result = Validator.validateField(field, object);
                if (!result.isSuccessfull()) {
                    return result;
                }
            } catch (Exception e) {
                log.info("Exception in RequiresValidator", e);
            }
        }
        return result;
    }

}
