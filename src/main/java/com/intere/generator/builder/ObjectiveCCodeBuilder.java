package com.intere.generator.builder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.intere.generator.App;
import com.intere.generator.builder.generation.ObjectiveCGeneration;
import com.intere.generator.deserializer.JsonDeserializer;

/**
 * Code Builder for Objective-C.
 * @author einternicola
 */
public class ObjectiveCCodeBuilder extends CodeBuilder {
	
	public ObjectiveCCodeBuilder(String className, String jsonFilename) throws IOException {
		super(null, className, jsonFilename, new ObjectiveCGeneration());
	}

	public HashMap<File, String> buildSourceFiles(File parentDirectory) throws IOException {
		HashMap<File, String> sourceCode = new HashMap<File, String>();
		List<JsonDeserializer> allDeserializers = new ArrayList<JsonDeserializer>();
		
		allDeserializers.add(getDeserializer());
		for(List<JsonDeserializer> list : getDeserializer().getSubClasses().values()) {
			allDeserializers.addAll(list);
		}
		
		for(JsonDeserializer generated : allDeserializers) {
			File headerFile = new File(parentDirectory, generated.getFilename() + ".h");
			sourceCode.put(headerFile, generation.generateHeaderFile(generated));
			
			File implementationFile = new File(parentDirectory, generated.getFilename() + ".m");
			sourceCode.put(implementationFile, generation.generateImplementationFile(generated));
			
		}
		sourceCode.put(new File(parentDirectory, "Serializer.h"), readSerializerHeader());
		sourceCode.put(new File(parentDirectory, "Serializer.m"), readSerializerImplementation());
		
		return sourceCode;
	}
	
	/**
	 * Reads the contents of the static Serializer.m file.
	 * @return The contents of the entire Serializer.m file
	 * @throws IOException
	 */
	private String readSerializerImplementation() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = App.class.getResourceAsStream("/Serializer.m");
		IOUtils.copy(in, out);
		
		return new String(out.toByteArray());
	}

	/**
	 * Reads the contents of the static Serializer.h file.
	 * @return The contents of the entire Serializer.h file.
	 * @throws IOException
	 */
	private String readSerializerHeader() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = App.class.getResourceAsStream("/Serializer.h");
		IOUtils.copy(in, out);
		
		return new String(out.toByteArray());
	}
}
