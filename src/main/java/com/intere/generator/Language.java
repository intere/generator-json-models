package com.intere.generator;

public enum Language {
	ObjC("objc", "Objective-C"),
	Ruby("ruby", "Ruby"),
	Java("java", "Java");
	
	private String abbreviation;
	private String fullName;
	
	private Language(String abbreviation, String fullName) {
		this.abbreviation = abbreviation;
		this.fullName = fullName;
	}
	
	public static Language fromAbbreviation(String abbreviation) {
		for(Language lang : Language.values()) {
			if(lang.getAbbreviation().equals(abbreviation)) {
				return lang;
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
