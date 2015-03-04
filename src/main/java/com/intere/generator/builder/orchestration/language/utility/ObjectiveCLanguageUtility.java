package com.intere.generator.builder.orchestration.language.utility;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.intere.generator.builder.orchestration.OrchestrationTree;
import com.intere.generator.builder.orchestration.language.utility.objc.ObjectiveCModelBuilder;
import com.intere.generator.builder.orchestration.language.utility.objc.ObjectiveCTestBuilder;
import com.intere.generator.builder.orchestration.language.utility.objc.ObjectiveCViewBuilder;

public class ObjectiveCLanguageUtility extends AbstractLanguageUtility {
//	private static final Logger LOGGER = LogManager.getLogger(ObjectiveCLanguageUtility.class);
//	JsonLanguageInterpreter interpreter = new ObjectiveCModelInterpreter();
	
	ModelBuilder modelBuilder = new ObjectiveCModelBuilder();
	TestBuilder testBuilder = new ObjectiveCTestBuilder();
	ViewBuilder viewBuilder = new ObjectiveCViewBuilder();
	
	
	@Override
	public ModelBuilder getModelBuilder() {
		return modelBuilder;
	}
	
	@Override
	public TestBuilder getTestBuilder() {
		return testBuilder;
	}
	
	@Override
	public ViewBuilder getViewBuilder() {
		return viewBuilder;
	}
	
	@Override
	public Map<File, String> copyModelResources(File sourcePath, OrchestrationTree tree) throws IOException {
		Map<File, String> resources = new HashMap<>();
		resources.put(new File(sourcePath, "Serializer.h"), readSerializerHeader());
		resources.put(new File(sourcePath, "Serializer.m"), readSerializerImplementation());
		return resources;
	}
	
	/**
	 * Reads the contents of the static Serializer.m file.
	 * @return The contents of the entire Serializer.m file
	 * @throws IOException
	 */
	private String readSerializerImplementation() throws IOException {
		return readResourceAndReplaceHeaders("/Serializer.m");
	}

	/**
	 * Reads the contents of the static Serializer.h file.
	 * @return The contents of the entire Serializer.h file.
	 * @throws IOException
	 */
	private String readSerializerHeader() throws IOException {
		return readResourceAndReplaceHeaders("/Serializer.h");
	}

	

	@Override
	public void enforceFilenames(OrchestrationTree tree) {
		// No-Op for Objective C		
	}

//	@Override
//	public String buildViewImports(ModelClass modelClass) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public String buildViewClassDeclaration(ModelClass modelClass) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}

