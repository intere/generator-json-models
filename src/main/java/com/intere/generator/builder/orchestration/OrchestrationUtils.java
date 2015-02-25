package com.intere.generator.builder.orchestration;

import static com.intere.generator.deserializer.JsonNodeUtils.isArray;
import static com.intere.generator.deserializer.JsonNodeUtils.isArrayOfObjects;
import static com.intere.generator.deserializer.JsonNodeUtils.isObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;

import com.intere.generator.Language;
import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.models.JavaModelInterpreter;
import com.intere.generator.builder.interpreter.models.ObjectiveCModelInterpreter;
import com.intere.generator.builder.interpreter.models.RubyModelInterpreter;
import com.intere.generator.builder.orchestration.language.JavaOrchestration;
import com.intere.generator.builder.orchestration.language.LanguageOrchestrator;
import com.intere.generator.builder.orchestration.language.ObjectiveCOrchestration;
import com.intere.generator.deserializer.JsonNodeUtils;
import com.intere.generator.metadata.Metadata;
import com.intere.generator.metadata.MetadataClasses;
import com.intere.generator.metadata.MetadataClassesTransientProperty;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

public class OrchestrationUtils {

	public static List<ModelClass> readBuildClasses(Metadata metadata, MetadataClasses clazz, JsonNode node) {
		return createAndPopulateModelClass(metadata, clazz, clazz.getClassName(), node, clazz.getUrlPath());
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
				// TODO: Will this work?
				modelClasses.addAll(createAndPopulateModelClass(metadata, clazz, childClassName, child.iterator().next(), null));
			}
		}
		
		// TODO Auto-generated method stub
		return modelClasses;
	}

	private static Collection<ModelClassProperty> populateProperties(JsonLanguageInterpreter interpreter, String className, Metadata metadata, MetadataClasses clazz, JsonNode node) {
		List<ModelClassProperty> properties = new ArrayList<>();
		Iterator<String> iter = node.getFieldNames();
		while(iter.hasNext()) {
			String name = iter.next();
			JsonNode child = node.get(name);
			ModelClassProperty property = new ModelClassProperty();
			property.setName(name);
			configureNodeType(interpreter, className, name, child, property);
			properties.add(property);
		}
		
		for(MetadataClassesTransientProperty prop : clazz.getTransientProperty()) {
			ModelClassProperty property = new ModelClassProperty();
			property.setIsTransient(true);
			property.setName(prop.getName());
			property.setType(prop.getType());
		}
		
		return properties;
	}
	
	private static void configureNodeType(JsonLanguageInterpreter interpreter, String className, String name, JsonNode child, ModelClassProperty property) {		
		property.setType(getNodeType(interpreter, child, className, name));
		property.setIsArray(isArray(child));
		property.setIsPrimitive(isPrimitive(property));
		
		if(isArray(child)) {
			property.setArraySubType(getNodeType(interpreter, child.iterator().next(), className, name));
		}
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

	private static List<ModelClass> createAndPopulateModelClass(Metadata metadata, MetadataClasses clazz, String className, JsonNode node, String restUrl) {
		JsonLanguageInterpreter interpreter = getInterpreterFromMetadata(metadata);
		List<ModelClass> modelClasses = new ArrayList<>();
		ModelClass model = new ModelClass();
		model.setClassName(className);
		model.setHasSubClasses(hasSubClasses(node));
		model.setNamespace(metadata.getNamespace());
		model.setReadonly(clazz.getReadonly());
		model.setRestUrl(restUrl);
		model.getProperty().addAll(populateProperties(interpreter, className, metadata, clazz, node));
		model.setFileName(interpreter.buildFilenameFromClassname(className));
		model.setTestClassName(interpreter.buildClassName(className) + "Test");
		modelClasses.add(model);
		if(model.getHasSubClasses()) {
			modelClasses.addAll(getSubClasses(interpreter, metadata, clazz, node, model.getClassName()));
		}
		
		return modelClasses;
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
				System.out.println("ERROR: No Interpreter for type: " + lang.name());
				return null;
		}
	}
	
	public static LanguageOrchestrator getLanguageOrchestrator(Metadata metadata) {
		Language lang = Language.fromFullName(metadata.getLanguage());
		switch(lang) {
		case Java:
			return new JavaOrchestration();
		case ObjC:
			return new ObjectiveCOrchestration();
			
		default:
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
