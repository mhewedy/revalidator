package com.elm.common.revalidator.mapping;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Revalidate implements Serializable {

    private static final long serialVersionUID = 1L;

    private String basePackage;
    private List<Rule> rules = new ArrayList<Rule>();

    public Revalidate() {
    }

    public List<Rule> getRules() {
        return rules;
    }

    @XmlElement(name = "rule")
    void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    @XmlAttribute
    void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    String getBasePackage() {
        return basePackage;
    }
}
