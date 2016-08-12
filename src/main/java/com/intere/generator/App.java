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

import com.intere.generator.builder.orchestration.CodeOrchestration;
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
		options.addOption("td", "template-directory", true, "Where should the templates be read from?");
		options.addOption("ct", "custom-template", true, "What custom template would you like to execute?");
		options.addOption("pre", "custom-prefix", true, "A prefix to be added to each class name (for custom templates only)");
		options.addOption("suf", "class-suffix", true, "A suffix to be added to each class name (for custom templates only)");
		return options;
	}
	
	private static void executeOrchestrationMode(CommandLine cmd, String[] args) throws IOException {
		String orchestrationFilePath = cmd.getOptionValue("orchestrate");
		File outputDirectory = FileIOUtils.createFolderIfNotExists(cmd.getOptionValue('o', "tmp"));

		if(cmd.hasOption("template-directory")) {
			File templateDirectory = new File(cmd.getOptionValue("template-directory"));
			if(!templateDirectory.exists()) {
				throw new IllegalArgumentException("ERROR!  " + templateDirectory.getAbsolutePath() + " doesn't exist");
			}
			factory.setTemplateDirectory(templateDirectory);
		}
		if(cmd.hasOption("custom-template")) {
			factory.setTemplateFile(cmd.getOptionValue("custom-template"));

			if(!cmd.hasOption("custom-prefix") && !cmd.hasOption("custom-suffix")) {
				System.err.println("\n\nWARNING: it is recommended that you use a prefix or a suffix if you're using a custom template\n\n");
			}
		}
		if(cmd.hasOption("custom-prefix")) {
			factory.setClassPrefix(cmd.getOptionValue("custom-prefix"));
		}
		if(cmd.hasOption("custom-suffix")) {
			factory.setClassSuffix(cmd.getOptionValue("custom-suffix"));
		}

		if(cmd.hasOption("output-location")) {
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
