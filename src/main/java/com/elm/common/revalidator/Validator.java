package com.elm.common.revalidator;

import com.elm.common.revalidator.annotations.Optional;
import com.elm.common.revalidator.mapping.Mapper;
import com.elm.common.revalidator.mapping.Rule;
import com.elm.common.revalidator.mapping.Rule.Level;
import com.elm.common.revalidator.util.Util;
import com.elm.common.revalidator.validators.AbstractValidator;
import com.elm.resultobjects.ApplicationResult;
import com.elm.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.ResourceBundle;

public final class Validator {
    private Validator() {
    }

    /**
     * Resource bundle to read messages from
     *
     * @param resourceBundle
     */
    public static void setResourceBundle(ResourceBundle resourceBundle) {
        Util.resourceBundle = resourceBundle;
    }

    public static ApplicationResult validate(Object object) {
        Class<? extends Object> clazz = object.getClass();
        
        Log.debug(Validator.class, "Validating object: " + object);
        ApplicationResult result = validate(null, object, Level.TYEP);

        Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0) {

            for (Field field : fields) {
                if (result.isSuccessfull()) {
                	Log.debug(Validator.class, "Validating field : " + field.getName() + " in object: " + object);
                    result = validate(field, object, Level.FIELD);
                }
            }
        }
        return result;
    }

    public static ApplicationResult validateField(Field field, Object object) {
        ApplicationResult result = new ApplicationResult();

        List<Rule> rules = Mapper.getRules();
        Log.debug(Validator.class, "validating " + field.getName());

        for (Rule rule : rules) {
            try {
                @SuppressWarnings("unchecked")
                Class<? extends Annotation> annotClzz = (Class<? extends Annotation>) Class
                        .forName(rule.getAnnotation());
                result = handle(field, object, annotClzz, false);

                if (!result.isSuccessfull()) {
                    break;
                }
            } catch (ClassNotFoundException e) {
                Log.info(Validator.class, "Exception in Validator", e);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private static ApplicationResult validate(Field field, Object object, Level level) {
        ApplicationResult result = new ApplicationResult();

        List<Rule> rules = Mapper.getRules();

        for (Rule rule : rules) {
            try {
                Class<? extends Annotation> annotClzz = (Class<? extends Annotation>) Class
                        .forName(rule.getAnnotation());
                result = level == Level.FIELD ? handle(field, object,
                        annotClzz, field.isAnnotationPresent(Optional.class))
                        : handle(object, annotClzz);

                if (!result.isSuccessfull()) {
                    break;
                }
            } catch (ClassNotFoundException e) {
                Log.info(Validator.class, "Exception in Validator", e);
            }
        }
        return result;
    }

    private static ApplicationResult handle(Object object,
                                            Class<? extends Annotation> annotationClass) {

        Class<?> clazz = object.getClass();
        if (clazz.isAnnotationPresent(annotationClass)) {

            Annotation annotation = clazz.getAnnotation(annotationClass);
            return AbstractValidator.getInstance(annotationClass).handle(
                    clazz.getSimpleName(), object, annotation, null, false);
        }

        return new ApplicationResult();
    }

    private static ApplicationResult handle(Field field, Object object,
                                            Class<? extends Annotation> annotationClass, boolean optional) {

        if (field.isAnnotationPresent(annotationClass)) {
            Annotation annotation = field.getAnnotation(annotationClass);

            try {
                String name = field.getName();
                field.setAccessible(true);
                Object value = field.get(object);
                return AbstractValidator.getInstance(annotationClass).handle(
                        name, value, annotation, object, optional);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return new ApplicationResult();
    }
}
