package com.intere.generator.builder.orchestration.language.utility;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.intere.generator.builder.orchestration.OrchestrationTree;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

/**
 * The {@link LanguageUtility} class provides various helper methods for code generation for various languages.
 * @author einternicola
 *
 */
public interface LanguageUtility {

	
	/** Gets you the View Builder for this class. */
	ViewBuilder getViewBuilder();
	
	/** Gets you the Service Builder for this class.  */
	ServiceBuilder getServiceBuilder();
	
	/** Gets you the Single View Controller Builder for this class.  */
	SingleViewControllerBuilder getSingleViewControllerBuilder();
	
	/** Gets you the List View Controller Builder for this class.  */
	ListViewControllerBuilder getListViewControllerBuilder();
	
	/** Enforces the filenames based on the model class names.  */
	void enforceFilenames(OrchestrationTree tree);
	
	/** Enforces Property Name Mappings.  */
	void enforcePropertyMappings(OrchestrationTree tree);

	/** Copies the Model Resource files into the provided sourcePath. */
	Map<File, String> copyModelResources(File sourcePath, OrchestrationTree tree) throws IOException;
	
	/** Copies the View Resource files into the provided viewPath.  */
	Map<File, String> copyViewResources(File viewPath, OrchestrationTree tree) throws IOException;
	
	public interface CommentBuilder {
		/** Give me some number of tabs.  */
		String tabs(int tabCount);
		
		/** Give me a single line comment.  */
		String singleLineComment(String comment);
		
		/** Give me a single line comment - that starts with "n" tabs. */
		String singleLineComment(String comment, int tabCount);
		
		/** Give me a multi-line comment.  */
		String multiLineComment(String comment);
		
		/** Give me a multi-line comment that uses with "n" tabs.  */
		String multiLineComment(String comment, int tabCount);
		
		/** Build me a file comment for the provided filename.  */
		String buildFileComment(String filename);
	}
	
	public interface ModelBuilder {
		/** Give me a namespace definition for the provided model class.  */
		String buildNamespace(ModelClass modelClass);
		
		/** Builds the file comment for the header file for this class. */
		String buildHeaderFileComment(ModelClass modelClass);
		
		/** Builds the file comment for this class.  */
		String buildImplementationFileComment(ModelClass modelClass);
		
		/** Build the imports for the provided class.  */
		String buildImports(ModelClass modelClass);
		
		/** Build the Serialization Constants for the provided model class (Obj-C).  */
		String buildSerializationConstants(ModelClass modelClass);
		
		/** Build the class implementation for the provided class.  */
		String buildClassImplementation(ModelClass modelClass);
		
		/** Build the Utility Method declarations for this class.  */
		String buildModelUtilityDeclarationMethods(ModelClass modelClass);
		
		/** Build the Utility Method definitions for this class.  */
		String buildViewUtilityDefinitionMethods(ModelClass modelClass);

		/** Build me a class declaration for the provided model class.  */
		String buildClassDeclaration(ModelClass modelClass);
		
		/** Wrap up the class.  */
		String finishClass(ModelClass modelClass);
		
		/** Build the property declarations for the provided model class.  */
		String buildPropertyDeclarations(ModelClass modelClass);
		
		/** Build a single property declaration for the provided property.  */
		String buildSinglePropertyDeclaration(ModelClassProperty property);
		
		/** Build Getters and Setters for the provided model Class (Java).  */
		String buildGettersAndSetters(ModelClass modelClass);
		
		/** Build a Getter and Setter definition for the provided property (Java).  */
		String buildGetterAndSetter(ModelClassProperty prop);
		
		/** Get me the property type for the provided property.  */
		String getPropertyType(ModelClassProperty property);
	}
	
	public interface TestBuilder {
		/** Builds the Test Class Declaration. */
		String buildTestClassDeclaration(ModelClass modelClass);

		/** Builds the imports for the test class. */
		String buildTestImports(ModelClass modelClass);

		/** Builds the Setup Method for tests.  */
		String buildTestSetupMethod(ModelClass modelClass);
		
		/** Builds the test methods for this class.  */
		
		String buildTestMethods(ModelClass modelClass);
		/** Builds the file comment for this class.  */
		String buildImplementationFileComment(ModelClass modelClass);
		
		/** Wrap up the class.  */
		String finishClass(ModelClass modelClass);
		
		/** Give me a namespace definition for the provided model class.  */
		String buildNamespace(ModelClass modelClass);
	}
	
	public interface ViewBuilder {

		String buildHeaderFileComment(ModelClass modelClass);

		String buildImports(ModelClass modelClass);

		String buildClassDeclaration(ModelClass modelClass);

		String finishClass(ModelClass modelClass);

		String buildImplementationFileComment(ModelClass modelClass);

		String buildClassImplementation(ModelClass modelClass);

		String buildGettersAndSetters(ModelClass modelClass);

		String buildViewUtilityDefinitionMethods(ModelClass modelClass);		
	}
	
	public interface ServiceBuilder extends ModelBuilder {
		
	}
	
	public interface SingleViewControllerBuilder {

		String buildHeaderFileComment(ModelClass modelClass);

		String buildImports(ModelClass modelClass);

		String buildClassDeclaration(ModelClass modelClass);

		String buildUtilityDeclarationMethods(ModelClass modelClass);

		String finishClass(ModelClass modelClass);

		String buildImplementationFileComment(ModelClass modelClass);

		String buildClassImplementation(ModelClass modelClass);

		String buildUtilityDefinitionMethods(ModelClass modelClass);
		
	}
	
	public interface ListViewControllerBuilder {

		String buildHeaderFileComment(ModelClass modelClass);

		String buildImports(ModelClass modelClass);

		String buildClassDeclaration(ModelClass modelClass);

		String buildUtilityDeclarationMethods(ModelClass modelClass);

		String finishClass(ModelClass modelClass);

		String buildImplementationFileComment(ModelClass modelClass);

		String buildClassImplementation(ModelClass modelClass);

		String buildUtilityDefinitionMethods(ModelClass modelClass);
		
	}
}