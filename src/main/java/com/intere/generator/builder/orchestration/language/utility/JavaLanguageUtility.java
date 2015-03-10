package com.intere.generator.builder.orchestration.language.utility;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.intere.generator.builder.orchestration.OrchestrationTree;
import com.intere.generator.builder.orchestration.language.utility.java.JavaModelBuilder;
import com.intere.generator.builder.orchestration.language.utility.java.JavaTestBuilder;
import com.intere.generator.builder.orchestration.language.utility.java.JavaViewBuilder;

public class JavaLanguageUtility extends AbstractLanguageUtility {	
	ModelBuilder modelBuilder = new JavaModelBuilder();
	TestBuilder testBuilder = new JavaTestBuilder();
	ViewBuilder viewBuilder = new JavaViewBuilder();
	
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
	public Map<File, String> copyModelResources(File sourcePath,OrchestrationTree tree) throws IOException {
		// Currently a No-Op for Java;
		return new HashMap<>();
	}
	@Override
	public Map<File, String> copyViewResources(File viewPath,OrchestrationTree tree) throws IOException {
		// Currently a No-Op for Java;
		return new HashMap<>();
	}

	@Override
	public void enforceFilenames(OrchestrationTree tree) {
		// No-Op for Java
	}

	@Override
	public void enforcePropertyMappings(OrchestrationTree tree) {
		// no-op for Java (unless we find keywords).
	}
	
	@Override
	public Map<String, String> getPropertyMappings() {
		return new HashMap<>();
	}
}