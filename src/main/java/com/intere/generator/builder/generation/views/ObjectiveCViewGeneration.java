package com.intere.generator.builder.generation.views;

import static com.intere.generator.deserializer.JsonNodeUtils.isArray;
import static com.intere.generator.deserializer.JsonNodeUtils.isBoolean;
import static com.intere.generator.deserializer.JsonNodeUtils.isDate;
import static com.intere.generator.deserializer.JsonNodeUtils.isFloat;
import static com.intere.generator.deserializer.JsonNodeUtils.isInteger;
import static com.intere.generator.deserializer.JsonNodeUtils.isLong;
import static com.intere.generator.deserializer.JsonNodeUtils.isObject;
import static com.intere.generator.deserializer.JsonNodeUtils.isText;

import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;

import org.codehaus.jackson.JsonNode;

import com.intere.generator.builder.generation.utils.CodeGenerationUtils;
import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.models.ObjectiveCModelInterpreter;
import com.intere.generator.deserializer.JsonDeserializer;

public class ObjectiveCViewGeneration extends ViewCodeGeneration {
	
	private JsonLanguageInterpreter INTERPRETER = new ObjectiveCModelInterpreter();

	@Override
	public JsonLanguageInterpreter getInterpreter() {
		return INTERPRETER;
	}
	
	@Override
	public String generateHeaderFile(JsonDeserializer deserializer) {
		String modelClassName = deserializer.getName();
		String viewClassName = INTERPRETER.buildViewFilenameFromClassname(modelClassName);
		StringBuilder builder = new StringBuilder();
		builder.append(generateHeaderCommentBlock(viewClassName + ".h"));
		builder.append("#import <UIKit/UIKit.h>\n");
		builder.append("#import \"" + modelClassName + ".h\"\n");
		builder.append("\n");
		
		builder.append("@interface " + viewClassName + " : UIView\n\n");
		builder.append(comment("Sets the " + modelClassName + " object for this view") + "\n");
		builder.append("-(void)set" + modelClassName + ":(" + modelClassName + " *)a" + modelClassName + ";\n\n");

		builder.append(comment("Gets the " + modelClassName + " object that's set on this view") + "\n");
		builder.append("-(" + modelClassName + " *)get" + modelClassName + ";\n\n");	
		
		builder.append("@end\n\n");
		return builder.toString();
	}

	@Override
	public String generateImplementationFile(JsonDeserializer deserializer) {
		String modelClassName = deserializer.getName();
		String viewClassName = INTERPRETER.buildViewFilenameFromClassname(modelClassName);
		NavigableMap<String, List<JsonDeserializer>> subClasses = deserializer.getSubClasses();
		JsonNode node = deserializer.getNode();
		StringBuilder builder = new StringBuilder();
		builder.append(generateHeaderCommentBlock(viewClassName + ".m"));
		builder.append("#import \"" + viewClassName + ".h\"\n");
		builder.append("\n");
		
		// Interface Declaration
		builder.append("@interface " + viewClassName + "() {\n");
		// Now - go generate all of the properties:
		Iterator<String> iter = node.getFieldNames();
		while(iter.hasNext()) {
			String name = iter.next();
			builder.append("\t" + buildViewType(node.get(name), name) + " *" + getInterpreter().cleanVariableName(name) + ";\n");
		}
		builder.append("}\n");
		builder.append("@end\n");		
		
		return builder.toString();
	}

	private String buildViewType(JsonNode node, String name) {
		if(isText(node) || isDate(node) || isFloat(node) || isInteger(node) || isLong(node) || isBoolean(node)) {
			return "UILabel";
		} else if(isObject(node) || isArray(node)) {
			return "UIView";
		} else {
			System.out.println(name + " is some other type of node...");
			return "// Unknown Type: ";
		}
	}

	@Override
	public String generateTestFile(JsonDeserializer deserializer,
			String jsonFilename, String testJsonFilename) {
		// TODO Auto-generated method stub
		return null;
	}

	protected String generateHeaderCommentBlock(String filename) {
		return CodeGenerationUtils.generateCStyleHeaderBlock(filename);
	}
	
	protected String comment(String message) {
		return "/** " + message + " */";
	}
}
