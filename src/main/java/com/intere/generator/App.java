package com.intere.generator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import com.intere.generator.builder.orchestration.CodeOrchestration;
import com.intere.generator.io.FileIOUtils;

/**
 * Entry point of the application.  It uses the Apache Commons CLI library to manage the CLI interface.
 * @author einternicola
 *
 */
public class App {
	private static final String GENERATOR_VERSION = "0.0.4";

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		System.out.println(appBanner());
		CommandLineParser cliParser = new PosixParser();
		CommandLine cmd = cliParser.parse(getOptions(), args);
		
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
			CodeOrchestration orchestrator = new CodeOrchestration(orchestrationFilePath, outputDirectory);
			System.out.println("\n");
			switch(orchestrator.getTree().getLanguage()) {
			case Java:
				System.out.println(javaBanner());
				break;
			case ObjC:
				System.out.println(objectiveCBanner());
				break;
			case Ruby:
				System.out.println(rubyBanner());
			}
			System.out.println("\n");
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
	
	public static String appBanner() {
		return  "\n" + 
				"============================================================================================================================================================\n" + 
				"   oooo  .oooooo..o   .oooooo.   ooooo      ooo        .oooooo.                                                           .                      \n" + 
				"   `888 d8P'    `Y8  d8P'  `Y8b  `888b.     `8'       d8P'  `Y8b                                                        .o8                      \n" + 
				"    888 Y88bo.      888      888  8 `88b.    8       888            .ooooo.  ooo. .oo.    .ooooo.  oooo d8b  .oooo.   .o888oo  .ooooo.  oooo d8b \n" + 
				"    888  `\"Y8888o.  888      888  8   `88b.  8       888           d88' `88b `888P\"Y88b  d88' `88b `888\"\"8P `P  )88b    888   d88' `88b `888\"\"8P \n" + 
				"    888      `\"Y88b 888      888  8     `88b.8       888     ooooo 888ooo888  888   888  888ooo888  888      .oP\"888    888   888   888  888     \n" + 
				"    888 oo     .d8P `88b    d88'  8       `888       `88.    .88'  888    .o  888   888  888    .o  888     d8(  888    888 . 888   888  888     \n" + 
				".o. 88P 8\"\"88888P'   `Y8bood8P'  o8o        `8        `Y8bood8P'   `Y8bod8P' o888o o888o `Y8bod8P' d888b    `Y888\"\"8o   \"888\" `Y8bod8P' d888b    \n" + 
				"`Y888P\n" + 
				"============================================================================================================================================================\n" + 
				"\n\n" +
				"VERSION: " + getVersion() + "\n\n";
		
		
	}
	
	public static String javaBanner() {
		return  "   oooo                                 \n" + 
				"   `888                                 \n" + 
				"    888  .oooo.   oooo    ooo  .oooo.   \n" + 
				"    888 `P  )88b   `88.  .8'  `P  )88b  \n" + 
				"    888  .oP\"888    `88..8'    .oP\"888  \n" + 
				"    888 d8(  888     `888'    d8(  888  \n" + 
				".o. 88P `Y888\"\"8o     `8'     `Y888\"\"8o \n" + 
				"`Y888P                                  \n" + 
				"                                        \n";
	}
	
	public static String rubyBanner() {
		return  "ooooooooo.                .o8                   \n" + 
				"`888   `Y88.             \"888                   \n" + 
				" 888   .d88' oooo  oooo   888oooo.  oooo    ooo \n" + 
				" 888ooo88P'  `888  `888   d88' `88b  `88.  .8'  \n" + 
				" 888`88b.     888   888   888   888   `88..8'   \n" + 
				" 888  `88b.   888   888   888   888    `888'    \n" + 
				"o888o  o888o  `V88V\"V8P'  `Y8bod8P'     .8'     \n" + 
				"                                    .o..P'      \n" + 
				"                                    `Y8P'       \n";
	}
	
	public static String objectiveCBanner() {
		return  "  .oooooo.    .o8           o8o                         .    o8o                                  .oooooo.   \n" + 
				" d8P'  `Y8b  \"888           `\"'                       .o8    `\"'                                 d8P'  `Y8b  \n" + 
				"888      888  888oooo.     oooo  .ooooo.   .ooooo.  .o888oo oooo  oooo    ooo  .ooooo.          888          \n" + 
				"888      888  d88' `88b    `888 d88' `88b d88' `\"Y8   888   `888   `88.  .8'  d88' `88b         888          \n" + 
				"888      888  888   888     888 888ooo888 888         888    888    `88..8'   888ooo888 8888888 888          \n" + 
				"`88b    d88'  888   888     888 888    .o 888   .o8   888 .  888     `888'    888    .o         `88b    ooo  \n" + 
				" `Y8bood8P'   `Y8bod8P'     888 `Y8bod8P' `Y8bod8P'   \"888\" o888o     `8'     `Y8bod8P'          `Y8bood8P'  \n" + 
				"                            888                                                                              \n" + 
				"                        .o. 88P                                                                              \n" + 
				"                        `Y888P                                                                               ";
	}
	
	public static String getVersion() {
		return GENERATOR_VERSION;
	}
}
