package com.elm.common.revalidator.validators;

import com.elm.common.revalidator.mapping.Mapper;
import com.elm.common.revalidator.mapping.Rule;
import com.elm.common.revalidator.util.ApplicationResult;
import org.apache.log4j.Logger;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for all validator implementations. <br />
 * All sub-classes should be thread-safe.
 *
 * @author mhewedy
 */
public abstract class AbstractValidator {

    private static Logger log = Logger.getLogger(AbstractValidator.class);

    private static final Map<String, AbstractValidator> VALIDATORS_CACHE = new HashMap<String, AbstractValidator>();

    public static AbstractValidator getInstance(
            Class<? extends Annotation> annotationClass) {

        if (VALIDATORS_CACHE.containsKey(annotationClass.getName())) {
            return VALIDATORS_CACHE.get(annotationClass.getName());
        } else {
            List<Rule> rules = Mapper.getRules();

            for (Rule rule : rules) {
                AbstractValidator abstractValidator = newValidatorForAnnotationClass(
                        annotationClass, rule);
                if (abstractValidator != null) {
                    VALIDATORS_CACHE.put(annotationClass.getName(),
                            abstractValidator);
                    return abstractValidator;
                }
            }
            return new NullValidator();
        }
    }

    private static AbstractValidator newValidatorForAnnotationClass(
            Class<? extends Annotation> annotationClass, Rule rule) {
        try {
            String annotString = rule.getAnnotation();
            String validatorString = rule.getValidatorImpl();

            if (annotationClass.getName().equals(annotString)) {
                return (AbstractValidator) Class.forName(validatorString)
                        .newInstance();
            }
        } catch (Exception ex) {
            log.info("Exception in AbstractValidator", ex);
        }
        return null;
    }

    /**
     * @param name       field name
     * @param value      field value
     * @param annotation
     * @param object     object of the field, in case need to access other fields in
     *                   the validation
     * @param optional   field has @Optinal annotation
     * @return
     */
    public abstract ApplicationResult handle(String name, Object value,
                                             Annotation annotation, Object object, boolean optional);
}
