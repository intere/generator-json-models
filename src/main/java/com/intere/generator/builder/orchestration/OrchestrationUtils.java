package com.intere.generator.builder.orchestration;

import static com.intere.generator.deserializer.JsonNodeUtils.isArray;
import static com.intere.generator.deserializer.JsonNodeUtils.isArrayOfObjects;
import static com.intere.generator.deserializer.JsonNodeUtils.isObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.intere.generator.builder.interpreter.models.SwiftModelInterpreter;
import com.intere.generator.builder.orchestration.language.*;
import com.intere.generator.metadata.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.intere.generator.Language;
import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.models.JavaModelInterpreter;
import com.intere.generator.builder.interpreter.models.ObjectiveCModelInterpreter;
import com.intere.generator.builder.interpreter.models.RubyModelInterpreter;
import com.intere.generator.deserializer.JsonNodeUtils;
import org.springframework.util.Assert;

public class OrchestrationUtils {
	private static final Logger LOGGER = LogManager.getLogger(OrchestrationUtils.class);
	
	/**
	 * This method is responsible for taking the provided {@link Metadata}, {@link MetadataClasses} and {@link JsonNode} objects and rectifying them all together 
	 * to build a collection of {@link ModelClass} objects for you. 
	 * @param metadata The {@link Metadata} object.
	 * @param clazz The {@link MetadataClasses} object.
	 * @param node The {@link JsonNode} object (the JSON Model that ultimately drives the bulk of the code creation).
	 * @param prefix
	 *@param suffix @return A Collection of {@link ModelClass} objects.
	 */
	public static List<CustomClass> readBuildClasses(Metadata metadata, MetadataClasses clazz, JsonNode node, String prefix, String suffix) {
		return createAndPopulateModelClass(metadata, clazz, clazz.getClassName(), node, clazz.getUrlPath(), prefix, suffix);
	}
	
	/**
	 * This method is responsible for providing you with a {@link LanguageOrchestrator} object based on what the provided {@link Metadata} object requires.
	 * @param metadata
	 * @return
	 */
	public static LanguageOrchestrator getLanguageOrchestrator(Metadata metadata) {
		Language lang = Language.fromFullName(metadata.getLanguage());
		switch(lang) {
		case Java:
			return new JavaOrchestration();

		case ObjC:
			return new ObjectiveCOrchestration();
			
		case Ruby:
			return new RubyOrchestration();

		case Swift:
			return new SwiftOrchestration();
			
		default:
			LOGGER.warn("No Language Orchestrator for language: " + lang.getFullName());
			return null;
		}
	}

	private static Collection<CustomClass> getSubClasses(JsonLanguageInterpreter interpreter, Metadata metadata, MetadataClasses clazz, JsonNode node, String className, String prefix, String suffix) {
		List<CustomClass> modelClasses = new ArrayList<>();
		
		Iterator<String> iter = node.getFieldNames();
		while(iter.hasNext()) {
			String name = iter.next();
			JsonNode child = node.get(name);
			String childClassName = interpreter.buildSubClassName(className, name);
			if(isObject(child)) {
				modelClasses.addAll(createAndPopulateModelClass(metadata, clazz, childClassName, child, null, prefix, suffix));
			} else if(isArrayOfObjects(child)) {
				modelClasses.addAll(createAndPopulateModelClass(metadata, clazz, childClassName, child.iterator().next(), null, prefix, suffix));
			}
		}
		
		return modelClasses;
	}

	private static Collection<ModelClassProperty> populateProperties(JsonLanguageInterpreter interpreter, String className, Metadata metadata, MetadataClasses clazz, JsonNode node, ModelClass modelClass) {
		List<ModelClassProperty> properties = new ArrayList<>();
		Iterator<String> iter = node.getFieldNames();
		while(iter.hasNext()) {
			String name = iter.next();
			JsonNode child = node.get(name);
			ModelClassProperty property = new ModelClassProperty();
			property.setName(name);
			property.setParentModel(modelClass);
			configureNodeType(interpreter, className, name, child, property);
			properties.add(property);

			assert null != property.getIsPrimitive();
			assert null != property.getIsArray();
			assert null != property.getAlias();
			assert null != property.getName();
			assert null != property.getIsKey();
			assert null != property.getIsReadonly();
			assert null != property.getIsTransient();
			assert null != property.getIsVisible();
			assert null != property.getType();

		}		
		return properties;
	}
	
