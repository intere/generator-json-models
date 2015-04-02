package com.intere.generator.builder.orchestration.language.utility;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.intere.generator.builder.orchestration.OrchestrationTree;

@Service(value="JavaLanguage")
public class JavaLanguageUtility extends AbstractLanguageUtility {	
	@Autowired @Qualifier("JavaModelBuilder")
	ModelBuilder modelBuilder;
	@Autowired @Qualifier("JavaTestBuilder")
	TestBuilder testBuilder;
	@Autowired @Qualifier("JavaViewBuilder")
	ViewBuilder viewBuilder;
	@Autowired @Qualifier("JavaServiceBuilder")
	ServiceBuilder serviceBuilder;
	@Autowired @Qualifier("JavaRestClientBuilder")
	RestClientBuilder restClientBuilder;
	
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
	public ServiceBuilder getServiceBuilder() {
		return serviceBuilder;
	}
	
	@Override
	public RestClientBuilder getRestClientBuilder() {
		return restClientBuilder;
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