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
import com.intere.generator.io.FileIOUtils;

public class App {
	private static final String GENERATOR_VERSION = "0.0.3";

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
		boolean genServices = cmd.hasOption("svcs");		

		if(null == className || null == jsonFilename) {
			System.out.println("ERROR: Invalid Usage: " + stringArrayToString(args) + "...\n\n");
			App.usage();
			return;
		}		

		System.out.println("Using Language: " + language.getFullName());
		File outputDirectory = FileIOUtils.createFolderIfNotExists(cmd.getOptionValue('o', "tmp"));
		CodeBuilder builder = CodeBuilderFactory.getCodeBuilderFactory(language, namespace, className, jsonFilename);
		HashMap<File, String> generatedCode = builder.buildSourceFiles(outputDirectory);
		CodeBuilderFactory.generateCode(generatedCode);
		
		if(genServices) {
			HashMap<File, String> generatedServices = builder.buildServiceFiles(outputDirectory);
			CodeBuilderFactory.generateCode(generatedServices);
		}

		
		HashMap<File, String> generatedTests = builder.buildTestFiles(outputDirectory);
		CodeBuilderFactory.generateTests(generatedTests);		
	}

	private static Options getOptions() {
		Options options = new Options();
		options.addOption("l", "language", true, "What Language you would like to use, can be one of: " +
			"objc (Objective-C), ruby, java");
		options.addOption("cn", "className", true, "What is the name for the base class?  You must provide this");
		options.addOption("f", "input-file", true, "The Input (JSON) File to read to generate the class");
		options.addOption("o", "output-location", true, "Where do you want the generated code to go?");
		options.addOption("ns", "namespace", true, "The Namespace (ruby) or Package (java) that the generated code should live in");
		options.addOption("svcs", "services", false, "Additionally, you'd like to generate services to go along with the models");

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
