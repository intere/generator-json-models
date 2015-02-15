package com.intere.generator.builder.interpreter;

import static org.junit.Assert.*;

import org.junit.Test;

import com.intere.generator.builder.interpreter.models.JavaModelInterpreter;

public class JavaInterpreterTest {
	
	private JavaModelInterpreter interpreter = new JavaModelInterpreter();
	
	@Test
	public void testHumanReadableString() {
		String []inputs = {"var", "Otis", "_id", "this_is_the_end", "ThisIsTheEnd", "user ID", "user_iD", "myJavaClass"};
		String []expected = {"Var", "Otis", "Id", "This Is The End", "This Is The End", "User ID", "User ID", "My Java Class"};
		
		for(int i=0; i<inputs.length; i++) {
			assertEquals(expected[i], interpreter.humanReadableName(inputs[i]));
		}
	}

	@Test
	public void testCleanVariableName() {
		String []inputs = {"var", "Otis", "_id", "this_is_the_end"};
		String []expected = {"var", "otis", "id", "thisIsTheEnd"};
		
		for(int i=0; i<inputs.length; i++) {
			assertEquals(expected[i], interpreter.cleanVariableName(inputs[i]));
		}
	}
	
	@Test
	public void testBuildSubClassName() {
		String parentClassName = "My";
		String []inputs = {"var", "Otis", "_id", "this_is_the_end"};
		String []expected = {"MyVar", "MyOtis", "MyId", "MyThisIsTheEnd"};
		
		for(int i=0; i<inputs.length; i++) {
			assertEquals(expected[i], interpreter.buildSubClassName(parentClassName, inputs[i]));
		}
	}

}
