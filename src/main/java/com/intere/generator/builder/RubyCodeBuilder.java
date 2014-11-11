package com.intere.generator.builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.intere.generator.builder.generation.RubyGeneration;
import com.intere.generator.deserializer.JsonDeserializer;

public class RubyCodeBuilder extends CodeBuilder {
	/**
	 * Constructor that sets up the code builder for ruby.
	 * @param className
	 * @param jsonFilename
	 * @throws IOException
	 */
	public RubyCodeBuilder(String namespace, String className, String jsonFilename) throws IOException {
		super(namespace, className, jsonFilename, new RubyGeneration());		
	}

	public HashMap<File, String> buildSourceFiles(File parentDirectory) throws IOException {
		HashMap<File, String> sourceCode = new HashMap<File, String>();
		List<JsonDeserializer> allDeserializers = new ArrayList<JsonDeserializer>();
		
		allDeserializers.add(getDeserializer());
		for(List<JsonDeserializer> list : getDeserializer().getSubClasses().values()) {
			allDeserializers.addAll(list);
		}
		
		for(JsonDeserializer generated : allDeserializers) {
			File implementationFile = new File(parentDirectory, generated.getFilename() + ".rb");
			sourceCode.put(implementationFile, generation.generateImplementationFile(generated));
		}
		sourceCode.put(new File(parentDirectory, "json_model.rb"), readJsonModelFile());
		
		return sourceCode;
	}
	
	@Override
	public HashMap<File, String> buildTestFiles(File parentDirectory) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String readJsonModelFile() throws IOException {
		return readResourceAndReplaceHeaders("/json_model.rb");
	}
}
