package com.intere.generator.builder.orchestration;

import static com.intere.generator.deserializer.JsonNodeUtils.isArray;
import static com.intere.generator.deserializer.JsonNodeUtils.isArrayOfObjects;
import static com.intere.generator.deserializer.JsonNodeUtils.isObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.intere.generator.Language;
import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.models.JavaModelInterpreter;
import com.intere.generator.builder.interpreter.models.ObjectiveCModelInterpreter;
import com.intere.generator.builder.interpreter.models.RubyModelInterpreter;
import com.intere.generator.builder.orchestration.language.JavaOrchestration;
import com.intere.generator.builder.orchestration.language.LanguageOrchestrator;
import com.intere.generator.builder.orchestration.language.ObjectiveCOrchestration;
import com.intere.generator.builder.orchestration.language.RubyOrchestration;
import com.intere.generator.deserializer.JsonNodeUtils;
import com.intere.generator.metadata.Metadata;
import com.intere.generator.metadata.MetadataClasses;
import com.intere.generator.metadata.MetadataClassesImports;
import com.intere.generator.metadata.MetadataClassesListSummary;
import com.intere.generator.metadata.MetadataClassesPropertyMap;
import com.intere.generator.metadata.MetadataClassesTransientProperty;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassImports;
import com.intere.generator.metadata.ModelClassProperty;
import com.intere.generator.metadata.ModelClassRelatedClassList;

public class OrchestrationUtils {
	private static final Logger LOGGER = LogManager.getLogger(OrchestrationUtils.class);
	
	/**
	 * This method is responsible for taking the provided {@link Metadata}, {@link MetadataClasses} and {@link JsonNode} objects and rectifying them all together 
	 * to build a collection of {@link ModelClass} objects for you. 
	 * @param metadata The {@link Metadata} object.
	 * @param clazz The {@link MetadataClasses} object.
	 * @param node The {@link JsonNode} object (the JSON Model that ultimately drives the bulk of the code creation).
	 * @return A Collection of {@link ModelClass} objects.
	 */
	public static List<ModelClass> readBuildClasses(Metadata metadata, MetadataClasses clazz, JsonNode node) {
		return createAndPopulateModelClass(metadata, clazz, clazz.getClassName(), node, clazz.getUrlPath());
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
			
		default:
			LOGGER.warn("No Language Orchestrator for language: " + lang.getFullName());
			return null;
		}
	}

	private static Collection<ModelClass> getSubClasses(JsonLanguageInterpreter interpreter, Metadata metadata, MetadataClasses clazz, JsonNode node, String className) {
		List<ModelClass> modelClasses = new ArrayList<>();
		
		Iterator<String> iter = node.getFieldNames();
		while(iter.hasNext()) {
			String name = iter.next();
			JsonNode child = node.get(name);
			String childClassName = interpreter.buildSubClassName(className, name);
			if(isObject(child)) {
				modelClasses.addAll(createAndPopulateModelClass(metadata, clazz, childClassName, child, null));
			} else if(isArrayOfObjects(child)) {
				modelClasses.addAll(createAndPopulateModelClass(metadata, clazz, childClassName, child.iterator().next(), null));
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
			if(OrchestrationDataType.ARRAY == OrchestrationDataType.fromModelProperty(property)) {
				if(null != property.getArraySubType()) {
					property.setInitializer("new ArrayList<" + property.getArraySubType() + ">()");
				} else {
					property.setInitializer("new ArrayList<>()");
				}
			}			
			properties.add(property);
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
					property.setIsPrimitive(false);
					property.setDataType(OrchestrationDataType.fromModelProperty(property));
					property.setInitializer(prop.getInitializer());
					property.setParentModel(clazz);
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
	 */
	private static List<ModelClass> createAndPopulateModelClass(Metadata metadata, MetadataClasses clazz, String className, JsonNode node, String restUrl) {
		JsonLanguageInterpreter interpreter = getInterpreterFromMetadata(metadata);
		List<ModelClass> modelClasses = new ArrayList<>();
		ModelClass model = new ModelClass();
		model.setClassName(className);
		model.setHasSubClasses(hasSubClasses(node));
		model.setNamespace(metadata.getNamespace());
		model.setReadonly(clazz.getReadonly());
		model.setRestUrl(restUrl);
		model.getProperty().addAll(populateProperties(interpreter, className, metadata, clazz, node, model));
		model.getRelatedClassList().addAll(findRelatedClassList(model, clazz));
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
		model.setRestClientClassName(interpreter.buildClassName(className) + "RestClient");
		modelClasses.add(model);
		if(model.getHasSubClasses()) {
			modelClasses.addAll(getSubClasses(interpreter, metadata, clazz, node, model.getClassName()));
		}
		
		return modelClasses;
	}

	/**
	 * Reads the propertyMap from the metadata JSON and maps it to the appropriate model classes.  NOTE: This is only the first step, 
	 * to track those properties, later on, we need to select the classes.
	 */
	private static List<ModelClassRelatedClassList> findRelatedClassList(ModelClass model, MetadataClasses clazz) {
		List<ModelClassRelatedClassList> list = new ArrayList<>();
		for(MetadataClassesPropertyMap relation : clazz.getPropertyMap()) {
			if(relation.getMapClassTo().equals(model.getClassName())) {
				ModelClassRelatedClassList obj = new ModelClassRelatedClassList();
				obj.setMapClassFrom(relation.getMapClassFrom());
				obj.setMapClassTo(relation.getMapClassTo());
				obj.setMapPropertyFrom(relation.getMapPropertyFrom());
				obj.setMapPropertyTo(relation.getMapPropertyTo());
				list.add(obj);
			}
		}
		return list;
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

}
