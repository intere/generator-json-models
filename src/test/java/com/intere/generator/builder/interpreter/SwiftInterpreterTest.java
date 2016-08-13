package com.intere.generator.builder.interpreter;

import com.intere.generator.builder.interpreter.models.JavaModelInterpreter;
import com.intere.generator.builder.interpreter.models.SwiftModelInterpreter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by internicolae on 8/13/16.
 */
public class SwiftInterpreterTest {

    private SwiftModelInterpreter interpreter = new SwiftModelInterpreter();

    @Test
    public void testHumanReadableString() {
        String []inputs = {"var", "Otis", "_id", "this_is_the_end", "ThisIsTheEnd", "user ID", "user_iD", "mySwiftClass"};
        String []expected = {"Var", "Otis", "Id", "This Is The End", "This Is The End", "User ID", "User ID", "My Swift Class"};

        for(int i=0; i<inputs.length; i++) {
            assertEquals(expected[i], interpreter.humanReadableName(inputs[i]));
        }
    }

    @Test
    public void testCleanVariableName() {
        String []inputs = {"var", "Otis", "_id", "this_is_the_end", "key_points"};
        String []expected = {"var", "otis", "theId", "thisIsTheEnd", "keyPoints"};

        for(int i=0; i<inputs.length; i++) {
            assertEquals(expected[i], interpreter.cleanVariableName(inputs[i]));
        }
    }

    @Test
    public void testBuildSubClassName() {
        String parentClassName = "My";
        String []inputs = {"var", "Otis", "_id", "this_is_the_end", "key_points"};
        String []expected = {"MyVar", "MyOtis", "MyTheId", "MyThisIsTheEnd", "MyKeyPoints"};

        for(int i=0; i<inputs.length; i++) {
            assertEquals(expected[i], interpreter.buildSubClassName(parentClassName, inputs[i]));
        }
    }
}
