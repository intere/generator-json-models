package com.intere.generator.io;

import java.io.File;

public class FileIOUtils {	
	public static File createFolderIfNotExists(String folderPath) {
		File testFolder = new File(folderPath);
		if(testFolderCreateIfDoesNotExist(testFolder)) {
			return testFolder;
		}
		return null;
	}
	
	public static boolean testFolderCreateIfDoesNotExist(File testFolder) {
		if(!testFolder.exists()) {
			System.out.println(testFolder.getAbsolutePath() + " does not exist, creating it for you...");
			if(!testFolder.mkdirs()) {
				System.out.println("Couldn't create output directory, existing...");
				System.exit(-1);
			}
		}
		
		return testFolder.exists();
	}
	
	/**
	 * This method will ensure that the provided directory exists.  If it doesn't exist, it attempts to create it.
	 * @param directory
	 * @return
	 */
	public static boolean ensureExists(File directory) {
		if(directory.exists()) {
			return true;
		} else {
			if(directory.mkdirs()) {
				System.out.println("Created directory: " + directory.getAbsolutePath());
				return true;
			} else {
				System.out.println("Failed to create directory: " + directory.getAbsolutePath());
				return false;
			}
		}
	}
}
