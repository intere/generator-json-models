package com.intere.generator.builder.orchestration.language.utility.java;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.models.JavaModelInterpreter;
import com.intere.generator.builder.orchestration.OrchestrationDataType;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility.CommentBuilder;
import com.intere.generator.builder.orchestration.language.utility.base.BaseTestBuilder;
import com.intere.generator.builder.orchestration.language.utility.comments.CStyleCommentBuilder;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

public class JavaTestBuilder extends BaseTestBuilder {
	private static final Logger LOGGER = LogManager.getLogger(JavaTestBuilder.class);
	protected CommentBuilder commentBuilder = new CStyleCommentBuilder();
	protected JsonLanguageInterpreter interpreter = new JavaModelInterpreter();
	
	@Override
	public CommentBuilder getCommentBuilder() {
		return commentBuilder;
	}

	@Override
	public JsonLanguageInterpreter getInterpreter() {
		return interpreter;
	}
	
	@Override
	public String buildNamespace(ModelClass modelClass) {
		if(null != modelClass.getNamespace()) {
			return "package " + modelClass.getNamespace() + ";\n\n";
		}
		return "";
	}
	
	@Override
	public String buildImplementationFileComment(ModelClass modelClass) {
		return buildFileComment(modelClass.getTestClassName() + ".java");
	}
	
	@Override
	public String buildSerializationConstants(ModelClass modelClass) {
		// Not Used for this language
		return null;
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
			if(!prop.getIsTransient()) {
				builder.append(addTestMethod(prop));
			}
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
	public String finishClass(ModelClass modelClass) {
		return "}\t" + singleLineComment("end " + modelClass.getTestClassName()) + "\n\n";
	}

	@Override
	public String buildHeaderFileComment(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildImports(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildClassImplementation(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildModelUtilityDeclarationMethods(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildModelUtilityDefinitionMethods(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildClassDeclaration(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildSinglePropertyDeclaration(ModelClassProperty property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildGetterAndSetter(ModelClassProperty prop) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPropertyType(ModelClassProperty property) {
		// TODO Auto-generated method stub
		return null;
	}	
}
