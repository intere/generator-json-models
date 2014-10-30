package com.intere.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.intere.generator.deserializer.JsonDeserializer;

public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if(args.length < 2) {
			App.usage();
			return;
		}

		String className = args[0];
		String filename = args[1];
		File outputDir = null;
		
		if(args.length > 2) {
			outputDir = getOutputFolder(args[2]);
		} else {
			outputDir = getOutputFolder("./tmp");
		}

		FileInputStream fisTargetFile = new FileInputStream(new File(filename));
		String jsonString = IOUtils.toString(fisTargetFile, "UTF-8");
		fisTargetFile.close();

		List<JsonDeserializer> allDeserializers = new ArrayList<JsonDeserializer>();
		JsonDeserializer des = new JsonDeserializer(className, jsonString);
		allDeserializers.add(des);
		for(List<JsonDeserializer> list : des.getSubClasses().values()) {
			allDeserializers.addAll(list);
		}
		
		for(JsonDeserializer generated : allDeserializers) {
			File headerFile = new File(outputDir, generated.getName() + ".h");
			FileOutputStream out = new FileOutputStream(headerFile);
			out.write(generated.generateHeaderFile().getBytes());
			out.close();
			System.out.println("Created Header File: " + headerFile.getAbsolutePath());
			
			File implementationFile = new File(outputDir, generated.getName() + ".m");
			out = new FileOutputStream(implementationFile);
			out.write(generated.generateImplementationFile().getBytes());
			out.close();
			System.out.println("Created Implementation File: " + implementationFile.getAbsolutePath());
		}
		
		copySerializerFiles(outputDir);
	}
	
	private static void copySerializerFiles(File outputDir) throws IOException {
		FileOutputStream out = new FileOutputStream(new File(outputDir, "Serializer.h"));
		InputStream in = App.class.getResourceAsStream("/Serializer.h");
		IOUtils.copy(in, out);
		
		out = new FileOutputStream(new File(outputDir, "Serializer.m"));
		in = App.class.getResourceAsStream("/Serializer.m");
		IOUtils.copy(in, out);
	}

	public static File getOutputFolder(String outputDir) {
		File testOutputDir = new File(outputDir);
		if(!testOutputDir.exists()) {
			System.out.println(outputDir + " does not exist, creating it for you...");
			if(!testOutputDir.mkdirs()) {
				System.out.println("Couldn't create output directory, existing...");
				System.exit(-1);
			}
		}
		
		return testOutputDir;
	}

	public static void usage() {
		System.out.println();
		System.out.println("Usage: ");
		System.out.println("app <class name> <json file> <output location>");
		System.out.println();
		System.out.println("Parameters:");
		System.out.println("\tclass name - The name of the generate class");
		System.out.println("\tjson file - The JSON file to read to generate the file from");
		System.out.println("\toutput location: Where you want the output files to go.");
	}
}
