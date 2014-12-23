package com.intere.generator.builder.generation;

import org.junit.Before;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.deserializer.JsonDeserializer;
import com.intere.generator.test.TestStrings;

public class CodeGenerationTest implements TestStrings {
	private CodeGeneration codeGen;
	
	@Before
	public void setUp() {
		codeGen = new MockCodeGeneration();
	}	
	
	private class MockCodeGeneration extends CodeGeneration {
		@Override
		public JsonLanguageInterpreter getInterpreter() {return null;}
		@Override
		public String generateImplementationFile(JsonDeserializer deserializer) {return null;}
		@Override
		public String generateHeaderFile(JsonDeserializer deserializer) {return null;}
		@Override
		public String generateTestFile(JsonDeserializer deserializer, String jsonFilename) {return null;}
	}
}
