package com.elm.common.revalidator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Optional means, you cannot provide value for the field (null), but if it has
 * a value, and any other validation annotations present the value will be
 * validated against these other annotations
 *
 * @author mhewedy
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Optional {

}
