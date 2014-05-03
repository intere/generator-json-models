package com.intere.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

		FileInputStream fisTargetFile = new FileInputStream(new File(filename));
		String jsonString = IOUtils.toString(fisTargetFile, "UTF-8");
		fisTargetFile.close();

		JsonDeserializer des = new JsonDeserializer(className, jsonString);

		// Write the Header File
		String headerFile = className + ".h";
		FileOutputStream out = new FileOutputStream(headerFile);
		out.write(des.generateHeaderFile().getBytes());
		out.close();
		System.out.println("Created file: " + headerFile);

		// Now Write the Impl File
		String implementationFile = className + ".m";
		out = new FileOutputStream(implementationFile);
		out.write(des.generateImplementationFile().getBytes());
		out.close();
		System.out.println("Created file: " + implementationFile);

	}

	public static void usage() {
		System.out.println();
		System.out.println("Usage: ");
		System.out.println("app <class name> <json file>");
		System.out.println();
		System.out.println("Parameters:");
		System.out.println("\tclass name - The name of the generate class");
		System.out.println("\tjson file - The JSON file to read to generate the file from");

	}

}
