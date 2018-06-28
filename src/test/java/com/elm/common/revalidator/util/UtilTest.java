package com.elm.common.revalidator.util;

import junit.framework.TestCase;

public class UtilTest extends TestCase {

    public void testIsArabic() {

        assertTrue(Util.isArabic("مرحبا", false));
        assertFalse(Util.isArabic("مرحبا 123", false));
        assertTrue(Util.isArabic("مرحبا 123", true));

    }
}