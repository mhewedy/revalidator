package com.elm.common.revalidator.objects;

import com.elm.common.revalidator.annotations.Pattern;
import com.elm.common.revalidator.myvalidators.annotations.IsNumber;

/**
 * Created by mhewedy on 5/15/2014.
 */
public class Person {

    @Pattern("\\d+")
    private String id;

    @IsNumber
    private String someNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getSomeNumber() {
        return someNumber;
    }

    public void setSomeNumber(String someNumber) {
        this.someNumber = someNumber;
    }
}