	private static List<ModelClassProperty> addTransientProperties(MetadataClasses parent, ModelClass clazz) {
		List<ModelClassProperty> props = new ArrayList<>();
		for(MetadataClassesTransientProperty prop : parent.getTransientProperty()) {
			if(null == prop.getClassName() || 0 == prop.getClassName().trim().length()) {
				LOGGER.error("Found Transient Property without a class name");
				continue;
			}
			if(null != prop.getName() && null != prop.getType() && prop.getName().trim().length()>0 && prop.getType().trim().length()>0) {
				if(clazz.getClassName().equals(prop.getClassName())) {
					ModelClassProperty property = new ModelClassProperty();
					property.setIsTransient(true);
					property.setName(prop.getName());
					property.setType(prop.getType());
					property.setIsArray(false);
					property.setIsKey(false);
					property.setDataType(OrchestrationDataType.fromModelProperty(property));
					props.add(property);
				}
			} else {
				if(prop.getName().trim().length()==0) {
					LOGGER.warn(clazz.getClassName() + " has a transient property without a name");
				}
				if(prop.getType().trim().length()==0) {
					LOGGER.warn(clazz.getClassName() + " has a transient property without a class name");
				}
			}
		}
		
		return props;
	}
	
	private static Collection<? extends String> getSummaryProperties(MetadataClasses clazz, ModelClass model) {
		for(MetadataClassesListSummary prop : clazz.getListSummary()) {
			if(prop.getClassName().equals(model.getClassName())) {
				return prop.getPropertyList();
			}
		}
		return new ArrayList<>();
	}
	
	/**
	 * This method is responsible for filling in the details of the Property object you pass to it.  It uses delegation to fill in the data for Array Sub Type properties too.
	 * @param interpreter
	 * @param className
	 * @param name
	 * @param child
	 * @param property
	 */
	private static void configureNodeType(JsonLanguageInterpreter interpreter, String className, String name, JsonNode child, ModelClassProperty property) {
		property.setIsArray(false);
		property.setIsKey(false);
		property.setIsPrimitive(false);
		property.setIsReadonly(false);
		property.setIsTransient(false);
		property.setIsVisible(true);
		property.setType(getNodeType(interpreter, child, className, name));
		property.setIsArray(isArray(child));
		property.setIsPrimitive(isPrimitive(property));
		property.setDataType(OrchestrationDataType.fromModelProperty(property));
		property.setValue(child.getTextValue());
		property.setAlias(interpreter.cleanVariableName(name));
		if(property.getDataType() == OrchestrationDataType.ARRAY) {
			property.setArraySubTypeProperty(getArraySubtypeProperty(interpreter, className, name, child.get(0)));
		}
		
		if(isArray(child)) {
			property.setArraySubType(getNodeType(interpreter, child.iterator().next(), className, name));
		}
	}
	
	private static ModelClassProperty getArraySubtypeProperty(JsonLanguageInterpreter interpreter, String className, String name, JsonNode child) {
		ModelClassProperty property = new ModelClassProperty();
		configureNodeType(interpreter, className, name, child, property);
		return property;
	}

	private static Boolean isPrimitive(ModelClassProperty property) {
		switch(property.getType()) {
		case "Boolean":
		case "Long":
		case "Double":
			return true;
			
		default:
			return false;
		}
	}

