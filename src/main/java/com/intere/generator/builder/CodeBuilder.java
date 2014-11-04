package com.intere.generator.builder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

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
	 * @return A {@link HashMap} of {@link File}, {@link String} (source file + source.
	 * @throws IOException 
	 */
	public abstract HashMap<File, String> buildSourceFiles(File parentDirectory) throws IOException;
	
	public String getClassName() {
		return className;
	}
	
	public String getJsonFilename() {
		return jsonFilename;
	}

	public JsonDeserializer getDeserializer() {
		return deserializer;
	}
}
