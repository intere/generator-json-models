package com.intere.generator.builder.orchestration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.intere.generator.Language;
import com.intere.generator.builder.orchestration.language.LanguageOrchestrator;
import com.intere.generator.metadata.Metadata;
import com.intere.generator.metadata.MetadataClasses;
import com.intere.generator.metadata.ModelClass;

public class OrchestrationTree {
	private static final Logger LOGGER = LogManager.getLogger(OrchestrationTree.class);
	private Metadata metadata;
	private Language language;
	private List<ModelClass> modelClasses = new ArrayList<>();
	private HashMap<String, MetadataClasses> metadataMap = new HashMap<>();
	private HashMap<String, ModelClass> modelClassMap = new HashMap<>();
	
	public OrchestrationTree(String metadataPath) throws IOException {
		LOGGER.info("Reading Metadata File: " + metadataPath);
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
		List<ModelClass> tmpModelClasses = OrchestrationUtils.readBuildClasses(metadata, clazz, node);
		for(ModelClass model : tmpModelClasses) {
			modelClassMap.put(model.getClassName(), model);
		}
		modelClasses.addAll(tmpModelClasses);
		// TODO: Now we need to pass back through and overlay the metadata configuration
	}

	private void readMetadata(String metadataPath) throws IOException {
		ObjectMapper jsonMapper = new ObjectMapper();
		metadata = jsonMapper.readValue(new FileInputStream(metadataPath), Metadata.class);
		language = Language.fromFullName(metadata.getLanguage());
	}
	
	public HashMap<String, ModelClass> getModelClassMap() {
		return modelClassMap;
	}
	
	public List<ModelClass> getModelClasses() {
		return modelClasses;
	}
	
	public Metadata getMetadata() {
		return metadata;
	}
	
	public Language getLanguage() {
		return language;
	}
}
