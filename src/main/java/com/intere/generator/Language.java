package com.intere.generator;

/**
 * Enumeration used to keep track of languages.
 */
public enum Language {
	ObjC("objc", "Objective-C"),
	Swift("swift", "Swift"),
	Ruby("ruby", "Ruby"),
	Java("java", "Java"),;
	
	private String abbreviation;
	private String fullName;

	/**
	 * Constructs the type with an abbreviation and full name.
	 * @param abbreviation The (internal) abbreviation for the language type.
	 * @param fullName The full language name.
	 */
	private Language(String abbreviation, String fullName) {
		this.abbreviation = abbreviation;
		this.fullName = fullName;
	}

	/**
	 * Get the language from the provided full name.
	 * @param fullName The full name to be converted to a Language object.
	 * @return The language associated with the full name.
	 */
	public static Language fromFullName(String fullName) {
		if(null != fullName) {
			for(Language lang : Language.values()) {
				if(lang.getFullName().toLowerCase().equals(fullName.toLowerCase())) {
					return lang;
				}
			}
			
			return null;
		}
		return null;
	}

	/**
	 * Get the language from the provided abbreviation.
	 * @param abbreviation The abbreviation to be converted to a language.
	 * @return The Language associated with the abbreviation.
	 */
	public static Language fromAbbreviation(String abbreviation) {
		if(null != abbreviation) {
			for(Language lang : Language.values()) {
				if(lang.getAbbreviation().equals(abbreviation.toLowerCase())) {
					return lang;
				}
			}
		}
		
		return null;
	}
	
	public String getAbbreviation() {
		return abbreviation;
	}
	
	public String getFullName() {
		return fullName;
	}
}
