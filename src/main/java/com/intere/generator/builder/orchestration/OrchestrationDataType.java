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

/**
 * This enumeration serves as an abstract way to deal with various data types we might encounter.
 *
 */
public enum OrchestrationDataType {
	/** It's really just a string, but starts with one of the following: "image:", "image16x9:", or "image4x3:" */
	IMAGE("Image", "String", "NSString", "String", "UIImageView"),
	
	/** Probably a string, could be a number, but determined to be a Date. */
	DATE("Date", "Date", "NSDate", "NSDate", "UIDatePicker"),
	
	/** A String that's not greater than 25 characters.  */
	STRING("String", "String", "NSString", "String", "UITextField"),
	
	/** A String that's longer than 25 characters.  */
	TEXT("Text", "String", "NSString", "String", "UITextView"),
	
	/** An Integer. */
	LONG("Long", "Long", "NSInteger", "Int", "UITextField"),
	
	/** A Decimal number */
	DOUBLE("Double", "Double", "double", "Double", "UITextField"),
	
	/** True / False */
	BOOLEAN("Boolean", "Boolean", "BOOL", "Bool", "UISwitch"),
	
	/** A collection of other objects, strings, numbers, etc. */
	ARRAY("Array", "List", "NSArray", "Array", "UIButton"),
	
	/** An abstract data type, a "child class" */
	CLASS(null, null, null, null, "UIButton"),
	
	/** Type could not be determined, hopefully we don't have too many of these. */
	UNKNOWN("Unknown", "String", "NSString", "String", null);
	
	private static final Logger LOGGER = LogManager.getLogger(OrchestrationDataType.class);
	private String internalName;
	private String javaName;
	private String objcName;
	private String swiftName;
	private String objcViewName;
	
	/** Default Constructor.  */
	private OrchestrationDataType() {}
	
	/** Constructor that set the internal name. */
	private OrchestrationDataType(String internalName, String javaName, String objcName, String swiftName, String objcViewName) {
		this.internalName = internalName;
		this.javaName = javaName;
		this.objcName = objcName;
		this.swiftName = swiftName;
		this.objcViewName = objcViewName;
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
			if(node.getTextValue().length()>25) {
				return TEXT;
			}
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

	/**
	 * Gets the type in the "Java Dialect".
	 * @return
	 */
	public String getJavaName() {
		return javaName;
	}

	/**
	 * Gets the type in the "Obj-C Dialect".
	 * @return
	 */
	public String getObjectiveCName() {
		return objcName;
	}

	/**
	 * Gets the type in the "Swift Dialect".
	 * @return The type name in Swift.
	 */
	public String getSwiftName() {
		return swiftName;
	}

	/**
	 * Gets the type in the "Obj-C View Dialect".
	 * @return
	 */
	public String getObjectiveCViewClass() {
		return objcViewName;
	}
}
