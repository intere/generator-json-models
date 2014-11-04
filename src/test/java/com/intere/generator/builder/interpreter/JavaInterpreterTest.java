package com.intere.generator.builder.interpreter;

import static org.junit.Assert.*;

import org.junit.Test;

public class JavaInterpreterTest {
	
	private JavaInterpreter interpreter = new JavaInterpreter();

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
