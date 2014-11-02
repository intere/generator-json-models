package com.intere.generator.deserializer;

import java.util.Iterator;

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
		} else if(node.isBoolean()) {
			return "@property (nonatomic) BOOL " + variableName + ";\n";
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
	 * Is the provided node an Array of Objects?
	 * @param node The node to check.
	 * @return True if the node is in fact an array of objects.
	 */
	public static boolean isArrayOfObjects(JsonNode node) {
		if(node.isArray()) {
			for (Iterator<JsonNode> iterator = node.getElements(); iterator.hasNext();) {
				JsonNode childNode = iterator.next();
				if(childNode.isObject()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isArrayofArrays(JsonNode node) {
		if(node.isArray()) {
			for (Iterator<JsonNode> iterator = node.getElements(); iterator.hasNext();) {
				JsonNode childNode = iterator.next();
				if(childNode.isArray()) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Builds the Serialization snippet per property.  
	 * Think: Serializing properties into the dictionary prior to serializing to JSON.
	 * @param node The node to be serialized.
	 * @param nodeName The name of the node that is going to be serialized.
	 * @return
	 */
	public static String buildGeneratedSerializePropertyString(JsonNode node, String className, String nodeName) {
		String variableName = cleanVariableName(nodeName);
		String defName = createSerializeConstantSymbolName(nodeName);
		String subClassName = buildSubClassName(className, nodeName);
		
		if(node.isTextual()) {
			return "[Serializer setDict:dict object:self." + variableName + " forKey:" + defName + "];\n";
		} else if(node.isInt()) {
			return "[Serializer setDict:dict intValue:self." + variableName + " forKey:" + defName + "];\n";	
		} else if(node.isFloatingPointNumber()) {
			return "[Serializer setDict:dict doubleValue:self." + variableName + " forKey:" + defName + "];\n";
		} else if(node.isBoolean()) {
			return "[Serializer setDict:dict boolValue:self." + variableName + " forKey:" + defName + "];\n";
		} else if(node.isArray()) {
			if(JsonNodeUtils.isArrayOfObjects(node)) {
				return "[Serializer setDict:dict object:[" + subClassName + " toArrayOfDictionaries:self."+ variableName + "] forKey:" + defName + "];\n"; 
			} else if(JsonNodeUtils.isArrayofArrays(node)) {
				// TODO: how do we handle an array of arrays?
			} else {
				return "[Serializer setDict:dict object:self." + variableName + " forKey:" + defName + "];\n";
			}
		} else if(node.isObject()) {
			return "[Serializer setDict:dict object:[self." + variableName + " toDictionary] forKey:" + defName + "];\n";
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
		} else if(node.isBoolean()) {
			return "object." + variableName + " = [Serializer getBoolFromDict:dict forKey:" + defName + " orDefaultTo:NO];\n";
		} else if(node.isArray()) {
			if(isArrayOfObjects(node)) {
				return "object." + variableName + " = [" + className + " fromArrayOfDictionaries:[Serializer getArrayFromDict:dict forKey:" + defName + "]];\n";
			} else if(isArrayofArrays(node)) {
				// TODO ??
			} else {
				return "object." + variableName + " = [[NSMutableArray alloc]initWithArray:[Serializer getArrayFromDict:dict forKey:" + defName + "]];\n";
			}
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
        
        for(int i=1;i<stringArray.length;i++) {
        	if(stringArray[i] == '_' && stringArray.length>i) {
        		stringArray[i+1] = Character.toUpperCase(stringArray[i+1]);
        	}
        }
        String result = new String(stringArray).replaceAll("_", "");
        
        return result;
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
		char[] stringArray = cleanVariableName(subClassName.trim()).toCharArray();
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
