package com.intere.generator.builder.orchestration;

import static com.intere.generator.deserializer.JsonNodeUtils.isArray;
import static com.intere.generator.deserializer.JsonNodeUtils.isBoolean;
import static com.intere.generator.deserializer.JsonNodeUtils.isDate;
import static com.intere.generator.deserializer.JsonNodeUtils.isFloat;
import static com.intere.generator.deserializer.JsonNodeUtils.isImage;
import static com.intere.generator.deserializer.JsonNodeUtils.isInteger;
import static com.intere.generator.deserializer.JsonNodeUtils.isLong;
import static com.intere.generator.deserializer.JsonNodeUtils.isObject;
import static com.intere.generator.deserializer.JsonNodeUtils.isText;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonNode;

import com.intere.generator.metadata.ModelClassProperty;


public enum OrchestrationDataType {	
	IMAGE("Image", "String", "NSString"),
	DATE("Date", "Date", "NSDate"),
	STRING("String", "String", "NSString"),
	LONG("Long", "Long", "NSInteger"),
	DOUBLE("Double", "Double", "CGFloat"),
	BOOLEAN("Boolean", "Boolean", "BOOL"),
	ARRAY("Array", "List", "NSArray"),
	CLASS,
	UNKNOWN("Unknown", "String", "NSString");
	
	private static final Logger LOGGER = LogManager.getLogger(OrchestrationDataType.class);
	private String internalName;
	private String javaName;
	private String objcName;
	
	/** Default Constructor.  */
	private OrchestrationDataType() {}
	
	/** Constructor that set the internal name. */
	private OrchestrationDataType(String internalName, String javaName, String objcName) {
		this.internalName = internalName;
		this.javaName = javaName;
		this.objcName = objcName;
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
		if(null == typeName) {
			return null;
		}
		
		OrchestrationDataType type = OrchestrationDataType.fromInternalName(typeName);
		if(null == type) {
			type = OrchestrationDataType.fromName(typeName);
		}
		if(null == type) {
			type = UNKNOWN;
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
			LOGGER.warn("Unknown Node type: " + node.toString() + ", defaulting to String");
			return UNKNOWN;
		}
	}
	
	@SuppressWarnings("unused")
	public static OrchestrationDataType fromModelProperty(ModelClassProperty prop) {
		if(prop.getIsArray()) {
			return ARRAY;
		}
		OrchestrationDataType dt = OrchestrationDataType.fromString(prop.getType());
		if(dt == null || UNKNOWN == dt) {
			return CLASS;
		}
		else if(null != dt) {
			return dt;
		}
		
		return UNKNOWN;
	}

	public String getJavaName() {
		return javaName;
	}

	public String getObjectiveCName() {
		return objcName;
	}
}
