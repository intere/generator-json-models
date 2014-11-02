package com.intere.generator.builder.interpreter;

import java.util.ArrayList;
import java.util.List;

public class RubyInterpreter implements JsonLanguageInterpreter {

	public String cleanVariableName(String propertyName) {
		propertyName = propertyName.replaceAll("^_", "");		// Remove leading underscore
		char[] stringArray = propertyName.trim().toCharArray();
		List<Character> chars = new ArrayList<Character>();
		chars.add(Character.toLowerCase(stringArray[0]));
		for(int i=1; i<stringArray.length; i++) {
			if(Character.isUpperCase(stringArray[i])) {
				chars.add('_');
				chars.add(Character.toLowerCase(stringArray[i]));
			} else {
				chars.add(stringArray[i]);
			}
		}
		
		stringArray = new char[chars.size()];
		for(int i=0; i<chars.size(); i++) {
			stringArray[i] = chars.get(i).charValue();
		}
		
		return new String(stringArray).replaceAll("__", "_");
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
		return cleanVariableName(className);
	}

}
