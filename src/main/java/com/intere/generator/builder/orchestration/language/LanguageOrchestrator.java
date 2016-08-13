package com.intere.generator.builder.orchestration.language;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.intere.generator.builder.orchestration.OrchestrationTree;

public interface LanguageOrchestrator {

	/**
	 * Generates custom classes for this language.
	 * @param outputDirectory Where to put the Models
	 * @param templateSourceDir The directory to look within to find the template path
	 * @param templateFile The template file to use to generate the classes
	 * @return A list of files that were generated.
	 * @throws IOException
	 */
	List<File> generateCustomClasses(File outputDirectory, OrchestrationTree tree, File templateSourceDir, String templateFile) throws IOException;

	/** 
	 * Generates the models for this language.  
	 * @param outputDirectory Where to put the Models
	 * @return a List of files that were generated.
	 * @throws IOException 
	 */
	List<File> generateModels(File outputDirectory, OrchestrationTree tree) throws IOException;
	
	/**
	 * Generates the JUnit Tests for the models.
	 * @param outputDirectory Where to put the Unit Tests.
	 * @param tree The {@link OrchestrationTree} to use to build the tests from.
	 * @return a List of files that were generated.
	 * @throws IOException
	 */
	List<File> generateModelUnitTests(File outputDirectory, OrchestrationTree tree) throws IOException;

	/**
	 * Generates the Services for you using the provided {@link OrchestrationTree}.
	 * @param outputDirectory Where to put the Services.
	 * @param tree The {@link OrchestrationTree} to use to generate the services from.
	 * @return a List of files that were generated.
	 * @throws IOException
	 */
	List<File> generateServices(File outputDirectory, OrchestrationTree tree) throws IOException;

	/**
	 * Generates the Views for you, using the provided {@link OrchestrationTree}.
	 * @param outputDirectory Where to put the Views.
	 * @param tree The {@link OrchestrationTree} to use to generate the views path.
	 * @return a List of files that were generated.
	 * @throws IOException
	 */
	List<File> generateViews(File outputDirectory, OrchestrationTree tree) throws IOException;

	/**
	 * Generates the Rest Services for you using the provided {@link OrchestrationTree}.
	 * @param outputDirectory Where to put the Rest Services.
	 * @param tree The {@link OrchestrationTree} to use to generate the rest services.
	 * @return a List of files that were generated.
	 * @throws IOException
	 */
	List<File> generateRestServices(File outputDirectory, OrchestrationTree tree) throws IOException;

	/**
	 * Copies the resource files associated with the source (models).
	 * @param outputDirectory Where to copy the resource files to.
	 * @param tree The {@link OrchestrationTree} to help out (if necessary).
	 * @return a {@link List} of {@link File} objects.
	 * @throws IOException 
	 */
	List<File> copyModelResources(File outputDirectory, OrchestrationTree tree) throws IOException;

	/**
	 * Reviews all of the data and performs the last metadata overlays necessary for code generation.
	 * @param tree
	 */
	void review(OrchestrationTree tree);

	/**
	 * Copies the resource files associated with the views.
	 * @param viewPath Where to copy the resource files to.
	 * @param tree The {@link OrchestrationTree} to help out (if necessary).
	 * @throws IOException
	 */
	List<File> copyViewResources(File viewPath, OrchestrationTree tree) throws IOException;
}
