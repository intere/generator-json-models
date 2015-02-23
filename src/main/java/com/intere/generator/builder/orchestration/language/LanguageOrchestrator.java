package com.intere.generator.builder.orchestration.language;

import java.io.File;
import java.io.IOException;

import com.intere.generator.builder.orchestration.OrchestrationTree;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

public interface LanguageOrchestrator {

	/** Generates the models for this language.  
	 * @param outputDirectory 
	 * @throws IOException */
	void generateModels(File outputDirectory, OrchestrationTree tree) throws IOException;
	
	public interface LanguageUtility {
		String tabs(int tabCount);
		String singleLineComment(String comment);
		String singleLineComment(String comment, int tabCount);
		String multiLineComment(String comment);
		String multiLineComment(String comment, int tabCount);
		String buildNamespace(ModelClass modelClass);
		String buildFileComment(ModelClass modelClass, String extension);
		String buildClassDeclaration(ModelClass modelClass);
		String finishClass(ModelClass modelClass);
		String buildPropertyDeclarations(ModelClass modelClass);
		String buildSinglePropertyDeclaration(ModelClassProperty property);
		String buildGettersAndSetters(ModelClass modelClass);
		String buildGetterAndSetter(ModelClassProperty prop);
		String getPropertyType(ModelClassProperty property);
		String buildImports(ModelClass modelClass);
		String buildClassImplementation(ModelClass modelClass);
		String buildSerializationConstants(ModelClass modelClass);
		String buildModelUtilityDeclarationMethods(ModelClass modelClass);
		String buildModelUtilityDefinitionMethods(ModelClass modelClass);
	}
}
