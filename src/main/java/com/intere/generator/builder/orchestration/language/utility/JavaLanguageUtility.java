package com.intere.generator.builder.orchestration.language.utility;


import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.models.JavaModelInterpreter;
import com.intere.generator.builder.orchestration.OrchestrationDataType;
import com.intere.generator.builder.orchestration.OrchestrationTree;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

public class JavaLanguageUtility extends AbstractLanguageUtility {
	private static final Logger LOGGER = LogManager.getLogger(JavaLanguageUtility.class);
	JsonLanguageInterpreter interpreter = new JavaModelInterpreter();
	
	@Override
	public String buildNamespace(ModelClass modelClass) {
		if(null != modelClass.getNamespace()) {
			return "package " + modelClass.getNamespace() + ";\n\n";
		}
		return "";
	}

	@Override
	public String buildClassDeclaration(ModelClass modelClass) {
		return  "@SuppressWarnings(\"serial\")\n" + 
				"public class " + modelClass.getClassName() + " implements Serializable {\n";
	}

	@Override
	public String finishClass(ModelClass modelClass, boolean testClass) {
		return "}\t" + singleLineComment("end " + (testClass ? modelClass.getTestClassName() : modelClass.getClassName())) + "\n\n";
	}
	
	@Override
	public String buildSinglePropertyDeclaration(ModelClassProperty property) {
		StringBuilder builder = new StringBuilder();
		String propertyType = getPropertyType(property);			
		builder.append(tabs(1) + "private " + propertyType + " " + property.getName());
		if(OrchestrationDataType.ARRAY == OrchestrationDataType.fromModelProperty(property)) {
			if(null != property.getArraySubType()) {
				builder.append(" = new ArrayList<" + property.getArraySubType() + ">()" );
			} else {
				builder.append(" = new ArrayList()");
			}
		}
		builder.append(";\n");
		
		return builder.toString();
	}
	
	@Override
	public String getPropertyType(ModelClassProperty property) {
		OrchestrationDataType type = OrchestrationDataType.fromModelProperty(property);
		
		switch(type) {
		case ARRAY:
			if(null != property.getArraySubType()) {
				return "List<" + property.getArraySubType() + ">";
			} else {
				return "List";
			}
			
		case UNKNOWN:
			return "String";
			
		case CLASS:
			return property.getType();
			
		default:
			return type.getJavaName();
		}
	}

	@Override
	public String buildGettersAndSetters(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		for(ModelClassProperty prop : modelClass.getProperty()) {
			builder.append(buildGetterAndSetter(prop));
		}
		return builder.toString();
	}

	@Override
	public String buildGetterAndSetter(ModelClassProperty prop) {
		StringBuilder builder = new StringBuilder();
		String propMethodBase = interpreter.buildGetterAndSetterName(prop.getName());
		String type = getPropertyType(prop);
		
		builder.append(multiLineComment("Setter for " + prop.getName() + " property", 1) + "\n");
		builder.append(tabs(1) + "public void set" + propMethodBase + "(" + type + " " + prop.getName() + ") {\n");
		builder.append(tabs(2) + "this." + prop.getName() + " = " + prop.getName() + ";\n");
		builder.append(tabs(1) + "}\n\n");
		
		builder.append(multiLineComment("Getter for " + prop.getName() + " property", 1) + "\n");
		builder.append(tabs(1) + "public " + type + " get" + propMethodBase + "() {\n");
		builder.append(tabs(2) + "return this." + prop.getName() + ";\n");
		builder.append(tabs(1) + "}\n\n");

		return builder.toString();
	}
	
	@Override
	public String buildImports(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
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
		
		for(String key : imports.keySet()) {
			builder.append("import " + imports.get(key) + ";\n");
		}
		
		builder.append("\n");
		
		return builder.toString();
	}

	@Override
	public String buildClassImplementation(ModelClass modelClass) {
		// Not Used for this language
		return null;
	}

	@Override
	public String buildSerializationConstants(ModelClass modelClass) {
		// Not Used for this language
		return null;
	}

	@Override
	public String buildModelUtilityDeclarationMethods(ModelClass modelClass) {
		// Not currently used
		return null;
	}

