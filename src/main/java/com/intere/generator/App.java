package com.intere.generator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.intere.generator.builder.CodeBuilder;
import com.intere.generator.builder.CodeBuilderFactory;
import com.intere.generator.builder.orchestration.CodeOrchestration;
import com.intere.generator.io.FileIOUtils;

public class App {
	private static final Logger LOGGER = LogManager.getLogger(App.class);
	
	private static final String GENERATOR_VERSION = "0.0.3";

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		CommandLineParser cliParser = new PosixParser();
		CommandLine cmd = cliParser.parse(getOptions(), args);
		
		if(cmd.hasOption("orchestrate")) {
			executeOrchestrationMode(cmd, args);
		} else {
			executeSimpleMode(cmd, args);
		}
	}

	private static Options getOptions() {
		Options options = new Options();
		options.addOption("l", "language", true, "What Language you would like to use, can be one of: " +
			"objc (Objective-C), ruby, java");
		options.addOption("cn", "className", true, "What is the name for the base class?  You must provide this");
		options.addOption("f", "input-file", true, "The Input (JSON) File to read to generate the class");
		options.addOption("o", "output-location", true, "Where do you want the generated code to go?");
		options.addOption("ns", "namespace", true, "The Namespace (ruby) or Package (java) that the generated code should live in");
		options.addOption("svcs", "generate-services", false, "Additionally, you'd like to generate services to go along with the models");
		options.addOption("vw", "generate-views", false, "Additionally, you'd like to generate views to go along with the models");
		options.addOption("or", "orchestrate", true, "Specify a metadata.json file to use to orchestrate the code generation (overrides most command line options)");

		return options;
	}
	
	private static void executeOrchestrationMode(CommandLine cmd, String[] args) throws IOException {
		String orchestrationFilePath = cmd.getOptionValue("orchestrate");
		File outputDirectory = FileIOUtils.createFolderIfNotExists(cmd.getOptionValue('o', "tmp"));
		
		new CodeOrchestration(orchestrationFilePath, outputDirectory);
	}
	
	/**
	 * Method that handles the "simple execution mode".
	 * @param cmd
	 * @param args
	 * @throws IOException
	 */
	private static void executeSimpleMode(CommandLine cmd, String[] args) throws IOException {
		String className = cmd.getOptionValue("cn");
		String jsonFilename = cmd.getOptionValue("f");
		String namespace = cmd.getOptionValue("ns", "com.json.generated");
		Language language = Language.fromAbbreviation(cmd.getOptionValue('l', "objc"));
		boolean genServices = cmd.hasOption("svcs");
		boolean genViews = cmd.hasOption("vw");

		if(null == className || null == jsonFilename) {
			LOGGER.error("ERROR: Invalid Usage: " + stringArrayToString(args) + "...\n\n");
			App.usage();
			return;
		}

		LOGGER.info("Using Language: " + language.getFullName());
		File outputDirectory = FileIOUtils.createFolderIfNotExists(cmd.getOptionValue('o', "tmp"));
		CodeBuilder builder = CodeBuilderFactory.getCodeBuilderFactory(language, namespace, className, jsonFilename);
		HashMap<File, String> generatedCode = builder.buildSourceFiles(outputDirectory);
		CodeBuilderFactory.generateCode(generatedCode);
		
		if(genServices) {
			HashMap<File, String> generatedServices = builder.buildServiceFiles(outputDirectory);
			CodeBuilderFactory.generateCode(generatedServices);
		}
		
		if(genViews) {
			HashMap<File, String> generatedViews = builder.buildViewFiles(outputDirectory);
			CodeBuilderFactory.generateCode(generatedViews);
		}

		
		HashMap<File, String> generatedTests = builder.buildTestFiles(outputDirectory);
		CodeBuilderFactory.generateTests(generatedTests);
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
