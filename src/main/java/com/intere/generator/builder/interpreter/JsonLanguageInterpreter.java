package com.intere.generator.builder.interpreter;


public interface JsonLanguageInterpreter {
	
	/**
	 * Given the string, provides you a "clean" variable name (appropriate for the language).
	 * @param propertyName The property name that we would like to generate a property for.
	 * @return
	 */
	String cleanVariableName(String propertyName);
	
	/**
	 * Given the propertyName value, this will build you a className.
	 * @param propertyName
	 * @return A nicely formatted class name.
	 */
	String buildClassName(String propertyName);
	
	/**
	 * Given the provided names for parent class and property, this method will provide you with an appropriate
	 * "child class" name for the given language.
	 * @param parentClassName
	 * @param propertyName
	 * @return
	 */
	String buildSubClassName(String parentClassName, String propertyName);
	
	/**
	 * This method will generate you a nice constant symbol name for the given property.
	 * @param propertyName
	 * @return
	 */
	String createSerializeConstantSymbolName(String propertyName);
	
	/**
	 * What should the filename (no extensions) be (using the provided class name).
	 * @param className The Class Name.
	 * @return The base filename (no extension) based on the provided class name.
	 */
	String buildFilenameFromClassname(String className);
	
	/**
	 * What should the service classname (no extension) be (using the provided class name).
	 * @param className The Class Name.
	 * @return The base service filename (no extension) based on the provided class name.
	 */
	String buildServiceFilenameFromClassname(String className);
	
	/**
	 * What should the test filename be (no extension) using the provided class name
	 * @param className the Class Name.
	 * @return The base test filename (no extension) based on the provided class name.
	 */
	String buildTestfilenameFromClassname(String className);
	
	/**
	 * This is very Java-Centric, builds the "getter/setter" name.
	 * @param propertyName
	 * @return
	 */
	String buildGetterAndSetterName(String propertyName);	
}
