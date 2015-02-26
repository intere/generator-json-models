package com.intere.generator.io;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileIOUtils {	
	private static final Logger LOGGER = LogManager.getLogger(FileIOUtils.class);
	
	public static File createFolderIfNotExists(String folderPath) {
		File testFolder = new File(folderPath);
		if(testFolderCreateIfDoesNotExist(testFolder)) {
			return testFolder;
		}
		return null;
	}
	
	public static boolean testFolderCreateIfDoesNotExist(File testFolder) {
		if(!testFolder.exists()) {
			LOGGER.info(testFolder.getAbsolutePath() + " does not exist, creating it for you...");
			if(!testFolder.mkdirs()) {
				LOGGER.error("Couldn't create output directory, existing...");
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
				LOGGER.info("Created directory: " + directory.getAbsolutePath());
				return true;
			} else {
				LOGGER.error("Failed to create directory: " + directory.getAbsolutePath());
				return false;
			}
		}
	}
}
