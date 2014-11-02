package com.intere.generator.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

import com.intere.generator.Language;
import com.intere.generator.builder.generation.CodeGeneration;
import com.intere.generator.builder.generation.ObjectiveCGeneration;
import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.ObjectiveCInterpreter;
import com.intere.generator.deserializer.JsonDeserializer;

/**
 * Factory creator for the appropriate type of code builder.
 * @author einternicola
 *
 */
public class CodeBuilderFactory {
	
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
	
	public static CodeBuilder getCodeBuilderFactory(Language lang, String className, String jsonFilename) throws IOException {
		if(null != lang) {
			switch(lang) {
			case ObjC: {
				return new ObjectiveCCodeBuilder(className, jsonFilename);
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