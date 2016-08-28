package com.intere.generator.builder.orchestration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.intere.generator.metadata.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.intere.generator.Language;

public class OrchestrationTree {
	private static final Logger LOGGER = LogManager.getLogger(OrchestrationTree.class);
	private Metadata metadata;
	private Language language;
	private List<CustomClass> modelClasses = new ArrayList<>();
	private HashMap<String, MetadataClasses> metadataMap = new HashMap<>();
	private HashMap<String, CustomClass> modelClassMap = new HashMap<>();
	private String prefix;
	private String suffix;
	
	public OrchestrationTree(String metadataPath, String prefix, String suffix) throws IOException {
		LOGGER.info("Reading Metadata File: " + metadataPath);
		setPrefix(prefix);
		setSuffix(suffix);
		readMetadata(metadataPath);
		buildTree(new File(metadataPath).getParent());
	}
	
	private void buildTree(String metadataPath) throws JsonProcessingException, IOException {
		for(MetadataClasses clazz : metadata.getClasses()) {
			metadataMap.put(clazz.getClassName(), clazz);
			readAndConfigureClasses(metadataPath, clazz);
		}
	}
	
	private void readAndConfigureClasses(String metadataPath, MetadataClasses clazz) throws JsonProcessingException, IOException {
		ObjectMapper jsonMapper = new ObjectMapper();
		File jsonFile = new File(metadataPath, clazz.getJsonFile());
		LOGGER.info("Reading JSON File: " + jsonFile.getAbsolutePath());
		JsonNode node = jsonMapper.readTree(jsonFile);		
		if(node.isArray()) {
			node = node.get(0);
		}

		List<CustomClass> tmpModelClasses = OrchestrationUtils.readBuildClasses(metadata, clazz, node, prefix, suffix);
		for(CustomClass model : tmpModelClasses) {
			String customName = customClassName(model.getClassName());
			modelClassMap.put(model.getClassName(), model);
		}
		wireAllObjectsTogether();
		modelClasses.addAll(modelClassMap.values());
	}

	private void wireAllObjectsTogether() {
		for(CustomClass clazz : modelClassMap.values()) {
			for(ModelClassProperty property : clazz.getProperty()) {
				wireProperties(property);
			}
		}
	}

	private void wireProperties(ModelClassProperty property) {
		switch(property.getDataType()) {
			case CLASS:
				if(null == property.getParentModel()) {
					property.setParentModel(modelClassMap.get(property.getType()));
				}
				break;

			case ARRAY:
				switch(property.getArraySubTypeProperty().getDataType()) {
					case CLASS:
						property.getArraySubTypeProperty().setParentModel(modelClassMap.get(property.getArraySubTypeProperty().getType()));
						break;

					case ARRAY:
						wireProperties(property.getArraySubTypeProperty());
						break;
				}
				break;
		}
	}

	private String customClassName(String className) {
		String customName = className;
		if(null != prefix && !prefix.isEmpty()) {
			customName = prefix + customName;
		}
		if(null != suffix && !suffix.isEmpty()) {
			customName = customName + suffix;
		}

		if(!className.equals(customName)) {
			return customName;
		}

		return null;
	}

	private void readMetadata(String metadataPath) throws IOException {
		ObjectMapper jsonMapper = new ObjectMapper();
		metadata = jsonMapper.readValue(new FileInputStream(metadataPath), Metadata.class);
		language = Language.fromFullName(metadata.getLanguage());
	}
	
	public HashMap<String, CustomClass> getModelClassMap() {
		return modelClassMap;
	}
	
	public List<CustomClass> getModelClasses() {
		return modelClasses;
	}
	
	public Metadata getMetadata() {
		return metadata;
	}
	
	public Language getLanguage() {
		return language;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
}
