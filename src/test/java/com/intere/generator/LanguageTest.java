package com.intere.generator;


import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by einternicola on 8/7/15.
 */
public class LanguageTest {

    @Test
    public void testFromFullName_invalidFullName() {
        assertNull("We got a language from an invalid language name", Language.fromFullName("Arnold-C"));
    }

    @Test
    public void testFromFullName_validFullName() {
        assertNotNull("We got a null for Swift", Language.fromFullName("Swift"));
        assertNotNull("We got a null for Objective-C", Language.fromFullName("Objective-C"));
        assertNotNull("We got a null for Java", Language.fromFullName("Java"));
        assertNotNull("We got a null for Ruby", Language.fromFullName("Ruby"));
    }

    @Test
    public void testFromAbbreviation_invalidAbbreviation() {
        assertNull("We got a language from an invalid language abbreviation", Language.fromAbbreviation("ac"));
    }

    @Test
    public void testFromAbbreviation_validAbbreviation() {
        assertNotNull("We got a null for swift", Language.fromAbbreviation("swift"));
        assertNotNull("We got a null for objc", Language.fromAbbreviation("objc"));
        assertNotNull("We got a null for java", Language.fromAbbreviation("java"));
        assertNotNull("We got a null for ruby", Language.fromAbbreviation("ruby"));
    }
}
