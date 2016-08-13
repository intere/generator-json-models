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
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility.ServiceBuilder;
import com.intere.generator.metadata.ModelClass;

@Service(value="ObjectiveCOrchestration")
public class ObjectiveCOrchestration implements LanguageOrchestrator {
	private static final Logger LOGGER = LogManager.getLogger(ObjectiveCOrchestration.class);
	
	@Autowired @Qualifier("ObjectiveCLanguage")
	LanguageUtility languageUtil;

	@Override
	public List<File> generateCustomClasses(File outputDirectory, OrchestrationTree tree, File templateSourceDir, String templateFile) throws IOException {
		throw new IOException("Not Yet Implemented");
	}

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
		List<File> generatedResources = new ArrayList<>();
		Map<File, String> resources = languageUtil.copyViewResources(viewPath, tree);
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
	public List<File> generateModelUnitTests(File outputDirFile, OrchestrationTree tree) throws IOException {
		List<File> generatedClasses = new ArrayList<>();
		for(ModelClass modelClass : tree.getModelClasses()) {
			generatedClasses.add(buildTestFile(outputDirFile, modelClass));
		}
		return generatedClasses;
	}

	@Override
	public List<File> generateServices(File servicePath, OrchestrationTree tree) throws IOException {
		List<File> generatedServices = new ArrayList<>();
		for(ModelClass modelClass : tree.getModelClasses()) {
			generatedServices.add(buildServiceHeaderFile(servicePath, modelClass));
			generatedServices.add(buildServiceImplementationFile(servicePath, modelClass));
		}
		return generatedServices;
	}

	@Override
	public List<File> generateViews(File viewPath, OrchestrationTree tree) throws IOException {
		List<File> viewClasses = new ArrayList<>();
		for(ModelClass modelClass : tree.getModelClasses()) {
			viewClasses.add(buildViewHeaderFile(viewPath, modelClass));
			viewClasses.add(buildViewImplementationFile(viewPath, modelClass));
			viewClasses.add(buildSingleControllerHeaderFile(viewPath, modelClass));
			viewClasses.add(buildSingleControllerImplementationFile(viewPath, modelClass));
			viewClasses.add(buildListControllerHeaderFile(viewPath, modelClass));
			viewClasses.add(buildListControllerImplementationFile(viewPath, modelClass));
		}
		return viewClasses;
	}

	@Override
	public List<File> generateRestServices(File restPath, OrchestrationTree tree) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void review(OrchestrationTree tree) {
		languageUtil.enforcePropertyMappings(tree);
	}
	
	/**
	 * Builds the Service Class Header File (in the provided output directory).
	 */
	private File buildServiceHeaderFile(File outputDirectory, ModelClass modelClass) throws IOException {
		String fileContents = buildServiceClassHeader(modelClass);
		File outputFile = new File(outputDirectory, modelClass.getServiceClassName() + ".h");
		LOGGER.info("About to create Service Class: " + outputFile.getAbsolutePath());
		FileOutputStream fout = new FileOutputStream(outputFile);
		IOUtils.write(fileContents, fout);
		fout.close();
		return outputFile;
	}

	/**
	 * Builds the Service Class Implementation File (in the provided output directory).
	 */
	private File buildServiceImplementationFile(File outputDirectory, ModelClass modelClass) throws IOException {
		String fileContents = buildServiceClassImplementation(modelClass);
		File outputFile = new File(outputDirectory, modelClass.getServiceClassName() + ".m");
		LOGGER.info("About to create Service Class: " + outputFile.getAbsolutePath());
		FileOutputStream fout = new FileOutputStream(outputFile);
		IOUtils.write(fileContents, fout);
		fout.close();
		return outputFile;
	}

