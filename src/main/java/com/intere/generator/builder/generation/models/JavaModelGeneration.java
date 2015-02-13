package com.intere.generator.builder.generation.models;

import static com.intere.generator.deserializer.JsonNodeUtils.getDateFromNode;
import static com.intere.generator.deserializer.JsonNodeUtils.getExtensionFromFilename;
import static com.intere.generator.deserializer.JsonNodeUtils.isArray;
import static com.intere.generator.deserializer.JsonNodeUtils.isArrayOfObjects;
import static com.intere.generator.deserializer.JsonNodeUtils.isArrayofArrays;
import static com.intere.generator.deserializer.JsonNodeUtils.isBoolean;
import static com.intere.generator.deserializer.JsonNodeUtils.isDate;
import static com.intere.generator.deserializer.JsonNodeUtils.isFloat;
import static com.intere.generator.deserializer.JsonNodeUtils.isInteger;
import static com.intere.generator.deserializer.JsonNodeUtils.isLong;
import static com.intere.generator.deserializer.JsonNodeUtils.isObject;
import static com.intere.generator.deserializer.JsonNodeUtils.isText;

import java.util.Date;
import java.util.Iterator;

import org.codehaus.jackson.JsonNode;

import com.intere.generator.builder.generation.CodeGeneration;
import com.intere.generator.builder.generation.utils.CodeGenerationUtils;
import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.models.JavaModelInterpreter;
import com.intere.generator.deserializer.JsonDeserializer;

public class JavaModelGeneration extends CodeGeneration {

	@Override
	public String generateHeaderFile(JsonDeserializer deserializer) {
		return null;	// Java doesn't have header files
	}

	private String buildGettersAndSetters(JsonNode node, String className, String name) {
		String propName = getInterpreter().cleanVariableName(name);
		String propType = getNodeType(node, className, name);
		String getSetName = getInterpreter().buildGetterAndSetterName(name);

		if(propType.length()>0) {
			return "\t/**\n" +
				"\t * Setter for " + propName + " property.\n" +
				"\t */\n" +
				"\tpublic void set" + getSetName + "(" + propType + " " + propName + ") {\n" +
				"\t\tthis." + propName + " = " + propName + ";\n" +
				"\t}\n\n" +
				"\t/**\n" +
				"\t * Getter for " + propName + " property.\n" +
				"\t */\n" +
				"\tpublic " + propType + " get" + getSetName + "() {\n" +
				"\t\treturn " + propName + ";\n" +
				"\t}\n\n";
		}
		return "";
	}

	public String buildPropertyDeclaration(JsonNode node, String className, String name) {
		String propName = getInterpreter().cleanVariableName(name);
		String nodeType = getNodeType(node, className, name);
		if(nodeType.length()>0) {
			return "private " + nodeType + " " + propName + ";\n";
		}
		
		return "";
	}

	public String getNodeType(JsonNode node, String className, String name) {
		String subClass = getInterpreter().buildSubClassName(className, name);
		if(isText(node)) {
			return "String";
		} else if(isDate(node)) {
			return "Date";
		} else if(isInteger(node) || isLong(node)) {
			return "Long";
		} else if(isFloat(node)) {
			return "Double";
		} else if(isBoolean(node)) {
			return "Boolean";
		} else if(isObject(node)) {
			return subClass;
		} else if(isArrayOfObjects(node)) {
			return "List<" + getNodeType(node.iterator().next(), className, name) + ">";
		} else if(isArrayofArrays(node)) {
			return "List<List>";
		} else if(isArray(node)) {
			if(node.size()>0) {
				return "List<" + getNodeType(node.get(0), className, name) + ">";
			}
			return "List";
		} else {
			System.out.println("Unknown Node type: " + node.toString() + ", defaulting to String");
			return "String";
		}
	}

	@Override
	public String generateImplementationFile(JsonDeserializer deserializer) {
		String className = deserializer.getName();
		String filename = getInterpreter().buildFilenameFromClassname(className);
		String packageName = deserializer.getNamespace();
		JsonNode node = deserializer.getNode();

		StringBuilder builder = new StringBuilder();
		builder.append(generateHeaderCommentBlock(filename));
		builder.append("package " + packageName + ";\n\n");
		builder.append("import java.util.List;\n" +
				"import java.util.Date;\n" +
				"import java.io.Serializable;\n");

		builder.append("\n\n@SuppressWarnings(\"serial\")\n" + 
				"public class " + className + " implements Serializable {\n");

		Iterator<String> iter = node.getFieldNames();
		while(iter.hasNext()) {
			String name = iter.next();
			String declaration = buildPropertyDeclaration(node.get(name), className, name);
			if(declaration.length()>0) {
				builder.append("\t" + declaration);
			}
		}

		builder.append("\n");

		iter = node.getFieldNames();
		while(iter.hasNext()) {
			String name = iter.next();
			builder.append(buildGettersAndSetters(node.get(name), className, name));
		}

		builder.append("}\n");

		return builder.toString();
	}
	
