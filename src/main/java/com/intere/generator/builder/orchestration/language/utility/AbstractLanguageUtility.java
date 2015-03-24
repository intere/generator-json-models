package com.intere.generator.builder.orchestration.language.utility;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.intere.generator.App;
import com.intere.generator.builder.orchestration.OrchestrationTree;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

public abstract class AbstractLanguageUtility implements LanguageUtility {
	
	/** The Property Mappings you'd like to take place.  */
	public abstract Map<String, String> getPropertyMappings();
	
	@Override
	public SingleViewControllerBuilder getSingleViewControllerBuilder() {
		// Default Implementation - returns null.
		return null;
	}
	
	@Override
	public ListViewControllerBuilder getListViewControllerBuilder() {
		// Default Implementation - returns null.
		return null;
	}

	/**
	 * Reads a resource (from the classpath) to a string for you.
	 * @param resourceName
	 * @return
	 * @throws IOException
	 */
	public String readResource(String resourceName) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = App.class.getResourceAsStream(resourceName);
		IOUtils.copy(in, out);
		
		return new String(out.toByteArray());
	}
	
	public String readFile(String filename) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = new FileInputStream(filename);
		IOUtils.copy(in, out);
		
		return new String(out.toByteArray());
	}
	
	/**
	 * Reads the provided resource and performs the substitution specified by 
	 * {{@link #replaceHeaderVariables(String)}.
	 * @param resourceName The resource you would like to read.
	 * @return
	 * @throws IOException
	 */
	public String readResourceAndReplaceHeaders(String resourceName) throws IOException {
		return replaceHeaderVariables(readResource(resourceName));
	}
	
	/**
	 * Replaces the "${version}" string with the version and "${date}" with the date/time.
	 * @param input
	 * @return
	 */
	protected String replaceHeaderVariables(String input) {
		return input.replaceAll(Pattern.quote("${version}"), App.getVersion())
				.replaceAll(Pattern.quote("${date}"), new Date().toString());
	}
	
	@Override
	public void enforcePropertyMappings(OrchestrationTree tree) {
		Map<String, String> propertyMapping = getPropertyMappings();
		
		for(ModelClass clazz : tree.getModelClasses()) {
			for(ModelClassProperty prop : clazz.getProperty()) {
				if(propertyMapping.containsKey(prop.getName())) {
					prop.setAlias(propertyMapping.get(prop.getName()));
				} else {
					prop.setAlias(prop.getName());
				}
			}
		}
	}
}