	/**
	 * Builds the Test Class File (in the provided output directory).
	 * @param outputDirectory
	 * @param modelClass
	 * @return
	 */
	private File buildTestFile(File outputDirectory, ModelClass modelClass) throws IOException {
		String fileContents = buildTestClass(modelClass);
		File outputFile = new File(outputDirectory, modelClass.getTestClassName() + ".m");
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
	
	private File buildViewHeaderFile(File viewPath, ModelClass modelClass) throws IOException {
		String fileContents = buildViewClassDeclaration(modelClass);
		File outputFile = new File(viewPath, modelClass.getViewClassName() + ".h");
		return writeFile(outputFile, fileContents);
	}
	
	private File buildViewImplementationFile(File viewPath, ModelClass modelClass) throws IOException {
		String fileContents = buildViewClassImplementation(modelClass);
		File outputFile = new File(viewPath, modelClass.getViewClassName() + ".m");
		return writeFile(outputFile, fileContents);
	}

	/** Builds the UIViewController Header File for a single model view. */
	private File buildSingleControllerHeaderFile(File viewPath, ModelClass modelClass) throws IOException {
		String fileContents = buildSingleControllerClassDeclaration(modelClass);
		File outputFile = new File(viewPath, modelClass.getSingleControllerName() + ".h");
		return writeFile(outputFile, fileContents);
	}
	
	/** Builds the UIViewController Implementation File for a single view model.  */
	private File buildSingleControllerImplementationFile(File viewPath,ModelClass modelClass) throws IOException {
		String fileContents = buildSingleControllerClassImplementation(modelClass);
		File outputFile = new File(viewPath, modelClass.getSingleControllerName() + ".m");
		return writeFile(outputFile, fileContents);
	}
	
	private File buildListControllerHeaderFile(File viewPath, ModelClass modelClass) throws IOException {
		String fileContents = buildListControllerClassDeclaration(modelClass);
		File outputFile = new File(viewPath, modelClass.getListControllerName() + ".h");
		return writeFile(outputFile, fileContents);
	}

	private File buildListControllerImplementationFile(File viewPath,ModelClass modelClass) throws IOException {
		String fileContents = buildListControllerClassImplementation(modelClass);
		File outputFile = new File(viewPath, modelClass.getListControllerName() + ".m");
		return writeFile(outputFile, fileContents);
	}

	private String buildServiceClassHeader(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		ServiceBuilder serviceBuilder = languageUtil.getServiceBuilder();
		builder.append(serviceBuilder.buildHeaderFileComment(modelClass));
		builder.append(serviceBuilder.buildClassDeclaration(modelClass));
		builder.append(serviceBuilder.buildPropertyDeclarations(modelClass));
		builder.append(serviceBuilder.buildModelUtilityDeclarationMethods(modelClass));
		builder.append(serviceBuilder.finishClass(modelClass));
		return builder.toString();
	}
	
	private String buildServiceClassImplementation(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		ServiceBuilder serviceBuilder = languageUtil.getServiceBuilder();
		builder.append(serviceBuilder.buildImplementationFileComment(modelClass));
		builder.append(serviceBuilder.buildClassImplementation(modelClass));
		builder.append(serviceBuilder.buildViewUtilityDefinitionMethods(modelClass));
		builder.append(serviceBuilder.finishClass(modelClass));
		return builder.toString();
	}

	private String buildViewClassDeclaration(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(languageUtil.getViewBuilder().buildHeaderFileComment(modelClass));
		builder.append(languageUtil.getViewBuilder().buildImports(modelClass));
		builder.append(languageUtil.getViewBuilder().buildClassDeclaration(modelClass));
		builder.append(languageUtil.getViewBuilder().finishClass(modelClass));
		return builder.toString();
	}
	
	private String buildViewClassImplementation(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(languageUtil.getViewBuilder().buildImplementationFileComment(modelClass));
		builder.append(languageUtil.getViewBuilder().buildClassImplementation(modelClass));
//		builder.append(languageUtil.getViewBuilder().buildGettersAndSetters(modelClass));
		builder.append(languageUtil.getViewBuilder().buildViewUtilityDefinitionMethods(modelClass));
		builder.append(languageUtil.getViewBuilder().finishClass(modelClass));
		return builder.toString();
	}
	
	private String buildSingleControllerClassDeclaration(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(languageUtil.getSingleViewControllerBuilder().buildHeaderFileComment(modelClass));
		builder.append(languageUtil.getSingleViewControllerBuilder().buildImports(modelClass));
		builder.append(languageUtil.getSingleViewControllerBuilder().buildClassDeclaration(modelClass));
		builder.append(languageUtil.getSingleViewControllerBuilder().buildUtilityDeclarationMethods(modelClass));
		builder.append(languageUtil.getSingleViewControllerBuilder().finishClass(modelClass));
		return builder.toString();
	}

	private String buildSingleControllerClassImplementation(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(languageUtil.getSingleViewControllerBuilder().buildImplementationFileComment(modelClass));
		builder.append(languageUtil.getSingleViewControllerBuilder().buildClassImplementation(modelClass));
		builder.append(languageUtil.getSingleViewControllerBuilder().buildUtilityDefinitionMethods(modelClass));
		builder.append(languageUtil.getSingleViewControllerBuilder().finishClass(modelClass));
		return builder.toString();
	}

	private String buildListControllerClassDeclaration(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(languageUtil.getListViewControllerBuilder().buildHeaderFileComment(modelClass));
		builder.append(languageUtil.getListViewControllerBuilder().buildImports(modelClass));
		builder.append(languageUtil.getListViewControllerBuilder().buildClassDeclaration(modelClass));
		builder.append(languageUtil.getListViewControllerBuilder().buildUtilityDeclarationMethods(modelClass));
		builder.append(languageUtil.getListViewControllerBuilder().finishClass(modelClass));
		return builder.toString();
	}

	private String buildListControllerClassImplementation(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(languageUtil.getListViewControllerBuilder().buildImplementationFileComment(modelClass));
		builder.append(languageUtil.getListViewControllerBuilder().buildClassImplementation(modelClass));
		builder.append(languageUtil.getListViewControllerBuilder().buildUtilityDefinitionMethods(modelClass));
		builder.append(languageUtil.getListViewControllerBuilder().finishClass(modelClass));
		return builder.toString();
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
		builder.append(languageUtil.getModelBuilder().buildHeaderFileComment(modelClass));
		builder.append(languageUtil.getModelBuilder().buildImports(modelClass));
		builder.append(languageUtil.getModelBuilder().buildClassDeclaration(modelClass));
		builder.append(languageUtil.getModelBuilder().buildPropertyDeclarations(modelClass));
		builder.append(languageUtil.getModelBuilder().buildModelUtilityDeclarationMethods(modelClass));
		builder.append(languageUtil.getModelBuilder().finishClass(modelClass));
		return builder.toString();
	}

	/**
	 * Builds the implementation file for a given model class.
	 * @param modelClass
	 * @return
	 */
	private String buildClassImplementation(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(languageUtil.getModelBuilder().buildImplementationFileComment(modelClass));
		builder.append(languageUtil.getModelBuilder().buildSerializationConstants(modelClass));
		builder.append(languageUtil.getModelBuilder().buildClassImplementation(modelClass));
		builder.append(languageUtil.getModelBuilder().buildViewUtilityDefinitionMethods(modelClass));
		builder.append(languageUtil.getModelBuilder().finishClass(modelClass));
		
		return builder.toString();
	}
	
	private String buildTestClass(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(languageUtil.getTestBuilder().buildImplementationFileComment(modelClass));
		builder.append(languageUtil.getTestBuilder().buildTestImports(modelClass));
		builder.append(languageUtil.getTestBuilder().buildTestClassDeclaration(modelClass));
		builder.append(languageUtil.getTestBuilder().buildTestSetupMethod(modelClass));
		builder.append(languageUtil.getTestBuilder().buildTestMethods(modelClass));
		builder.append(languageUtil.getTestBuilder().finishClass(modelClass));
		
		return builder.toString();
	}
}
