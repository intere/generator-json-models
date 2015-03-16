package com.intere.generator.builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.intere.generator.builder.generation.models.RubyModelGeneration;
import com.intere.generator.builder.interpreter.InterpreterUtils;
import com.intere.generator.deserializer.JsonDeserializer;

public class RubyCodeBuilder extends CodeBuilder {
	/**
	 * Constructor that sets up the code builder for ruby.
	 * @param className
	 * @param jsonFilename
	 * @throws IOException
	 */
	public RubyCodeBuilder(String namespace, String className, String jsonFilename) throws IOException {
		super(namespace, className, jsonFilename, new RubyModelGeneration(), null, null);	// TODO - implement ruby service code generation	
	}

	public HashMap<File, String> buildSourceFiles(File parentDirectory) throws IOException {
		HashMap<File, String> sourceCode = new HashMap<File, String>();
		List<JsonDeserializer> allDeserializers = new ArrayList<JsonDeserializer>();
		
		allDeserializers.add(getDeserializer());
		for(List<JsonDeserializer> list : getDeserializer().getSubClasses().values()) {
			allDeserializers.addAll(list);
		}
		
		for(JsonDeserializer generated : allDeserializers) {
			File implementationFile = new File(parentDirectory, getRubyNamespacePath(namespace) + generated.getFilename() + ".rb");
			sourceCode.put(implementationFile, generation.generateImplementationFile(generated));
		}
		sourceCode.put(new File(parentDirectory, "json_model.rb"), readJsonModelFile());
		
		return sourceCode;
	}
	
	protected String getRubyNamespacePath(String namespace) {
		if(null==namespace) {
			return "";
		}
		
		return InterpreterUtils.capsToUnderscores(namespace) + File.separator;
	}
	
	@Override
	public HashMap<File, String> buildTestFiles(File parentDirectory) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<File, String> buildServiceFiles(File parentDirectory)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public HashMap<File, String> buildViewFiles(File parentDirectory)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String readJsonModelFile() throws IOException {
		return readResourceAndReplaceHeaders("/json_model.rb");
	}
}
