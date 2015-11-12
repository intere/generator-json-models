package com.intere.generator.builder.generation.utils;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Created by einternicola on 9/11/15.
 */
public class CodeGenerationUtilsTest {
    @Test
    public void testGenerateCStyleHeaderBlock() {
        final String filename = "test-file.java";
        final String actual = CodeGenerationUtils.generateCStyleHeaderBlock(filename);
        assertNotNull(actual);
        assertTrue(actual.indexOf(filename)>=0);
    }

    @Test
    public void testGetDate() throws Exception {
        assertNotNull(CodeGenerationUtils.getDate());
    }
}
