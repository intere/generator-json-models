package com.intere.generator.deserializer;

import java.util.Iterator;

import org.codehaus.jackson.JsonNode;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;

/**
 * Some (testable) utility methods that help out with the generation of {@link JsonDeserializer}.
 * @author einternicola
 */
public class JsonNodeUtils {
	
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
}
