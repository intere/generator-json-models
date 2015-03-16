package com.intere.generator.builder.interpreter;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.intere.generator.builder.interpreter.models.RubyModelInterpreter;

public class RubyInterpreterTest {
	
	private RubyModelInterpreter interpreter;

	@Before
	public void setUp() {
		interpreter = new RubyModelInterpreter();
	}
	
	@Test
	public void testCleanVariableName() {
		String []inputs = {"MyProp", "propOne", "_props", "props", "this_isThe_End"};
		String []expected = {"my_prop", "prop_one", "props", "props", "this_is_the_end"};
		
		for(int i=0; i<inputs.length; i++) {
			assertEquals(expected[i], interpreter.cleanVariableName(inputs[i]));
		}
	}
	
	@Test
	public void testBuildSubClassName() {
		String parentClassName = "My";
		String []inputs = {"Snarf", "pilotShovel", "hand_grenade", "My", "FooMyFoo", "blarf", "What_Weird_Stuff"};
		String []expected = {"MySnarf", "MyPilotShovel", "MyHandGrenade", "MyMy", "MyFooMyFoo", "MyBlarf", "MyWhatWeirdStuff"};
		
		for (int i = 0; i < inputs.length; i++) {
			assertEquals(expected[i], interpreter.buildSubClassName(parentClassName, inputs[i]));
		}
	}
	
	@Test
	public void testBuildFilenameFromClassname() {
		String []inputs = {"MyClass", "BigLongClassWithLotsOfNames", "Stupid", "IDoNotKnow"};
		String []expected = {"my_class", "big_long_class_with_lots_of_names", "stupid", "i_do_not_know"};
		
		for(int i=0; i<inputs.length; i++) {
			assertEquals(expected[i], interpreter.buildFilenameFromClassname(inputs[i]));
		}
	}
}
