package com.intere.generator.builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.intere.generator.builder.generation.models.ObjectiveCModelGeneration;
import com.intere.generator.builder.generation.services.ObjectiveCServiceGeneration;
import com.intere.generator.builder.generation.views.ObjectiveCViewGeneration;
import com.intere.generator.deserializer.JsonDeserializer;
import com.intere.generator.io.FileIOUtils;

/**
 * Code Builder for Objective-C.
 * @author einternicola
 */
public class ObjectiveCCodeBuilder extends CodeBuilder {
	/**
	 * Constructor that sets the Class Name and input JSON filename and sets the stage to perform
	 * code building for Objective-C.
	 * @param className
	 * @param jsonFilename
	 * @throws IOException
	 */
	public ObjectiveCCodeBuilder(String className, String jsonFilename) throws IOException {
		super(null, className, jsonFilename, new ObjectiveCModelGeneration(), new ObjectiveCServiceGeneration(), new ObjectiveCViewGeneration());
	}
	
	/**
	 * Builds the source files (and any associated libraries).
	 */
	public HashMap<File, String> buildSourceFiles(File parentDirectory) throws IOException {
		HashMap<File, String> sourceCode = new HashMap<File, String>();
		List<JsonDeserializer> allDeserializers = new ArrayList<JsonDeserializer>();
		
		allDeserializers.add(getDeserializer());
		for(List<JsonDeserializer> list : getDeserializer().getSubClasses().values()) {
			allDeserializers.addAll(list);
		}
		
		parentDirectory = FileIOUtils.createFolderIfNotExists(parentDirectory.getAbsolutePath() 
				+ File.separator + "src");
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
	
	@Override
	public HashMap<File, String> buildTestFiles(File parentDirectory) throws IOException {
		HashMap<File, String> sourceCode = new HashMap<File, String>();
		List<JsonDeserializer> allDeserializers = new ArrayList<JsonDeserializer>();
		
		allDeserializers.add(getDeserializer());
		for(List<JsonDeserializer> list : getDeserializer().getSubClasses().values()) {
			allDeserializers.addAll(list);
		}
		
		parentDirectory = FileIOUtils.createFolderIfNotExists(parentDirectory.getAbsolutePath() 
				+ File.separator + "test");
		
		for(JsonDeserializer generated : allDeserializers) {
			File implementationFile = new File(parentDirectory, generated.getTestFilename() + ".m");
			sourceCode.put(implementationFile, generation.generateTestFile(generated, jsonFilename, generation.getTestJsonFilename(generated)));
		}
		
		for(JsonDeserializer generated : allDeserializers) {
			sourceCode.put(new File(parentDirectory, generation.getTestJsonFilename(generated) + ".json"), generation.generateTestJson(generated));
		}
		
		return sourceCode;
	}
	
	@Override
	public HashMap<File, String> buildServiceFiles(File parentDirectory) throws IOException {
		parentDirectory = FileIOUtils.createFolderIfNotExists(parentDirectory.getAbsolutePath() 
				+ File.separator + "src");
		HashMap<File, String> sourceCode = new HashMap<File, String>();
		File headerFile = new File(parentDirectory, getDeserializer().getServiceFilename() + ".h");
		sourceCode.put(headerFile, serviceGeneration.generateHeaderFile(getDeserializer()));
		
		File implementationFile = new File(parentDirectory, getDeserializer().getServiceFilename() + ".m");
		sourceCode.put(implementationFile, serviceGeneration.generateImplementationFile(getDeserializer()));
		
		return sourceCode;
	}
	
	@Override
	public HashMap<File, String> buildViewFiles(File parentDirectory) throws IOException {
		parentDirectory = FileIOUtils.createFolderIfNotExists(parentDirectory.getAbsolutePath() 
				+ File.separator + "src");
		HashMap<File, String> sourceCode = new HashMap<File, String>();
		
		File headerFile = new File(parentDirectory, getDeserializer().getViewFilename() + ".h");
		sourceCode.put(headerFile, viewGeneration.generateHeaderFile(getDeserializer()));
		
		File implementationFile = new File(parentDirectory, getDeserializer().getViewFilename() + ".m");
		sourceCode.put(implementationFile, viewGeneration.generateImplementationFile(getDeserializer()));
		
		return sourceCode;
	}
	
	/**
	 * Reads the contents of the static Serializer.m file.
	 * @return The contents of the entire Serializer.m file
	 * @throws IOException
	 */
	private String readSerializerImplementation() throws IOException {
		return readResourceAndReplaceHeaders("/Serializer.m");
	}

	/**
	 * Reads the contents of the static Serializer.h file.
	 * @return The contents of the entire Serializer.h file.
	 * @throws IOException
	 */
	private String readSerializerHeader() throws IOException {
		return readResourceAndReplaceHeaders("/Serializer.h");
	}
}
