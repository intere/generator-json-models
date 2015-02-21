package com.intere.generator.builder.orchestration;

import org.codehaus.jackson.JsonNode;
import static com.intere.generator.deserializer.JsonNodeUtils.*;



public enum OrchestrationDataType {	
	IMAGE("Image", "String"),
	DATE("Date", "Date"),
	STRING("String", "String"),
	LONG("Long", "Long"),
	DOUBLE("Double", "Double"),
	BOOLEAN("Boolean", "Boolean"),
	ARRAY("Array", "List"),
	CLASS,
	UNKNOWN("Unknown", "String");
	
	private String internalName;
	private String javaName;
	
	/** Default Constructor.  */
	private OrchestrationDataType() {}
	
	/** Constructor that set the internal name. */
	private OrchestrationDataType(String internalName, String javaName) {
		this.internalName = internalName;
		this.javaName = javaName;
	}
	
	public String getInternalName() {
		return internalName;
	}
	
	public boolean hasInternalName() {
		return null != internalName;
	}
	
	/**
	 * Gets you an {@link OrchestrationDataType} by its internal name.
	 * @param internalName
	 * @return
	 */
	public static OrchestrationDataType fromInternalName(String internalName) {
		for(OrchestrationDataType type : values()) {
			if(type.hasInternalName() && type.getInternalName().equals(internalName)) {
				return type;
			}
		}
		return null;
	}

	public static OrchestrationDataType fromName(String typeName) {
		for(OrchestrationDataType type : values()) {
			if(type.name().equals(typeName)) {
				return type;
			}
		}
		return null;
	}

	public static OrchestrationDataType fromString(String typeName) {
		OrchestrationDataType type = OrchestrationDataType.fromInternalName(typeName);
		if(null == type) {
			type = OrchestrationDataType.fromName(typeName);
		}
		
		return type;
	}
	
	public static OrchestrationDataType fromJsonNode(JsonNode node) {
		if(isImage(node)) {
			return IMAGE;
		} else if(isDate(node)) {
			return DATE;
		} else if(isText(node)) {
			return STRING;
		} else if(isInteger(node) || isLong(node)) {
			return LONG;
		} else if(isFloat(node)) {
			return DOUBLE;
		} else if(isBoolean(node)) {
			return BOOLEAN;
		} else if(isObject(node)) {
			return CLASS;
		} else if(isArray(node)) {
			return ARRAY;
		} else {
			System.out.println("Unknown Node type: " + node.toString() + ", defaulting to String");
			return UNKNOWN;
		}
	}

	public String getJavaName() {
		return javaName;
	}
}
