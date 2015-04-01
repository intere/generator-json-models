package com.intere.generator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.intere.generator.builder.orchestration.CodeOrchestratorFactory;
import com.intere.generator.io.FileIOUtils;

/**
 * Entry point of the application.  It uses the Apache Commons CLI library to manage the CLI interface.
 * @author einternicola
 *
 */
public class App {
	private static final String GENERATOR_VERSION = "0.0.4";
	private static ApplicationContext context;
	private static CodeOrchestratorFactory factory;

	/**
	 * Entry point of the application.
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		System.out.println(Banners.appBanner());
		CommandLineParser cliParser = new PosixParser();
		CommandLine cmd = cliParser.parse(getOptions(), args);
		context = new ClassPathXmlApplicationContext("AppContext.xml");
		if(null == context) {
			System.err.println("Could not get Spring Application Context.  Bailing...");
			System.exit(1);
		}
		factory = context.getBean(CodeOrchestratorFactory.class);
		if(null==factory) {
			System.err.println("Could not get Code Orchestrator Factory from Spring Context.  Bailing...");
			System.exit(2);
		}
		
		executeOrchestrationMode(cmd, args);
	}

	private static Options getOptions() {
		Options options = new Options();
		options.addOption("or", "orchestrate", true, "Specify a metadata.json file to use to orchestrate the code generation (overrides most command line options)");
		options.addOption("o", "output-location", true, "Where do you want the generated code to go?");
		return options;
	}
	
	private static void executeOrchestrationMode(CommandLine cmd, String[] args) throws IOException {
		String orchestrationFilePath = cmd.getOptionValue("orchestrate");
		File outputDirectory = FileIOUtils.createFolderIfNotExists(cmd.getOptionValue('o', "tmp"));
		
		if(cmd.hasOption("or")) {
			factory.beginOrchestration(orchestrationFilePath, outputDirectory);
		} else {
			usage();
		}
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
		System.out.println("\n\n");
	}
	
	public static String getVersion() {
		return GENERATOR_VERSION;
	}
}
