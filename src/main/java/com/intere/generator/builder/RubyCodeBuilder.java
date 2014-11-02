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
import com.intere.generator.builder.generation.CodeGeneration;
import com.intere.generator.builder.generation.RubyGeneration;
import com.intere.generator.deserializer.JsonDeserializer;

public class RubyCodeBuilder implements CodeBuilder {
	
	private CodeGeneration generation;
	protected String className;
	protected String jsonFilename;
	private JsonDeserializer deserializer;
	
	public RubyCodeBuilder(String className, String jsonFilename) throws IOException {
		this.className = className;
		this.jsonFilename = jsonFilename;
		this.generation = new RubyGeneration();
		this.deserializer = generation.parseJson(className, jsonFilename);				
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
	
	private String readJsonModelFile() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = App.class.getResourceAsStream("/json_model.rb");
		IOUtils.copy(in, out);
		
		return new String(out.toByteArray());
	}

	public JsonDeserializer getDeserializer() {
		return deserializer;
	}
}
