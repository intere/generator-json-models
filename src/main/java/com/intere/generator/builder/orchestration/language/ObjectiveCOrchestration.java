package com.intere.generator.builder.orchestration.language;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.intere.generator.builder.orchestration.OrchestrationTree;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility;
import com.intere.generator.builder.orchestration.language.utility.ObjectiveCLanguageUtility;
import com.intere.generator.metadata.ModelClass;

public class ObjectiveCOrchestration implements LanguageOrchestrator {
	private static final Logger LOGGER = LogManager.getLogger(ObjectiveCOrchestration.class);
	LanguageUtility languageUtil = new ObjectiveCLanguageUtility();
	
	@Override
	public List<File> generateModels(File outputDirectory, OrchestrationTree tree) throws IOException {
		List<File> generatedClasses = new ArrayList<>();
		for(ModelClass modelClass : tree.getModelClasses()) {
			generatedClasses.add(buildModelHeaderFile(outputDirectory, modelClass));
			generatedClasses.add(buildModelImplementationFile(outputDirectory, modelClass));
		}
		
		return generatedClasses;
	}

	@Override
	public List<File> generateModelUnitTests(File outputDirFile, OrchestrationTree tree) throws IOException {
		List<File> generatedClasses = new ArrayList<>();
		for(ModelClass modelClass : tree.getModelClasses()) {
			generatedClasses.add(buildTestFile(outputDirFile, modelClass));
		}
		
		return generatedClasses;
	}

	@Override
	public List<File> generateServices(File servicePath, OrchestrationTree tree)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public List<File> generateViews(File viewPath, OrchestrationTree tree)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public List<File> generateRestServices(File restPath, OrchestrationTree tree) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Builds the Test Class File (in the provided output directory).
	 * @param outputDirectory
	 * @param modelClass
	 * @return
	 */
	private File buildTestFile(File outputDirectory, ModelClass modelClass) throws IOException {
		String fileContents = buildTestClass(modelClass);
		File outputFile = new File(outputDirectory, modelClass.getTestClassName() + ".java");
		LOGGER.info("About to create Test Class: " + outputFile.getAbsolutePath());
		FileOutputStream fout = new FileOutputStream(outputFile);
		IOUtils.write(fileContents, fout);
		fout.close();
		return outputFile;
	}

	/**
	 * Performs the generation / writing of the Model implementation file.
	 * @param outputDirectory Where to put the implementation file.
	 * @param modelClass The model class to build the implementation file from.
	 * @return
	 * @throws IOException
	 */
	private File buildModelImplementationFile(File outputDirectory, ModelClass modelClass) throws IOException {
		String fileContents = buildClassImplementation(modelClass);
		File outputFile = new File(outputDirectory, modelClass.getFileName() + ".m");
		return writeFile(outputFile, fileContents);
	}

	/**
	 * Performs the generation / writing of the Model Header file.
	 * @param outputDirectory Where to put the header file.
	 * @param modelClass The model class to build the header file from.
	 * @return
	 * @throws IOException
	 */
	private File buildModelHeaderFile(File outputDirectory, ModelClass modelClass) throws IOException {
		String fileContents = buildClassDeclaration(modelClass);
		File outputFile = new File(outputDirectory, modelClass.getFileName() + ".h");
		return writeFile(outputFile, fileContents);
	}

	/**
	 * Does the actual writing of the file for you.
	 * @param outputFile The file to write.
	 * @param fileContents The contents of the file to be written.
	 * @return The outputFile that was written.
	 * @throws IOException
	 */
	private File writeFile(File outputFile, String fileContents) throws IOException {
		LOGGER.info("About to create model class: " + outputFile.getAbsolutePath());
		FileOutputStream fout = new FileOutputStream(outputFile);
		IOUtils.write(fileContents, fout);
		fout.close();
		return outputFile;
	}

	/**
	 * Builds the header file for a given model class.
	 * @param modelClass
	 * @return
	 */
	private String buildClassDeclaration(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(languageUtil.buildFileComment(modelClass.getFileName() + ".h"));
		builder.append(languageUtil.buildImports(modelClass));
		builder.append(languageUtil.buildClassDeclaration(modelClass));
		builder.append(languageUtil.buildPropertyDeclarations(modelClass));
		builder.append(languageUtil.buildModelUtilityDeclarationMethods(modelClass));
		builder.append(languageUtil.finishClass(modelClass, false));
		return builder.toString();
	}

	/**
	 * Builds the implementation file for a given model class.
	 * @param modelClass
	 * @return
	 */
	private String buildClassImplementation(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(languageUtil.buildFileComment(modelClass.getFileName() + ".m"));
		builder.append(languageUtil.buildSerializationConstants(modelClass));
		builder.append(languageUtil.buildClassImplementation(modelClass));
		builder.append(languageUtil.buildModelUtilityDefinitionMethods(modelClass));
		builder.append(languageUtil.finishClass(modelClass, false));
		
		return builder.toString();
	}
	
	private String buildTestClass(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(languageUtil.buildFileComment(modelClass.getTestClassName() + ".m"));
		builder.append(languageUtil.buildTestImports(modelClass));
		builder.append(languageUtil.buildTestClassDeclaration(modelClass));
		builder.append(languageUtil.buildTestSetupMethod(modelClass));
		builder.append(languageUtil.buildTestMethods(modelClass));
		builder.append(languageUtil.finishClass(modelClass, true));
		
		return builder.toString();
	}
}
