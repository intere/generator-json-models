package com.intere.generator.builder.interpreter;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.intere.generator.builder.interpreter.models.ObjectiveCModelInterpreter;

public class ObjectiveCInterpreterTest {
	
	private ObjectiveCModelInterpreter interpreter;
	
	@Before
	public void setUp() {
		interpreter = new ObjectiveCModelInterpreter();
	}

	@Test
	public void testCleanVariableName() {
		String []inputs = {"var", "Otis", "_id", "this_is_the_end", "description"};
		String []expected = {"var", "otis", "theId", "thisIsTheEnd", "theDescription"};
		
		for(int i=0; i<inputs.length; i++) {
			assertEquals(expected[i], interpreter.cleanVariableName(inputs[i]));
		}
	}
	
	@Test
	public void testBuildSubClassName() {
		String parentClassName = "My";
		String []inputs = {"var", "Otis", "_id", "this_is_the_end"};
		String []expected = {"MyVar", "MyOtis", "MyTheId", "MyThisIsTheEnd"};
		
		for(int i=0; i<inputs.length; i++) {
			assertEquals(expected[i], interpreter.buildSubClassName(parentClassName, inputs[i]));
		}
	}
}
