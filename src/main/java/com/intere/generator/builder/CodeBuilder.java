package com.intere.generator.builder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.intere.generator.App;
import com.intere.generator.builder.generation.CodeGeneration;
import com.intere.generator.deserializer.JsonDeserializer;

public abstract class CodeBuilder {	
	protected CodeGeneration generation;
	protected String className;
	protected String jsonFilename;
	protected JsonDeserializer deserializer;
	protected String namespace;
	
	/**
	 * Constructor that sets the properties.
	 * @param className
	 * @param jsonFilename
	 * @param generation
	 * @throws IOException
	 */
	public CodeBuilder(String namespace, String className, String jsonFilename, CodeGeneration generation) throws IOException {
		this.namespace = namespace;
		this.className = className;
		this.jsonFilename = jsonFilename;
		this.generation = generation;
		this.deserializer = generation.parseJson(namespace, className, jsonFilename);
	}
	
	/**
	 * Given a {@link JsonDeserializer}, this method will hand you back a {@link HashMap} of all of the source files (and their generated source). 
	 * @param parentDirectory The parent directory where the Files will be generated into.
	 * @return A {@link HashMap} of {@link File}, {@link String} (source file + library files).
	 * @throws IOException 
	 */
	public abstract HashMap<File, String> buildSourceFiles(File parentDirectory) throws IOException;
	
	/**
	 * Given a {@link JsonDeserializer}, this method will hand you back a {@link HashMap} of all of the test files (and their generated source).
	 * @param parentDirectory The parent directory where the Files will be generated into.
	 * @return A {@link HashMap} of {@link File}, {@link String} (test file + source json).
	 * @throws IOException
	 */
	public abstract HashMap<File, String> buildTestFiles(File parentDirectory) throws IOException;
	
	public String getClassName() {
		return className;
	}
	
	public String getJsonFilename() {
		return jsonFilename;
	}

	public JsonDeserializer getDeserializer() {
		return deserializer;
	}
	
	/**
	 * Reads a resource (from the classpath) to a string for you.
	 * @param resourceName
	 * @return
	 * @throws IOException
	 */
	public String readResource(String resourceName) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = App.class.getResourceAsStream(resourceName);
		IOUtils.copy(in, out);
		
		return new String(out.toByteArray());
	}
	
	/**
	 * Reads the provided resource and performs the substitution specified by 
	 * {{@link #replaceHeaderVariables(String)}.
	 * @param resourceName The resource you would like to read.
	 * @return
	 * @throws IOException
	 */
	public String readResourceAndReplaceHeaders(String resourceName) throws IOException {
		return replaceHeaderVariables(readResource(resourceName));
	}
	
	/**
	 * Replaces the "${version}" string with the version and "${date}" with the date/time.
	 * @param input
	 * @return
	 */
	protected String replaceHeaderVariables(String input) {
		return input.replaceAll(Pattern.quote("${version}"), App.getVersion())
				.replaceAll(Pattern.quote("${date}"), new Date().toString());
	}
}
