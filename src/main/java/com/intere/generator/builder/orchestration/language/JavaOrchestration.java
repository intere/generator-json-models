package com.intere.generator.builder.orchestration.language;

import static com.intere.generator.io.FileIOUtils.ensureExists;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.intere.generator.builder.orchestration.OrchestrationTree;
import com.intere.generator.builder.orchestration.language.utility.JavaLanguageUtility;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility;
import com.intere.generator.metadata.ModelClass;

public class JavaOrchestration implements LanguageOrchestrator {
	private static final Logger LOGGER = LogManager.getLogger(JavaOrchestration.class);
	LanguageUtility languageUtil = new JavaLanguageUtility();

	@Override
	public List<File> generateModels(File outputDirectory, OrchestrationTree tree) throws IOException {
		List<File> generatedClasses = new ArrayList<>();
		for(ModelClass modelClass : tree.getModelClasses()) {
			generatedClasses.add(buildModelClassFile(outputDirectory, modelClass));
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
	public List<File> generateServices(File servicePath, OrchestrationTree tree) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<File> generateViews(File viewPath, OrchestrationTree tree) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<File> generateRestServices(File restPath, OrchestrationTree tree) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Builds the Model Class File (in the provided output directory).
	 * @param outputDirectory
	 * @param modelClass
	 * @return
	 * @throws IOException
	 */
	private File buildModelClassFile(File outputDirectory, ModelClass modelClass) throws IOException {
		File completePath = new File(outputDirectory, modelClass.getNamespace().replaceAll("\\.",  File.separator));
		if(ensureExists(completePath)) {
			String fileContents = buildModelClass(modelClass);
			File outputFile = new File(completePath, modelClass.getFileName() + ".java");
			LOGGER.info("About to create Model Class: " + outputFile.getAbsolutePath());
			FileOutputStream fout = new FileOutputStream(outputFile);
			IOUtils.write(fileContents, fout);
			fout.close();
			return outputFile;
		} else {
			LOGGER.error("Could not create directory: " + completePath);
		}
		return null;
	}
	
	/**
	 * Builds the Test Class File (in the provided output directory).
	 * @param outputDirectory
	 * @param modelClass
	 * @return
	 */
	private File buildTestFile(File outputDirectory, ModelClass modelClass) throws IOException {
		File completePath = new File(outputDirectory, modelClass.getNamespace().replaceAll("\\.",  File.separator));
		if(ensureExists(completePath)) {
			String fileContents = buildTestClass(modelClass);
			File outputFile = new File(completePath, modelClass.getTestClassName() + ".java");
			LOGGER.info("About to create Test Class: " + outputFile.getAbsolutePath());
			FileOutputStream fout = new FileOutputStream(outputFile);
			IOUtils.write(fileContents, fout);
			fout.close();
			return outputFile;
		} else {
			LOGGER.error("Could not create directory: " + completePath);
		}
		return null;
	}

	private String buildTestClass(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(languageUtil.buildNamespace(modelClass));
		builder.append(languageUtil.buildFileComment(modelClass.getTestClassName() + ".java"));
		builder.append(languageUtil.buildTestImports(modelClass));
		builder.append(languageUtil.buildTestClassDeclaration(modelClass));
		builder.append(languageUtil.buildTestSetupMethod(modelClass));
		builder.append(languageUtil.buildTestMethods(modelClass));
		builder.append(languageUtil.finishClass(modelClass, true));
		
		return builder.toString();
	}

	private String buildModelClass(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(languageUtil.buildNamespace(modelClass));
		builder.append(languageUtil.buildFileComment(modelClass.getFileName() + ".java"));
		builder.append(languageUtil.buildImports(modelClass));
		builder.append(languageUtil.buildClassDeclaration(modelClass));
		builder.append(languageUtil.buildPropertyDeclarations(modelClass));
		builder.append(languageUtil.buildGettersAndSetters(modelClass));
		builder.append(languageUtil.buildModelUtilityDefinitionMethods(modelClass));
		builder.append(languageUtil.finishClass(modelClass, false));
		return builder.toString();
	}

	@Override
	public List<File> copyResources(File sourcePath, OrchestrationTree tree) {
		// TODO Auto-generated method stub
		return null;
	}

}
