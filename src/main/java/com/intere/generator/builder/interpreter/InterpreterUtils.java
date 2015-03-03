package com.intere.generator.builder.interpreter;

import java.util.ArrayList;
import java.util.List;

public class InterpreterUtils {
	
	public static String humanReadableString(String propertyName) {
		propertyName = propertyName.replaceAll("^_", "");		// Remove leading underscore
		char[] stringArray = propertyName.trim().toCharArray();
        stringArray[0] = Character.toUpperCase(stringArray[0]);	// ensure the first character is Upper Case
        StringBuilder builder = new StringBuilder();
        boolean lastWasUpper = false;
        
        for(int i=0;i<stringArray.length;i++) {
        	if(i!=0) {
	        	if(stringArray[i] == '_' && stringArray.length>i) {
	        		builder.append(' ');
	        		builder.append(Character.toUpperCase(stringArray[i+1]));
	        		lastWasUpper = true;
	        		++i;
	        		continue;
	        	} else if(Character.isUpperCase(stringArray[i])) {
	        		if(!lastWasUpper) {
	        			builder.append(' ');
	        		}
	        		builder.append(stringArray[i]);
	        		lastWasUpper = true;
	        		continue;
	        	}
        	}
        	builder.append(stringArray[i]);
        	lastWasUpper = false;
        }
        String result = builder.toString().replaceAll("_", " ").replaceAll("  ", " ");
        
        return result;
	}
	
	public static String capsToUnderscores(String stringToConvert) {
		stringToConvert = stringToConvert.replaceAll("^_", "");		// Remove leading underscore
		char[] stringArray = stringToConvert.trim().toCharArray();
		List<Character> chars = new ArrayList<Character>();
		chars.add(Character.toLowerCase(stringArray[0]));
		boolean lastIsCapital = false;
		for(int i=1; i<stringArray.length; i++) {
			if(Character.isUpperCase(stringArray[i])) {
				if(!lastIsCapital) {
					chars.add('_');
				}
				chars.add(Character.toLowerCase(stringArray[i]));
				lastIsCapital = true;
			} else {
				lastIsCapital = false;
				chars.add(stringArray[i]);
			}
		}
		
		stringArray = new char[chars.size()];
		for(int i=0; i<chars.size(); i++) {
			stringArray[i] = chars.get(i).charValue();
		}
		
		return new String(stringArray).replaceAll("__", "_");
	}

	public static String javaStyleVariableName(String propertyName) {
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
	
	
}
