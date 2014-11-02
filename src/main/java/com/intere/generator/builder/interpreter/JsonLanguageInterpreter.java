package com.intere.generator.builder.interpreter;


public interface JsonLanguageInterpreter {
	
	/**
	 * Given the string, provides you a "clean" variable name (appropriate for the language).
	 * @param propertyName The property name that we would like to generate a property for.
	 * @return
	 */
	String cleanVariableName(String propertyName);
	
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
}
