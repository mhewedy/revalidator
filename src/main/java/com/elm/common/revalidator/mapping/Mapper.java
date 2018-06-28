package com.elm.common.revalidator.mapping;

import com.elm.util.Log;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Mapper {

    private static final Revalidate REVALIDATE = initRevalidate();

    public static List<Rule> getRules() {
        return REVALIDATE.getRules();
    }

    private static String getQualifiedClassName(String basePackage, String clzz) {
        if (basePackage != null && !basePackage.isEmpty()) {
            return basePackage + "." + clzz;
        }
        return clzz;
    }

    private static Revalidate initRevalidate() {

        Revalidate ret = new Revalidate();

        Revalidate builtInValidate = loadBuiltinRules();

        for (Rule r : builtInValidate.getRules()) {
            r.setAnnotation(getQualifiedClassName(builtInValidate.getBasePackage(), r.getAnnotation()));
            r.setValidatorImpl(getQualifiedClassName(builtInValidate.getBasePackage(), r.getValidatorImpl()));

            ret.getRules().add(r);
        }

        Revalidate userRevalidate = loadUserRules();
        for (Rule r : userRevalidate.getRules()) {
            r.setAnnotation(getQualifiedClassName(userRevalidate.getBasePackage(), r.getAnnotation()));
            r.setValidatorImpl(getQualifiedClassName(userRevalidate.getBasePackage(), r.getValidatorImpl()));

            ret.getRules().add(r);
        }

        return ret;
    }

    private static Revalidate loadBuiltinRules() {
        try {
            String builtinXmlFilePath = "com/elm/common/revalidator/revalidate.xml";

            JAXBContext context = JAXBContext.newInstance(Revalidate.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            InputStream is = Mapper.class.getClassLoader().getResourceAsStream(builtinXmlFilePath);
            if (is == null) {
                throw new RuntimeException("cannot find built-in" + builtinXmlFilePath + " in classpath");
            }
            Revalidate revalidate = (Revalidate) unmarshaller.unmarshal(is);
            is.close();
            return revalidate;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static Revalidate loadUserRules() {
        try {
            JAXBContext context = JAXBContext.newInstance(Revalidate.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            InputStream is = Mapper.class.getClassLoader().getResourceAsStream("revalidate.xml");
            if (is != null) {
                Revalidate revalidate = (Revalidate) unmarshaller.unmarshal(is);
                is.close();
                return revalidate;
            } else {
                Log.warn(Revalidate.class, "cannot find revalidate.xml in classpath");
            }
        } catch (JAXBException e) {
            Log.error(Revalidate.class, "cannot parse revalidate.xml " + e.getMessage());
        } catch (IOException e) {
            Log.error(Revalidate.class, e.getMessage());
        }
        return new Revalidate();
    }
}
