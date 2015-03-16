package com.intere.generator.metadata.utils;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;

import com.intere.generator.metadata.Metadata;
import com.intere.generator.metadata.MetadataClasses;

public class MetadataUtils {
	private Metadata metadata;
	private JsonNode rootNode;
	private Map<String, MetadataClasses> classMap = new HashMap<>();
	
	/** Constructor that takes the metadata object.  */
	public MetadataUtils(Metadata metadata) {
		this.metadata = metadata;
		buildMap();
	}
	
	/**
	 * Get the MetadataClass by class name.
	 * @param className
	 * @return
	 */
	public MetadataClasses getClassByName(String className) {
		return classMap.get(className);
	}
	
	/**
	 * Is the provided class name a readonly class?
	 * @param className
	 * @return
	 */
	public boolean isReadonly(String className) {
		MetadataClasses clazz = getClassByName(className);
		if(null != clazz) {
			return clazz.getReadonly();
		}
		return false;
	}

	/** Getter for the Metadata Object.  */
	public Metadata getMetadata() {
		return metadata;
	}
	
	/** Builds the initial map of the classes.  */
	private void buildMap() {
		for(MetadataClasses clazz : metadata.getClasses()) {
			classMap.put(clazz.getClassName(), clazz);
		}
	}
}
