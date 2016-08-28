package com.intere.generator.builder.orchestration.language;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.intere.generator.builder.generation.utils.SwiftDataGenerator;
import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.orchestration.language.utility.RubyLanguageUtility;
import com.intere.generator.metadata.CustomClass;
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

@Service(value="RubyOrchestration")
public class RubyOrchestration implements LanguageOrchestrator {
	private static final Logger LOGGER = LogManager.getLogger(RubyOrchestration.class);
	
	@Autowired
	@Qualifier("RubyLanguage")
	LanguageUtility languageUtil = new RubyLanguageUtility();

	@Autowired @Qualifier("RubyInterpreter")
	JsonLanguageInterpreter interpreter;

	@Autowired
	TemplateConfig template;

	@Override
	public List<File> generateCustomClasses(File outputDirectory, OrchestrationTree tree, File templateSourceDir, String templateFile) throws IOException {
		throw new IOException("Not yet implemented");
	}

	@Override
	public List<File> generateModels(File outputDirectory, OrchestrationTree tree) throws IOException {
		List<File> generatedClasses = new ArrayList<>();
		
		for(CustomClass modelClass : tree.getModelClasses()) {
			try {
				generatedClasses.add(buildModelClassFile(outputDirectory, modelClass));
			} catch (TemplateException ex) {
				throw new IOException(ex);
			}
		}
		return generatedClasses;
	}

	@Override
	public List<File> generateModelUnitTests(File testPath, OrchestrationTree tree) throws IOException {
		List<File> generatedSpecs = new ArrayList<>();
		languageUtil.enforceFilenames(tree);
		for(CustomClass modelClass : tree.getModelClasses()) {
			try {
				generatedSpecs.add(buildSpecFiles(testPath, modelClass));
			} catch (TemplateException ex) {
				throw new IOException(ex);
			}
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
	public void review(OrchestrationTree tree) {
		languageUtil.enforceFilenames(tree);
		languageUtil.enforcePropertyMappings(tree);
	}

	private File buildModelClassFile(File outputDirectory, CustomClass modelClass) throws IOException, TemplateException {
		File outputFile = new File(outputDirectory, modelClass.getFileName() + ".rb");
		LOGGER.info("About to create Model Class: " + outputFile.getAbsolutePath());

		Map<String, Object> model = buildFreemarkerModel(modelClass, modelClass.getClassName(), null, null);
		template.generateFile(model, "RubyClass.ftlh", new FileWriter(outputFile));

		return outputFile;
	}

	private File buildSpecFiles(File testPath, CustomClass modelClass) throws IOException, TemplateException {
		File outputFile = new File(testPath, modelClass.getFileName() + "_spec.rb");
		LOGGER.info("About to create Spec File: " + outputFile.getAbsolutePath());

		Map<String, Object> model = buildFreemarkerModel(modelClass, modelClass.getClassName(), null, null);
		template.generateFile(model, "RubyTestClass.ftlh", new FileWriter(outputFile));

		return outputFile;
	}

	private Map<String, Object> buildFreemarkerModel(CustomClass modelClass, String classname, String prefix, String suffix) {
		Map<String, Object> model = new HashMap<>();

		model.put("date", new Date());
		model.put("model", modelClass);
		model.put("filename", modelClass.getFileName() + ".rb");
		model.put("properties", getProperties(modelClass));
		model.put("generator", new SwiftDataGenerator());

		model.put("classname", null != classname ? classname : modelClass.getFileName());
		model.put("prefix", null != prefix ? prefix : "");
		model.put("suffix", null != suffix ? suffix : "");

		return model;
	}

	@Override
	public List<File> copyModelResources(File sourcePath, OrchestrationTree tree) throws IOException {
		return null;
	}
	
	@Override
	public List<File> copyViewResources(File viewPath, OrchestrationTree tree) throws IOException {
		// TODO Auto-generated method stub
		return null;
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
}
