package com.intere.generator.builder.orchestration.language;

import static com.intere.generator.io.FileIOUtils.ensureExists;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.orchestration.OrchestrationDataType;
import com.intere.generator.metadata.ModelClassImports;
import com.intere.generator.metadata.ModelClassProperty;
import com.intere.generator.metadata.models.LanguageModelClassProperty;
import com.intere.generator.templates.TemplateConfig;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.intere.generator.builder.orchestration.OrchestrationTree;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility;
import com.intere.generator.metadata.ModelClass;

@Service(value="JavaOrchestration")
public class JavaOrchestration implements LanguageOrchestrator {
	private static final Logger LOGGER = LogManager.getLogger(JavaOrchestration.class);

	@Autowired @Qualifier("JavaLanguage")
	LanguageUtility languageUtil;

	@Autowired @Qualifier("JavaInterpreter")
	JsonLanguageInterpreter interpreter;

	@Autowired TemplateConfig template;

	@Override
	public List<File> generateModels(File outputDirectory, OrchestrationTree tree) throws IOException {
		List<File> generatedClasses = new ArrayList<>();
		for(ModelClass modelClass : tree.getModelClasses()) {
			try {
				generatedClasses.add(buildModelClassFile(outputDirectory, modelClass));
			} catch (TemplateException e) {
				throw new IOException(e);
			}
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
	private File buildModelClassFile(File outputDirectory, ModelClass modelClass) throws IOException, TemplateException {
		File completePath = new File(outputDirectory, modelClass.getNamespace().replaceAll("\\.",  File.separator));
		if(ensureExists(completePath)) {

			File outputFile = new File(completePath, modelClass.getFileName() + ".java");
			LOGGER.info("About to create Model Class: " + outputFile.getAbsolutePath());

//			String fileContents = buildModelClass(modelClass);
//			FileOutputStream fout = new FileOutputStream(outputFile);
//			IOUtils.write(fileContents, fout);
//			fout.close();

			Map<String, Object> model = new HashMap<>();
			model.put("date", new Date());
			model.put("model", modelClass);
			model.put("filename", modelClass.getFileName() + ".java");
			model.put("imports", determineImports(modelClass));
			model.put("properties", getProperties(modelClass));

			template.generateFile(model, "JavaClass.ftlh", new FileWriter(outputFile));

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
		builder.append(languageUtil.getTestBuilder().buildNamespace(modelClass));
		builder.append(languageUtil.getTestBuilder().buildImplementationFileComment(modelClass));
		builder.append(languageUtil.getTestBuilder().buildTestImports(modelClass));
		builder.append(languageUtil.getTestBuilder().buildTestClassDeclaration(modelClass));
		builder.append(languageUtil.getTestBuilder().buildTestSetupMethod(modelClass));
		builder.append(languageUtil.getTestBuilder().buildTestMethods(modelClass));
		builder.append(languageUtil.getTestBuilder().finishClass(modelClass));
		
		return builder.toString();
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

	@Override
	public List<File> copyModelResources(File sourcePath, OrchestrationTree tree) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<File> copyViewResources(File viewPath, OrchestrationTree tree) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void review(OrchestrationTree tree) {
		languageUtil.enforcePropertyMappings(tree);
	}

	//
	// Helpers
	//

	private List<LanguageModelClassProperty> getProperties(ModelClass modelClass) {
		List<LanguageModelClassProperty> list = new ArrayList<>();
		for(ModelClassProperty prop : modelClass.getProperty()) {
			list.add(new LanguageModelClassProperty(prop, interpreter));
		}

		return list;
	}

	private Collection<String> determineImports(ModelClass modelClass) {

		Map<String, String> imports = new HashMap<>();

		imports.put("Serializable", "java.io.Serializable");

		for(ModelClassProperty prop : modelClass.getProperty()) {
			OrchestrationDataType type = OrchestrationDataType.fromModelProperty(prop);
			if(OrchestrationDataType.DATE == type) {
				imports.put("Date", "java.util.Date");
			} else if(OrchestrationDataType.ARRAY == type) {
				imports.put("List", "java.util.List");
				imports.put("ArrayList", "java.util.ArrayList");
			}
		}

		if(hasTransientProperties(modelClass)) {
			imports.put("JsonIgnore", "org.codehaus.jackson.annotate.JsonIgnore");
		}
		for(ModelClassImports theImport : modelClass.getImports()) {
			imports.put(theImport.getImportName(), theImport.getImportName());
		}

		return imports.values();
	}

	private boolean hasTransientProperties(ModelClass modelClass) {
		for(ModelClassProperty prop: modelClass.getProperty()) {
			if(prop.getIsTransient()) {
				return true;
			}
		}
		return false;
	}

}
