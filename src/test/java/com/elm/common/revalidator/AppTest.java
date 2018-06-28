package com.elm.common.revalidator;

import com.elm.common.revalidator.objects.Person;
import com.elm.resultobjects.ApplicationResult;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ResourceBundle;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    }

    public void test1(){
        Person p = new Person();
        p.setId("1234124");
        p.setSomeNumber("0");

        ApplicationResult result = Validator.validate(p);
        assertEquals(true, result.isSuccessfull());
    }

    public void test2(){
        Person p = new Person();
        p.setId("1234124XXX");
        p.setSomeNumber("0");

        ApplicationResult result = Validator.validate(p);
        assertEquals(false, result.isSuccessfull());
    }

    public void test3(){
        Person p = new Person();
        p.setId("0");

        ApplicationResult result = Validator.validate(p);
        assertEquals("isNotAnumber", result.getResultMessage());
    }

    public void test4(){
        Person p = new Person();
        p.setId("0");

        Validator.setResourceBundle(ResourceBundle.getBundle("resources"));
        ApplicationResult result = Validator.validate(p);
        assertEquals("Is Not a Number", result.getResultMessage());
    }
}
