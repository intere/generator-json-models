package com.intere.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.IOUtils;

import com.intere.generator.deserializer.JsonDeserializer;

public class App {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		CommandLineParser cliParser = new PosixParser();
		CommandLine cmd = cliParser.parse(getOptions(), args);
		
		String className = cmd.getOptionValue("cn");
		String filename = cmd.getOptionValue("f");
		Language language = Language.fromAbbreviation(cmd.getOptionValue('l', "objc"));
		
		if(null == className || null == filename) {
			System.out.println("ERROR: Invalid Usage...\n\n");
			App.usage();
			return;
		}
		
		System.out.println("Using Language: " + language.getFullName());

		File outputDir = getSourceOutputFolder(cmd.getOptionValue('o', "tmp"));

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

	public static File getSourceOutputFolder(String outputDir) {
		File testOutputDir = new File(outputDir + File.separatorChar + "src");
		if(!testOutputDir.exists()) {
			System.out.println(outputDir + " does not exist, creating it for you...");
			if(!testOutputDir.mkdirs()) {
				System.out.println("Couldn't create output directory, existing...");
				System.exit(-1);
			}
		}
		
		return testOutputDir;
	}
	
	private static Options getOptions() {
		Options options = new Options();
		options.addOption("l", "language", true, "What Language you would like to use, can be one of: " +
			"objc (Objective-C), ruby, java");
		options.addOption("cn", "className", true, "What is the name for the base class?  You must provide this");
		options.addOption("f", "input-file", true, "The Input (JSON) File to read to generate the class");
		options.addOption("o", "output-location", true, "Where do you want the generated code to go?");
		
		return options;
	}

	public static void usage() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "code-generator", getOptions());
	}
}