	@Override
	public String buildModelUtilityDefinitionMethods(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		
		// hashCode method
		builder.append(tabs(1) + "@Override\n");
		builder.append(tabs(1) + "public int hashCode() {\n");
		builder.append(tabs(2) + "final int prime = 31;\n");
		builder.append(tabs(2) + "int result = 1;\n");
		for(ModelClassProperty prop : modelClass.getProperty()) {
			builder.append(tabs(2) + "result = prime * result + ((" 
					+ prop.getName() + " == null) ? 0 : " + prop.getName() + ".hashCode());\n");
		}
		builder.append(tabs(2) + "return result;\n");
		builder.append(tabs(1) + "}\n\n");
		
		// equals method
		builder.append(tabs(1) + "@Override\n");
		builder.append(tabs(1) + "public boolean equals(Object obj) {\n");
		builder.append(tabs(2) + "if (this == obj)\n");
		builder.append(tabs(3) + "return true;\n");
		builder.append(tabs(2) + "if (obj == null)\n");
		builder.append(tabs(3) + "return false;\n");
		builder.append(tabs(2) + "if (getClass() != obj.getClass())\n");
		builder.append(tabs(3) + "return false;\n");
		builder.append(tabs(2) + modelClass.getClassName() + " other = (" + modelClass.getClassName() + ")obj;\n");
		for(ModelClassProperty prop : modelClass.getProperty()) {
			builder.append(tabs(2) + "if(" + prop.getName() + " == null) {\n");
			builder.append(tabs(3) + "if(other." + prop.getName() + " != null)\n");
			builder.append(tabs(4) + "return false;\n");
			builder.append(tabs(2) + "} else if(!" + prop.getName() + ".equals(other." + prop.getName() + "))\n");
			builder.append(tabs(3) + "return false;\n");
		}
		
		builder.append(tabs(2) + "return true;\n");
		builder.append(tabs(1) + "}\n\n");		
		return builder.toString();
	}

	@Override
	public String buildTestClassDeclaration(ModelClass modelClass) {
		return  "public class " + modelClass.getTestClassName() + " {\n";
	}

	@Override
	public String buildTestImports(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("import static org.junit.Assert.*;\n\n");
		builder.append("import org.junit.Test;\n");
		builder.append("import org.junit.Before;\n");
		builder.append("import java.io.ByteArrayInputStream;\n");
		builder.append("import java.io.IOException;\n");
		builder.append("import java.io.InputStream;\n");
		builder.append("import org.codehaus.jackson.JsonGenerationException;\n");
		builder.append("import org.codehaus.jackson.JsonParseException;\n");
		builder.append("import org.codehaus.jackson.map.JsonMappingException;\n");
		builder.append("import org.codehaus.jackson.map.ObjectMapper;\n");
		builder.append(buildImports(modelClass));
		return builder.toString();
	}

	@Override
	public String buildTestSetupMethod(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(tabs(1) + "private static final ObjectMapper jsonMapper = new ObjectMapper();\n");
		builder.append(tabs(1) + "private " + modelClass.getClassName() + " instance;\n\n");
		
		builder.append(tabs(1) + "@Before\n");
		builder.append(tabs(1) + "public void setUp() throws JsonParseException, JsonMappingException, IOException {\n");
		builder.append(tabs(2) + "InputStream in = new ByteArrayInputStream(\"{}\".getBytes());\n");
		builder.append(tabs(2) + "instance = jsonMapper.readValue(in, " + modelClass.getClassName() + ".class);\n");
		builder.append(tabs(1) + "}\n\n");
		
		builder.append(tabs(1) + "protected String serialize(" + modelClass.getClassName() + " object)  throws JsonGenerationException, JsonMappingException, IOException {\n");
		builder.append(tabs(2) + "return jsonMapper.writeValueAsString(object);\n");
		builder.append(tabs(1) + "}\n\n");
		
		builder.append(tabs(1) + "protected " + modelClass.getClassName() + " deserialize(String json) throws JsonParseException, JsonMappingException, IOException {\n");
		builder.append(tabs(2) + "return jsonMapper.readValue(json, " + modelClass.getClassName() + ".class);");
		builder.append(tabs(1) + "}\n\n");
		
		return builder.toString();
	}

	@Override
	public String buildTestMethods(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		for(ModelClassProperty prop : modelClass.getProperty()) {
			builder.append(addTestMethod(prop));
		}
		
		return builder.toString();
	}

	private String addTestMethod(ModelClassProperty prop) {
		StringBuilder builder = new StringBuilder();
		builder.append(tabs(1) + "@Test\n");
		builder.append(tabs(1) + "public void test" + interpreter.buildGetterAndSetterName(prop.getName()) + "() throws Exception {\n");
		builder.append(buildAssertion(prop));
		builder.append(tabs(1) + "}\n\n");
		return builder.toString();
	}

