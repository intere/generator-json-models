package com.intere.generator.builder.interpreter.models;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by einternicola on 9/11/15.
 */
public class JavaModelInterpreterTest {
    JavaModelInterpreter interpreter = new JavaModelInterpreter();

    @Test
    public void testCleanVariableName() {
        String[] inputs =  { null, "my_var", "__this__", "__this_is_a_call_____", "myProp" };
        String[] expected = {  "", "myVar",  "this", "thisIsACall", "myProp" };

        for(int i=0; i<inputs.length;i++) {
            assertEquals("cleanVariableName didn't properly clean " + inputs[i], expected[i], interpreter.cleanVariableName(inputs[i]));
        }
    }

    @Test
    public void testBuildClassName() {
        String []inputs = { null, "my_var", "some_property", };
        String []expected = { "", "MyVar", "SomeProperty" };

        for(int i=0; i<inputs.length;i++) {
            assertEquals("buildClassName didn't properly clean " + inputs[i], expected[i], interpreter.buildClassName(inputs[i]));
        }
    }

    @Test
    public void testBuildFilenameFromClassname() {
        String []inputs = { null, "my_var", "some_property", "MyVar" };
        String []expected = { "", "my_var", "some_property", "MyVar" };

        for(int i=0; i<inputs.length;i++) {
            assertEquals("buildFilenameFromClassname didn't properly build " + inputs[i], expected[i], interpreter.buildFilenameFromClassname(inputs[i]));
        }
    }

    @Test
    public void testBuildServiceFilenameFromClassname() {
        String []inputs =   { null,      "Trash",        "CamelCase",        "weirdCase" };
        String []expected = { "Service", "TrashService", "CamelCaseService", "weirdCaseService" };

        for(int i=0; i<inputs.length;i++) {
            assertEquals("buildServiceFilenameFromClassname didn't properly build " + inputs[i], expected[i], interpreter.buildServiceFilenameFromClassname(inputs[i]));
        }
    }

    @Test
    public void testBuildViewFilenameFromClassname() {
        String []inputs =   { null,   "Trash",     "CamelCase",     "weirdCase" };
        String []expected = { "View", "TrashView", "CamelCaseView", "weirdCaseView" };

        for(int i=0; i<inputs.length;i++) {
            assertEquals("buildViewFilenameFromClassname didn't properly build " + inputs[i], expected[i], interpreter.buildViewFilenameFromClassname(inputs[i]));
        }
    }

    @Test
    public void testBuildTestfilenameFromClassname() {
        String []inputs =   { null,   "Trash",     "CamelCase",     "weirdCase" };
        String []expected = { "Test", "TrashTest", "CamelCaseTest", "weirdCaseTest" };

        for(int i=0; i<inputs.length;i++) {
            assertEquals("buildTestfilenameFromClassname didn't properly build " + inputs[i], expected[i], interpreter.buildTestfilenameFromClassname(inputs[i]));
        }
    }

    @Test
    public void testBuildGetterAndSetterName() {
        String []inputs =   { null, "trash", "some_thing", "weIrdCase_thing" };
        String []expected = { "",   "Trash", "SomeThing",  "WeIrdCaseThing" };

        for(int i=0; i<inputs.length;i++) {
            assertEquals("buildGetterAndSetterName didn't properly build " + inputs[i], expected[i], interpreter.buildGetterAndSetterName(inputs[i]));
        }
    }
}
