package com.elm.common.revalidator.mapping;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

public class Rule implements Serializable {

    private static final long serialVersionUID = 1L;

    private String annotation;
    private String validatorImpl;
    private Level level;

    public String getAnnotation() {
        return annotation;
    }

    @XmlAttribute
    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getValidatorImpl() {
        return validatorImpl;
    }

    @XmlAttribute
    public void setValidatorImpl(String validatorImpl) {
        this.validatorImpl = validatorImpl;
    }

    public Level getLevel() {
        return level;
    }

    @XmlAttribute
    public void setLevel(Level level) {
        this.level = level;
    }

    public static enum Level {
        TYEP, FIELD;
    }

}
