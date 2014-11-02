package com.intere.generator.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

import com.intere.generator.Language;
import com.intere.generator.deserializer.JsonDeserializer;

/**
 * Factory creator for the appropriate type of code builder.
 * @author einternicola
 *
 */
public class CodeBuilderFactory {	
	/**
	 * Gets you the {@link JsonDeserializer} as a prerequisite to doing the code building.
	 * @param className
	 * @param jsonFile
	 * @return
	 * @throws IOException
	 */
	public static JsonDeserializer parseJson(String className, String jsonFile) throws IOException {
		FileInputStream fisTargetFile = new FileInputStream(new File(jsonFile));
		String jsonString = IOUtils.toString(fisTargetFile, "UTF-8");
		fisTargetFile.close();

		JsonDeserializer deserializer = new JsonDeserializer(className, jsonString);
		
		return deserializer;
	}
	
	/**
	 * Performs the actual code generation.
	 * @param generatedCode
	 * @throws IOException
	 */
	public static void generateCode(HashMap<File, String> generatedCode) throws IOException {
		for(File f : generatedCode.keySet()) {
			FileOutputStream out = new FileOutputStream(f);
			out.write(generatedCode.get(f).getBytes());
			out.close();
			System.out.println("Generated Source File: " + f.getAbsolutePath());
		}
	}
	
	public static CodeBuilder getCodeBuilderFactory(Language lang) {
		if(null != lang) {
			switch(lang) {
			case ObjC: {
				return new ObjectiveCCodeBuilder();
			}
			case Java: {
				// TODO
			}
			case Ruby: {
				// TODO
			}
			}
		}
		
		throw new IllegalArgumentException("Unsupported Language: " + lang.getFullName());
	}
}