	private static String getNodeType(JsonLanguageInterpreter interpreter, JsonNode node, String className, String name) {
		String subClass = interpreter.buildSubClassName(className, name);
		OrchestrationDataType type = OrchestrationDataType.fromJsonNode(node);
		
		switch(type) {
		case CLASS:
			return subClass;
			
		case UNKNOWN:
			return OrchestrationDataType.STRING.getInternalName();
			
		default:
			return type.getInternalName();
		}
	}

	/**
	 * This method is responsible for the creation and population of the Model Classes.
	 * @param metadata
	 * @param clazz
	 * @param className
	 * @param node
	 * @param restUrl
	 * @return
	 */
	private static List<CustomClass> createAndPopulateModelClass(Metadata metadata, MetadataClasses clazz, String className, JsonNode node, String restUrl, String prefix, String suffix) {
		JsonLanguageInterpreter interpreter = getInterpreterFromMetadata(metadata);
		List<CustomClass> modelClasses = new ArrayList<>();
		ModelClass model = new ModelClass();
		model.setClassName(className);
		model.setHasSubClasses(hasSubClasses(node));
		model.setNamespace(metadata.getNamespace());
		model.setReadonly(clazz.getReadonly());
		model.setRestUrl(restUrl);
		model.getProperty().addAll(populateProperties(interpreter, className, metadata, clazz, node, model));
		model.getProperty().addAll(addTransientProperties(clazz, model));
		model.getImports().addAll(getImports(clazz, model));
		model.getSummaryProperties().addAll(getSummaryProperties(clazz, model));
		model.setFileName(interpreter.buildFilenameFromClassname(className));
		model.setTestClassName(interpreter.buildClassName(className) + "Test");
		model.setViewClassName(interpreter.buildClassName(className) + "View");
		model.setSingleControllerName(interpreter.buildClassName(className) + "ViewController");
		model.setListControllerName(interpreter.buildClassName(className) + "TableViewController");
		model.setServiceClassName(interpreter.buildClassName(className) + "Service");
		model.setRestServiceClassName(interpreter.buildClassName(className) + "RestService");
		modelClasses.add(new CustomClass(model, customClassName(className, prefix, suffix)));
		if(model.getHasSubClasses()) {
			modelClasses.addAll(getSubClasses(interpreter, metadata, clazz, node, model.getClassName(), prefix, suffix));
		}
		
		return modelClasses;
	}

	private static Collection<? extends ModelClassImports> getImports(MetadataClasses clazz, ModelClass model) {
		List<ModelClassImports> importList = new ArrayList<>();
		for(MetadataClassesImports mImport : clazz.getImports()) {
			if(mImport.getTargetClassName().equals(model.getClassName())) {
				ModelClassImports theImport = new ModelClassImports();
				theImport.setImportName(mImport.getImportName());
				theImport.setTargetClassName(mImport.getTargetClassName());
				importList.add(theImport);
			}
		}
		return importList;
	}

	private static JsonLanguageInterpreter getInterpreterFromMetadata(Metadata metadata) {
		Language lang = Language.fromFullName(metadata.getLanguage());
		switch(lang) {
		case Java:
			return new JavaModelInterpreter();
			
		case ObjC:
			return new ObjectiveCModelInterpreter();
			
		case Ruby:
			return new RubyModelInterpreter();

		case Swift:
			return new SwiftModelInterpreter();
			
			default:
				LOGGER.error("ERROR: No Interpreter for type: " + lang.name());
				return null;
		}
	}

	private static Boolean hasSubClasses(JsonNode node) {
		Iterator<JsonNode> iter = node.getElements();
		while(iter.hasNext()) {
			JsonNode child = iter.next();
			if(JsonNodeUtils.isObject(child) || JsonNodeUtils.isArrayOfObjects(child)) {
				return true;
			}
		}
		return false;
	}

	private static String customClassName(String className, String prefix, String suffix) {
		String customName = className;
		if(null != prefix && !prefix.isEmpty()) {
			customName = prefix + customName;
		}
		if(null != suffix && !suffix.isEmpty()) {
			customName = customName + suffix;
		}

		if(!className.equals(customName)) {
			return customName;
		}

		return null;
	}

}
