package com.intere.generator.builder.generation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import com.intere.generator.builder.interpreter.InterpreterUtils;
import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.deserializer.JsonDeserializer;
import com.intere.generator.deserializer.JsonNodeUtils;

public abstract class CodeGeneration {
	
	/**
	 * Gets you the {@link JsonDeserializer} as a prerequisite to doing the code building.
	 * @param className
	 * @param jsonFile
	 * @return
	 * @throws IOException
	 */
	public JsonDeserializer parseJson(String namespace, String className, String jsonFile) throws IOException {
		FileInputStream fisTargetFile = new FileInputStream(new File(jsonFile));
		String jsonString = IOUtils.toString(fisTargetFile, "UTF-8");
		fisTargetFile.close();

		JsonDeserializer deserializer = new JsonDeserializer(getInterpreter(), namespace, className, jsonString);
		
		return deserializer;
	}
	
	/**
	 * Generates the Header File (if applicable, some languages ONLY use implementation files).
	 * @param deserializer The deserializer to use to generate the header file.
	 * @return null if header files are not used by the desired language, the contents of the file, otherwise.
	 */
	public abstract String generateHeaderFile(JsonDeserializer deserializer);
	
	/**
	 * Generates the Implementation File for the given language.
	 * @param deserializer
	 * @return The contents of the implementation file body.
	 */
	public abstract String generateImplementationFile(JsonDeserializer deserializer);
	
	/**
	 * Generates the Unit Test Implementation File for the given language.
	 * @param deserializer The deserializer to use to generate the test file.
	 * @param jsonFilename The JSON Filename to use to build the test.
	 * @return The contents of the implementation file body.
	 */
	public abstract String generateTestFile(JsonDeserializer deserializer, String jsonFilename, String testJsonFilename);
	
	/**
	 * Provides you with the Language Interpreter.
	 * @return
	 */
	public abstract JsonLanguageInterpreter getInterpreter();
	
	protected String getDate() {
		return new Date().toString();
	}

	public String generateTestJson(JsonDeserializer generated) {
		return generated.getNode().toString();
	}

	public String getTestJsonFilename(JsonDeserializer generated) {
		return InterpreterUtils.capsToUnderscores(generated.getName());
	}
}
