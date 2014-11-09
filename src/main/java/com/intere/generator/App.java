package com.intere.generator;

import java.io.File;
import java.util.HashMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import com.intere.generator.builder.CodeBuilder;
import com.intere.generator.builder.CodeBuilderFactory;

public class App {
	private static final String GENERATOR_VERSION = "0.0.2";

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		CommandLineParser cliParser = new PosixParser();
		CommandLine cmd = cliParser.parse(getOptions(), args);

		String className = cmd.getOptionValue("cn");
		String jsonFilename = cmd.getOptionValue("f");
		String namespace = cmd.getOptionValue("ns", "com.json.generated");
		Language language = Language.fromAbbreviation(cmd.getOptionValue('l', "objc"));

		if(null == className || null == jsonFilename) {
			System.out.println("ERROR: Invalid Usage: " + stringArrayToString(args) + "...\n\n");
			App.usage();
			return;
		}

		System.out.println("Using Language: " + language.getFullName());
		File outputDirectory = getSourceOutputFolder(cmd.getOptionValue('o', "tmp"));
		CodeBuilder builder = CodeBuilderFactory.getCodeBuilderFactory(language, namespace, className, jsonFilename);
		HashMap<File, String> generatedCode = builder.buildSourceFiles(outputDirectory);
		CodeBuilderFactory.generateCode(generatedCode);
	}

	/**
	 * Handles the creation of the output directory.
	 * @param outputDir
	 * @return
	 */
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
		options.addOption("ns", "namespace", true, "The Namespace (ruby) or Package (java) that the generated code should live in");

		return options;
	}
	
	/**
	 * Converts the arg array to a single string.
	 * @param args
	 * @return
	 */
	public static String stringArrayToString(String[] args) {
		StringBuilder builder = new StringBuilder();
		
		for(String arg : args) {
			if(builder.length()>0) {
				builder.append(' ');
			}
			builder.append(arg);
		}
		
		return builder.toString();
	}

	public static void usage() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "code-generator", getOptions());
	}
	
	public static String getVersion() {
		return GENERATOR_VERSION;
	}
}
