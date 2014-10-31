package com.intere.generator.deserializer;

import org.codehaus.jackson.JsonNode;

/**
 * Some (testable) utility methods that help out with the generation of {@link JsonDeserializer}.
 * @author einternicola
 */
public class JsonNodeUtils {
	
	/**
	 * Generates the <code>@property</code> definitions.
	 * @param node
	 * @param className
	 * @param name
	 * @return
	 */
	public static String buildPropertyDeclaration(JsonNode node, String className, String name) {
		String variableName = cleanVariableName(name);
		String subClassName = buildSubClassName(className, name);
		
		if(node.isTextual()) {
			return "@property (nonatomic, strong) NSString *" + variableName + ";\n";
		} else if(node.isInt()) {
			return "@property (nonatomic) NSInteger " + variableName + ";\n";
		} else if(node.isFloatingPointNumber()) {
			return "@property (nonatomic) double " + variableName + ";\n";
		} else if(node.isObject()) {
			return "@property (nonatomic, strong) " + subClassName + " *" + variableName + ";\n";
		} else if(node.isArray()) {
			return "@property (nonatomic, strong) NSMutableArray *" + variableName + ";\n";
		} else {
			System.out.println(name + " is some other type of node...");
		}
		return "";
	}
	
	public static String buildPoundDefineSerializerDeclarations(String name) {
		String defName = JsonNodeUtils.createSerializeConstantSymbolName(name);
		return "#define " + defName + " @\"" + name + "\"\n";
	}
	
	/**
	 * Builds the Serialization snippet per property.
	 * @param node
	 * @param name
	 * @return
	 */
	public static String buildGeneratedSerializePropertyString(JsonNode node, String name) {
		String variableName = cleanVariableName(name);
		String  defName = createSerializeConstantSymbolName(name);
		
		if(node.isTextual()) {
			return "[Serializer setDict:dict object:self." + variableName + " forKey:" + defName + "];\n";
		} else if(node.isInt()) {
			return "[Serializer setDict:dict intValue:self." + variableName + " forKey:" + defName + "];\n";	
		} else if(node.isFloatingPointNumber()) {
			return "[Serializer setDict:dict doubleValue:self." + variableName + " forKey:" + defName + "];\n";
		} else if(node.isArray()) {
			// TODO: how do we handle an array?
		} else if(node.isObject()) {
			// TOOD: how do we handle a sub-object?
		}
		return "";
	}
	
	/**
	 * Builds the Deserialization snippet per property.
	 * @param node
	 * @param parentClassName
	 * @param name
	 * @return
	 */
	public static String buildGenerateDeserializePropertyString(JsonNode node, String parentClassName, String name) {
		String defName = JsonNodeUtils.createSerializeConstantSymbolName(name);
		String variableName = cleanVariableName(name);
		String className = buildSubClassName(parentClassName, name);
		
		if(node.isTextual()) {
			return "object." + variableName + " = [Serializer safeGetDictString:dict withKey:" + defName + "];\n";
		} else if(node.isInt()) {
			return "object." + variableName + " = [Serializer getIntegerFromDict:dict forKey:" + defName + " orDefaultTo:0];\n";
		} else if(node.isFloatingPointNumber()) {
			return "object." + variableName + " = [Serializer getDoubleFromDict:dict forKey:" + defName + " orDefaultTo:0.0];\n";	
		} else if(node.isArray()) {
			return "object." + variableName + " = [[NSMutableArray alloc]initWithArray:[Serializer getArrayFromDict:dict forKey:" + defName + "]];\n";
			// TODO: how do we handle an array?
		} else if(node.isObject()) {
			return "object." + variableName + " = [" + className + " fromDictionary:[dict objectForKey:" + defName + "]];\n";
		}
		return "";
	}
	
	/**
	 * Cleans a Variable Name (ensures there is no leading "_" and that the first character is lower case).
	 * @param name The variable name to clean up to ensure it's a valid variable name.
	 * @return The cleaned up variable name.
	 */
	public static String cleanVariableName(String name) {
		name = name.replaceAll("^_", "");						// Remove leading underscore
		char[] stringArray = name.trim().toCharArray();			
        stringArray[0] = Character.toLowerCase(stringArray[0]);	// ensure the first character is lower case
        return new String(stringArray);
	}
	
	/**
	 * Provides an "Inner Class Name" for an Objective-C Class.  Objective-C does not support inner classes, so this is how we
	 * handle it, by creating a Subclass "facade".  If we wanted to create the Java equivalent of:
	 * <code>
	 * class Parent {
	 * 		class Child {
	 *			// ...
	 *		}
	 *	// ...
	 * }
	 * </code>
	 * You would call this with name="Parent" and subClassName as "Child" and you would end up with "ParentChild".
	 * @param name The parent class name.
	 * @param subClassName  The child class name.
	 * @return The <Parent Name><Child Name> class name.
	 */
	public static String buildSubClassName(String name, String subClassName) {
		char[] stringArray = subClassName.trim().toCharArray();
        stringArray[0] = Character.toUpperCase(stringArray[0]);
        return name + new String(stringArray);
	}
	
	public static String createSerializeConstantSymbolName(String name) {
		StringBuilder serName = new StringBuilder("SERIALIZE_");
		boolean lastLower = true;
		for(int i=0; i<name.length(); i++) {
			char c = name.charAt(i);
			if(c>='A' && c<='Z') {
				if(lastLower) {
					serName.append('_');
				}
				serName.append(c);
				lastLower = false;
			} else {
				lastLower = true;
				serName.append(("" + c).toUpperCase());
			}
		}
		
		return serName.toString();
	}
}