	@Override
	public String generateTestFile(JsonDeserializer deserializer, String jsonFilename, String jsonTestFilename) {
		String className = deserializer.getName();
		String packageName = deserializer.getNamespace();
		String testClassName = deserializer.getTestFilename();
		String jsonExtension = getExtensionFromFilename(jsonFilename);
		String filename = getInterpreter().buildFilenameFromClassname(testClassName);
		String classInstanceName = getInterpreter().cleanVariableName(className);
		
		StringBuilder builder = new StringBuilder();
		builder.append(generateHeaderCommentBlock(filename));
		builder.append("package " + packageName + ";\n\n" +
				"import static org.junit.Assert.*;\n\n" +
				"import java.io.IOException;\n" + 
				"import java.io.InputStream;\n\n" +
				"import org.codehaus.jackson.JsonParseException;\n" + 
				"import org.codehaus.jackson.map.JsonMappingException;\n" + 
				"import org.codehaus.jackson.map.ObjectMapper;\n" + 
				"import org.junit.Before;\n" + 
				"import org.junit.Test;\n\n" + 
				"public class " + testClassName + " {\n\n");
		builder.append("\tprivate static final ObjectMapper jsonMapper = new ObjectMapper();\n");
		builder.append("\tprivate " + className + " " + classInstanceName + ";\n\n");
		builder.append("\t@Before\n" + 
				"\tpublic void setUp() throws JsonParseException, JsonMappingException, IOException {\n" + 
				"\t\tInputStream in = getClass().getResourceAsStream(\"/" + packageName.replaceAll("\\.", "/") + "/" + jsonTestFilename + ".json\");\n" + 
				"\t\t" + classInstanceName + " = jsonMapper.readValue(in, " + className + ".class);\n" + 
				"\t}\n\n");
		
		Iterator<String> iter = deserializer.getNode().getFieldNames();
		while(iter.hasNext()) {
			String name = iter.next();
			builder.append(buildTestMethod(deserializer.getNode().get(name), className, name, classInstanceName));
		}
		
		builder.append("\n}\n");
		
		return builder.toString();
	}

	protected Object buildTestMethod(JsonNode jsonNode, String className, String name, String classInstanceName) {
		StringBuilder builder = new StringBuilder();
		String getSetName = getInterpreter().buildGetterAndSetterName(name);
		
		builder.append("\t@Test\n" +
				"\tpublic void test" + getSetName + "() {\n");
		
		appendAssertion(jsonNode, className, name, classInstanceName, getSetName, builder);
		
		builder.append("\t}\n\n");
		return builder.toString();
	}
	protected void appendAssertion(JsonNode jsonNode, String className, String name, String classInstanceName, String getSetName, StringBuilder builder) {
		appendAssertion(jsonNode, className, name, classInstanceName, getSetName, builder, null);
	}
	
	protected void appendAssertion(JsonNode jsonNode, String className, String name, String classInstanceName, String getSetName, StringBuilder builder, Integer index) {
		String message = "The " + name + " property didn't deserialize properly";
		String getProperty = classInstanceName + ".get" + getSetName + "()";
		if(null != index) {
			message = "The " + name + " property at index " + index + " didn't deserialize properly";
			getProperty += ".get(" + index + ")";
		}		
		
		if(isText(jsonNode)) {
			builder.append(buildAssertEquals(message, "\"" + jsonNode.getTextValue() + "\"", getProperty));
		} else if(isDate(jsonNode)) {
			Date date = getDateFromNode(jsonNode);
			String expected = "null";
			if(null != date) {
				expected = date.getTime() + "L";
			}
			builder.append(buildAssertEquals(message,  expected, getProperty + ".getTime()"));
		} else if(isArrayofArrays(jsonNode)) {
			builder.append("\t\t// TODO: Handle Array of Arrays\n");
		} else if(isArray(jsonNode)) {
			for(int i=0; i<jsonNode.size(); i++) {
				appendAssertion(jsonNode.get(i), className, name, classInstanceName, getSetName, builder, i);
			}
		} else if(isBoolean(jsonNode)) {
			if(jsonNode.asBoolean()) {
				builder.append(buildAssertTrue(message, getProperty));
			} else {
				builder.append(buildAssertFalse(message, getProperty));
			}
		} else if(isFloat(jsonNode)) {
			builder.append(buildAssertEquals(message, Double.toString(jsonNode.getDoubleValue()), getProperty));
		} else if(isObject(jsonNode)) {
			builder.append("\t\t// TODO: See " + className + "\n");
		} else if(isInteger(jsonNode)) {
			builder.append(buildAssertEquals(message, Long.toString(jsonNode.getLongValue()) + "L", getProperty + ".longValue()"));
		} else {
			builder.append("\t\t// TODO (don't know how to handle " + jsonNode.getTextValue() + ")\n");
		}
	}
	
	protected String buildAssertTrue(String message, String actual) {
		return "\t\tassertTrue(\"" + message + "\", " + actual + ");\n";
	}
	
	protected String buildAssertFalse(String message, String actual) {
		return "\t\tassertFalse(\"" + message + "\", " + actual + ");\n";
	}
	
	protected String buildAssertEquals(String message, String expected, String actual) {
		return "\t\tassertEquals(\"" + message + "\", " + expected + ", " + actual + ");\n";
	}
	
	protected String generateHeaderCommentBlock(String filename) {
		return CodeGenerationUtils.generateCStyleHeaderBlock(filename);
	}

	@Override
	public JsonLanguageInterpreter getInterpreter() {
		return new JavaModelInterpreter();
	}
}
