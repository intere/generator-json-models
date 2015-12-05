package com.intere.generator.builder.interpreter.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.intere.generator.builder.interpreter.InterpreterUtils;
import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;

@Service("JavaInterpreter")
public class JavaModelInterpreter implements JsonLanguageInterpreter {
	private static final Logger LOG = LogManager.getLogger(JavaModelInterpreter.class);
	
	public String humanReadableName(String propertyName) {
		return InterpreterUtils.humanReadableString(propertyName);
	}
	
	/**
	 * Cleans a Variable Name (ensures there is no leading "_" and that the first character is lower case).
	 * @param propertyName The variable name to clean up to ensure it's a valid variable name.
	 * @return The cleaned up variable name.
	 */
	public String cleanVariableName(String propertyName) {
		return InterpreterUtils.javaStyleVariableName(propertyName);
	}
	
	public String buildClassName(String propertyName) {
		return buildSubClassName("", propertyName);
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
	 * @param parentClassName The parent class name.
	 * @param propertyName  The child class name.
	 * @return The <Parent Name><Child Name> class name.
	 */
	public String buildSubClassName(String parentClassName, String propertyName) {
		if(null != parentClassName && null != propertyName) {
			char[] stringArray = cleanVariableName(propertyName.trim()).toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			return parentClassName + new String(stringArray);
		} else {
			LOG.warn("Attempted to build a class name from a null parent or property name");
			return "";
		}
	}
	
	/**
	 * Not used.
	 */
	public String createSerializeConstantSymbolName(String propertyName) {
		return "";
	}

	public String buildFilenameFromClassname(String className) {
		return null != className ? className : "";
	}
	
	public String buildServiceFilenameFromClassname(String className) {
		return buildFilenameFromClassname(className) + "Service";
	}
	
	public String buildViewFilenameFromClassname(String className) {
		return buildFilenameFromClassname(className) + "View";
	}
	
	public String buildTestfilenameFromClassname(String className) {
		return buildFilenameFromClassname(className) + "Test";
	}

	public String buildGetterAndSetterName(String propertyName) {
		return buildSubClassName("", propertyName);
	}
}