	private String buildAssertion(ModelClassProperty prop) {
		StringBuilder builder = new StringBuilder();
		String getSetBase = interpreter.buildGetterAndSetterName(prop.getName());
		OrchestrationDataType dt = OrchestrationDataType.fromModelProperty(prop);
		switch(dt) {
		case ARRAY:
			builder.append(tabs(2) + "final " + prop.getArraySubType() + " object = new " + prop.getArraySubType() + "();\n");
			builder.append(tabs(2) + "final List<" + prop.getArraySubType() + "> expected = new ArrayList<" + prop.getArraySubType() + ">();\n");
			builder.append(tabs(2) + "expected.add(object);\n");
			builder.append(tabs(2) + "instance.set" + getSetBase + "(expected);\n");
			builder.append(tabs(2) + "String serialized = serialize(instance);\n" );
			builder.append(tabs(2) + "instance = deserialize(serialized);\n");
			builder.append(tabs(2) + "assertEquals(\"The " + prop.getName() 
					+ " property didn't deserialize properly\", expected, instance.get" 
					+ getSetBase + "());\n");
			break;
		case STRING:
		case IMAGE:
			builder.append(tabs(2) + "final String expected = \"test\";\n");
			builder.append(tabs(2) + "instance.set" + getSetBase + "(expected);\n");
			builder.append(tabs(2) + "String serialized = serialize(instance);\n" );
			builder.append(tabs(2) + "instance = deserialize(serialized);\n");
			builder.append(tabs(2) + "assertEquals(\"The " + prop.getName() 
					+ " property didn't deserialize properly\", expected, instance.get" 
					+ getSetBase + "());\n");
			break;
			
		case BOOLEAN:
			builder.append(tabs(2) + "final Boolean expected = true;\n");
			builder.append(tabs(2) + "instance.set" + getSetBase + "(expected);\n");
			builder.append(tabs(2) + "String serialized = serialize(instance);\n" );
			builder.append(tabs(2) + "instance = deserialize(serialized);\n");
			builder.append(tabs(2) + "assertEquals(\"The " + prop.getName() 
					+ " property didn't deserialize properly\", expected, instance.get" 
					+ getSetBase + "());\n");
			break;
			
		case DOUBLE:
			builder.append(tabs(2) + "final Double expected = 123.456;\n");
			builder.append(tabs(2) + "instance.set" + getSetBase + "(expected);\n");
			builder.append(tabs(2) + "String serialized = serialize(instance);\n" );
			builder.append(tabs(2) + "instance = deserialize(serialized);\n");
			builder.append(tabs(2) + "assertEquals(\"The " + prop.getName() 
					+ " property didn't deserialize properly\", expected, instance.get" 
					+ getSetBase + "());\n");
			break;
			
		case LONG:
			builder.append(tabs(2) + "final Long expected = 123456789L;\n");
			builder.append(tabs(2) + "instance.set" + getSetBase + "(expected);\n");
			builder.append(tabs(2) + "String serialized = serialize(instance);\n" );
			builder.append(tabs(2) + "instance = deserialize(serialized);\n");
			builder.append(tabs(2) + "assertEquals(\"The " + prop.getName() 
					+ " property didn't deserialize properly\", expected, instance.get" 
					+ getSetBase + "());\n");
			break;
			
		case DATE:
			builder.append(tabs(2) + "final Date expected = new Date();\n");
			builder.append(tabs(2) + "instance.set" + getSetBase + "(expected);\n");
			builder.append(tabs(2) + "String serialized = serialize(instance);\n" );
			builder.append(tabs(2) + "instance = deserialize(serialized);\n");
			builder.append(tabs(2) + "assertEquals(\"The " + prop.getName() 
					+ " property didn't deserialize properly\", expected, instance.get" 
					+ getSetBase + "());\n");
			break;
			
		case CLASS:
			builder.append(tabs(2) + "final " + prop.getType() + " expected = new " + prop.getType() + "();\n");
			builder.append(tabs(2) + "instance.set" + getSetBase + "(expected);\n");
			builder.append(tabs(2) + "String serialized = serialize(instance);\n" );
			builder.append(tabs(2) + "instance = deserialize(serialized);\n");
			builder.append(tabs(2) + "assertEquals(\"The " + prop.getName() 
					+ " property didn't deserialize properly\", expected, instance.get" 
					+ getSetBase + "());\n");
			break;
		default:
			LOGGER.warn("Unknown type: " + prop.getName());
			break;
		}
		
		return builder.toString();
	}

	@Override
	public void enforceFilenames(OrchestrationTree tree) {
		// No-Op for Java
	}
}