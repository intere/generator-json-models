package com.intere.generator.builder.generation;

import static org.junit.Assert.*;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.junit.Before;
import org.junit.Test;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.deserializer.JsonDeserializer;
import com.intere.generator.test.TestStrings;
import static com.intere.generator.test.TestUtils.parseJsonObject;

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
	}
}
