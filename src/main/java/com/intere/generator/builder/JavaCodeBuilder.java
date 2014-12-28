package com.intere.generator.builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.intere.generator.builder.generation.JavaGeneration;
import com.intere.generator.deserializer.JsonDeserializer;
import com.intere.generator.io.FileIOUtils;

public class JavaCodeBuilder extends CodeBuilder {
	public JavaCodeBuilder(String namespace, String className, String jsonFilename) throws IOException {
		super(namespace, className, jsonFilename, new JavaGeneration());
	}

	public HashMap<File, String> buildSourceFiles(File parentDirectory) throws IOException {		
		HashMap<File, String> sourceCode = new HashMap<File, String>();
		List<JsonDeserializer> allDeserializers = new ArrayList<JsonDeserializer>();
		
		allDeserializers.add(getDeserializer());
		for(List<JsonDeserializer> list : getDeserializer().getSubClasses().values()) {
			allDeserializers.addAll(list);
		}
		
		
		// Source Files
		File sourceDir = FileIOUtils.createFolderIfNotExists(parentDirectory 
				+ File.separator + "src" + File.separator + "main" + File.separator + "java");
		for(JsonDeserializer generated : allDeserializers) {
			File packagePath = FileIOUtils.createFolderIfNotExists(sourceDir + File.separator + generated.getNamespace().replaceAll("\\.", File.separator));
			File implementationFile = new File(packagePath, generated.getFilename() + ".java");
			sourceCode.put(implementationFile, generation.generateImplementationFile(generated));
		}
		
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
		
		// Test Files
		File testDir = FileIOUtils.createFolderIfNotExists(parentDirectory 
				+ File.separator + "src" + File.separator + "test" + File.separator + "java");
		for(JsonDeserializer generated : allDeserializers) {
			File packagePath = FileIOUtils.createFolderIfNotExists(testDir + File.separator + generated.getNamespace().replaceAll("\\.", File.separator));
			File testFile = new File(packagePath, generated.getTestFilename() + ".java");
			sourceCode.put(testFile, generation.generateTestFile(generated, jsonFilename, generation.getTestJsonFilename(generated)));
		}
		
		testDir = FileIOUtils.createFolderIfNotExists(parentDirectory 
				+ File.separator + "src" + File.separator + "test" + File.separator + "resources");
		for(JsonDeserializer generated : allDeserializers) {
			File packagePath = FileIOUtils.createFolderIfNotExists(testDir + File.separator + generated.getNamespace().replaceAll("\\.", File.separator));
			sourceCode.put(new File(packagePath, generation.getTestJsonFilename(generated) + ".json"), generation.generateTestJson(generated));
		}
				
		return sourceCode;
	}
}
