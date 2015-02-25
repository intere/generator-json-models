package com.intere.generator.builder.orchestration.language;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.intere.generator.builder.orchestration.OrchestrationTree;

public interface LanguageOrchestrator {

	/** 
	 * Generates the models for this language.  
	 * @param sourcePath Where to put the Models 
	 * @return a List of files that were generated.
	 * @throws IOException 
	 */
	List<File> generateModels(File sourcePath, OrchestrationTree tree) throws IOException;
	
	/**
	 * Generates the JUnit Tests for the models.
	 * @param testPath Where to put the Unit Tests.
	 * @param tree The {@link OrchestrationTree} to use to build the tests from.
	 * @return a List of files that were generated.
	 * @throws IOException
	 */
	List<File> generateModelUnitTests(File testPath, OrchestrationTree tree) throws IOException;

	/**
	 * Generates the Services for you using the provided {@link OrchestrationTree}.
	 * @param servicePath Where to put the Services.
	 * @param tree The {@link OrchestrationTree} to use to generate the services from.
	 * @return a List of files that were generated.
	 * @throws IOException
	 */
	List<File> generateServices(File servicePath, OrchestrationTree tree) throws IOException;

	/**
	 * Generates the Views for you, using the provided {@link OrchestrationTree}.
	 * @param viewPath Where to put the Views.
	 * @param tree The {@link OrchestrationTree} to use to generate the views path.
	 * @return a List of files that were generated.
	 * @throws IOException
	 */
	List<File> generateViews(File viewPath, OrchestrationTree tree) throws IOException;

	/**
	 * Generates the Rest Services for you using the provided {@link OrchestrationTree}.
	 * @param restPath Where to put the Rest Services.
	 * @param tree The {@link OrchestrationTree} to use to generate the rest services.
	 * @return a List of files that were generated.
	 * @throws IOException
	 */
	List<File> generateRestServices(File restPath, OrchestrationTree tree) throws IOException;
}
