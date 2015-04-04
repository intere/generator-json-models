package com.intere.generator.builder.orchestration.language;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.intere.generator.builder.orchestration.OrchestrationTree;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility;
import com.intere.generator.builder.orchestration.language.utility.RubyLanguageUtility;
import com.intere.generator.metadata.ModelClass;

@Service(value="RubyOrchestration")
public class RubyOrchestration implements LanguageOrchestrator {
	private static final Logger LOGGER = LogManager.getLogger(RubyOrchestration.class);
	
	@Autowired
	@Qualifier("RubyLanguage")
	LanguageUtility languageUtil = new RubyLanguageUtility();
		
	@Override
	public List<File> generateModels(File outputDirectory, OrchestrationTree tree) throws IOException {
		List<File> generatedClasses = new ArrayList<>();
		
		for(ModelClass modelClass : tree.getModelClasses()) {
			generatedClasses.add(buildModelClassFile(outputDirectory, modelClass));
		}
		return generatedClasses;
	}

	@Override
	public List<File> generateModelUnitTests(File testPath, OrchestrationTree tree) throws IOException {
		List<File> generatedSpecs = new ArrayList<>();
		languageUtil.enforceFilenames(tree);
		for(ModelClass modelClass : tree.getModelClasses()) {
			generatedSpecs.add(buildSpecFiles(testPath, modelClass));
		}
		return generatedSpecs;
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
	
	@Override
	public List<File> generateRestClients(File restClientPath, OrchestrationTree tree) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<File> copyRestClientResources(File restClientPath, OrchestrationTree tree) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void review(OrchestrationTree tree) {
		languageUtil.enforceFilenames(tree);
		languageUtil.enforcePropertyMappings(tree);
	}


	private File buildModelClassFile(File outputDirectory, ModelClass modelClass) throws IOException {
		String fileContents = buildModelClass(modelClass);
		File outputFile = new File(outputDirectory, modelClass.getFileName() + ".rb");
		LOGGER.info("About to create Model Class: " + outputFile.getAbsolutePath());
		FileOutputStream fout = new FileOutputStream(outputFile);
		IOUtils.write(fileContents, fout);
		fout.close();
		return outputFile;
	}
	
	private File buildSpecFiles(File testPath, ModelClass modelClass) throws IOException {
		String fileContents = buildSpecFile(modelClass);
		File outputFile = new File(testPath, modelClass.getFileName() + "_spec.rb");
		LOGGER.info("About to create Spec File: " + outputFile.getAbsolutePath());
		FileOutputStream fout = new FileOutputStream(outputFile);
		IOUtils.write(fileContents, fout);
		fout.close();
		return outputFile;
	}

	private String buildModelClass(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(languageUtil.getModelBuilder().buildNamespace(modelClass));
		builder.append(languageUtil.getModelBuilder().buildImplementationFileComment(modelClass));
		builder.append(languageUtil.getModelBuilder().buildImports(modelClass));
		builder.append(languageUtil.getModelBuilder().buildClassDeclaration(modelClass));
		builder.append(languageUtil.getModelBuilder().buildPropertyDeclarations(modelClass));
		builder.append(languageUtil.getModelBuilder().buildGettersAndSetters(modelClass));
		builder.append(languageUtil.getModelBuilder().buildViewUtilityDefinitionMethods(modelClass));
		builder.append(languageUtil.getModelBuilder().finishClass(modelClass));
		return builder.toString();
	}
	
	private String buildSpecFile(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(languageUtil.getTestBuilder().buildImplementationFileComment(modelClass));
		builder.append(languageUtil.getTestBuilder().buildTestImports(modelClass));
		builder.append(languageUtil.getTestBuilder().buildTestClassDeclaration(modelClass));
		builder.append(languageUtil.getTestBuilder().buildTestSetupMethod(modelClass));
		builder.append(languageUtil.getTestBuilder().buildTestMethods(modelClass));
		builder.append(languageUtil.getTestBuilder().finishClass(modelClass));
		return builder.toString();
	}
	

	@Override
	public List<File> copyModelResources(File sourcePath, OrchestrationTree tree) throws IOException {
		List<File> generatedResources = new ArrayList<>();
		Map<File, String> resources = languageUtil.copyModelResources(sourcePath, tree);
		for(File f : resources.keySet()) {
			LOGGER.info("About to copy resource: " + f.getAbsolutePath());
			FileOutputStream fout = new FileOutputStream(f);
			IOUtils.write(resources.get(f), fout);
			fout.close();
			generatedResources.add(f);
		}
		return generatedResources;
	}
	
	@Override
	public List<File> copyViewResources(File viewPath, OrchestrationTree tree) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
