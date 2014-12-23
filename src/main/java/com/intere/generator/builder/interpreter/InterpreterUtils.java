package com.intere.generator.builder.interpreter;

import java.util.ArrayList;
import java.util.List;

public class InterpreterUtils {
	public static String capsToUnderscores(String stringToConvert) {
		stringToConvert = stringToConvert.replaceAll("^_", "");		// Remove leading underscore
		char[] stringArray = stringToConvert.trim().toCharArray();
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
	
	
}
