package com.intere.generator.builder.interpreter.models;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;

public class JavaModelInterpreter implements JsonLanguageInterpreter {
	/**
	 * Cleans a Variable Name (ensures there is no leading "_" and that the first character is lower case).
	 * @param propertyName The variable name to clean up to ensure it's a valid variable name.
	 * @return The cleaned up variable name.
	 */
	public String cleanVariableName(String propertyName) {
		propertyName = propertyName.replaceAll("^_", "");		// Remove leading underscore
		char[] stringArray = propertyName.trim().toCharArray();			
        stringArray[0] = Character.toLowerCase(stringArray[0]);	// ensure the first character is lower case
        
        for(int i=1;i<stringArray.length;i++) {
        	if(stringArray[i] == '_' && stringArray.length>i) {
        		stringArray[i+1] = Character.toUpperCase(stringArray[i+1]);
        	}
        }
        String result = new String(stringArray).replaceAll("_", "");
        
        return result;
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
		char[] stringArray = cleanVariableName(propertyName.trim()).toCharArray();
		stringArray[0] = Character.toUpperCase(stringArray[0]);
        return parentClassName + new String(stringArray);
	}
	
	/**
	 * Not used.
	 */
	public String createSerializeConstantSymbolName(String propertyName) {
		return "";
	}

	public String buildFilenameFromClassname(String className) {
		return className;
	}
	
	public String buildServiceFilenameFromClassname(String className) {
		return buildFilenameFromClassname(className) + "Service";
	}
	
	public String buildTestfilenameFromClassname(String className) {
		return buildFilenameFromClassname(className) + "Test";
	}

	public String buildGetterAndSetterName(String propertyName) {
		return buildSubClassName("", propertyName);
	}
}
