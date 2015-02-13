package com.intere.generator.builder.interpreter.models;

import com.intere.generator.builder.interpreter.InterpreterUtils;
import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;


public class RubyModelInterpreter implements JsonLanguageInterpreter {

	public String cleanVariableName(String propertyName) {
		return InterpreterUtils.capsToUnderscores(propertyName);
	}
	
	public String buildClassName(String propertyName) {
		return buildSubClassName("", propertyName);
	}

	public String buildSubClassName(String parentClassName, String propertyName) {
		propertyName = propertyName.replaceAll("^_", "");		// Remove leading underscore
		char[] stringArray = propertyName.trim().toCharArray();			
        stringArray[0] = Character.toLowerCase(stringArray[0]);	// ensure the first character is lower case
        
        for(int i=1;i<stringArray.length;i++) {
        	if(stringArray[i] == '_' && stringArray.length>i) {
        		stringArray[i+1] = Character.toUpperCase(stringArray[i+1]);
        	}
        }
        stringArray = new String(stringArray).replaceAll("_", "").toCharArray();
        stringArray[0] = Character.toUpperCase(stringArray[0]);
        return parentClassName + new String(stringArray);
	}

	public String createSerializeConstantSymbolName(String propertyName) {
		// TODO Auto-generated method stub
		return null;
	}

	public String buildFilenameFromClassname(String className) {
		return InterpreterUtils.capsToUnderscores(className);
	}
	
	public String buildServiceFilenameFromClassname(String className) {
		return buildFilenameFromClassname(className) + "_service";
	}
	
	public String buildViewFilenameFromClassname(String className) {
		return buildFilenameFromClassname(className) + "_view";
	}
	
	public String buildTestfilenameFromClassname(String className) {
		return buildFilenameFromClassname(className) + "_spec";
	}

	public String buildGetterAndSetterName(String propertyName) {
		return null;
	}

}
