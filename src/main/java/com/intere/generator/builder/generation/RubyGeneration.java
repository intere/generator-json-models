package com.intere.generator.builder.generation;

import java.util.Iterator;

import org.codehaus.jackson.JsonNode;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.RubyInterpreter;
import com.intere.generator.deserializer.JsonDeserializer;
import com.intere.generator.deserializer.JsonNodeUtils;

public class RubyGeneration extends CodeGeneration {
	private static final RubyInterpreter INTERPRETER = new RubyInterpreter();

	@Override
	public String generateHeaderFile(JsonDeserializer deserializer) {
		return null;	// Ruby doesn't have Header Files
	}

	@Override
	public String generateImplementationFile(JsonDeserializer deserializer) {
		String className = deserializer.getName();
		String filename = getInterpreter().buildFilenameFromClassname(className);
		JsonNode node = deserializer.getNode();
		
		StringBuilder builder = new StringBuilder();
		builder.append("#\n" + 
				"#  " + filename + ".rb\n" +
				"#  Generated by JSON Model Generator on " + getDate() + ".\n" +
				"#    https://github.com/intere/generator-json-models\n" +
				"#\n" +
				"#    The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content\n" +
				"#\n" +
				"#\n\n");
		
		// TODO: require lines?
		
		builder.append("class " + className + " < JsonModel\n");
		
		Iterator<String> iter = node.getFieldNames();
		while(iter.hasNext()) {
			String name = iter.next();
			builder.append(buildPropertyDeclaration(node.get(name), className, name));
		}
		
		// TODO Serialize / Deserialize declarations:
		
		builder.append("end\n");
		
		return builder.toString();
	}

	public String buildPropertyDeclaration(JsonNode node, String className, String name) {
		String propName = getInterpreter().cleanVariableName(name);
		String subClass = getInterpreter().buildSubClassName(className, name);
		
		if(node.isArray()) {
			if(JsonNodeUtils.isArrayOfObjects(node)) {
				// How do we handle this?
			} else if(JsonNodeUtils.isArrayofArrays(node)) {
				
			} else {
				return "\n\tdef " + propName + "\n" +
						"\t\t@json.try(:[], '" + name + "')\n" +
						"\tend\n";
			}
		} else if(node.isObject()) {
			return "\n\tdef " + propName + "\n" +
					"\t\t@" + propName + " ||= " + subClass + ".new @json.try(:[], '" + name + "')\n" +
					"\tend\n";
		} else {
			return "\n\tdef " + propName + "\n" +
					"\t\t@json.try(:[], '" + name + "')\n" +
					"\tend\n";
		}
		
		return "";
	}

	@Override
	public JsonLanguageInterpreter getInterpreter() {
		return INTERPRETER;
	}

}
