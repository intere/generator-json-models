package com.intere.generator.builder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.intere.generator.deserializer.JsonDeserializer;

public interface CodeBuilder {	
	/**
	 * Given a {@link JsonDeserializer}, this method will hand you back a {@link HashMap} of all of the source files (and their generated source). 
	 * @param rootDeserializer The Metadata about the JSON (used to generate the source code).
	 * @param parentDirectory The parent directory where the Files will be generated into.
	 * @return A {@link HashMap} of {@link File}, {@link String} (source file + source.
	 * @throws IOException 
	 */
	HashMap<File, String> buildSourceFiles(JsonDeserializer rootDeserializer, File parentDirectory) throws IOException;
}
