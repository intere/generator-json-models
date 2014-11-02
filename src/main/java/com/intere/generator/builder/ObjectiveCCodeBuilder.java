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
import com.intere.generator.deserializer.JsonDeserializer;

/**
 * Code Builder for Objective-C.
 * @author einternicola
 */
public class ObjectiveCCodeBuilder implements CodeBuilder {
	
	public HashMap<File, String> buildSourceFiles(JsonDeserializer rootDeserializer, File parentDirectory) throws IOException {
		
		HashMap<File, String> sourceCode = new HashMap<File, String>();
		
		List<JsonDeserializer> allDeserializers = new ArrayList<JsonDeserializer>();
		
		allDeserializers.add(rootDeserializer);
		for(List<JsonDeserializer> list : rootDeserializer.getSubClasses().values()) {
			allDeserializers.addAll(list);
		}
		
		for(JsonDeserializer generated : allDeserializers) {
			File headerFile = new File(parentDirectory, generated.getName() + ".h");
			sourceCode.put(headerFile, generated.generateHeaderFile());
			
			File implementationFile = new File(parentDirectory, generated.getName() + ".m");
			sourceCode.put(implementationFile, generated.generateImplementationFile());
		}
		sourceCode.put(new File(parentDirectory, "Serializer.h"), readSerializerHeader());
		sourceCode.put(new File(parentDirectory, "Serializer.m"), readSerializerImplementation());
		
		return sourceCode;
	}
	
	private String readSerializerImplementation() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = App.class.getResourceAsStream("/Serializer.m");
		IOUtils.copy(in, out);
		
		return new String(out.toByteArray());
	}

	private String readSerializerHeader() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = App.class.getResourceAsStream("/Serializer.h");
		IOUtils.copy(in, out);
		
		return new String(out.toByteArray());
	}
}
