package com.intere.generator.test;

public interface TestStrings {
	final String PROP_NAME = "prop1";
	
	final String PROP_ARRAY_OF_STRINGS = "{\"" + PROP_NAME + "\":[\"string1\",\"string2\"]}";
	final String PROP_STRING = "{\"" + PROP_NAME + "\": \"string1\"}";
	final String PROP_INT = "{\"" + PROP_NAME + "\":5}";
	final String PROP_BOOLEAN = "{\"" + PROP_NAME + "\":true}";
	final String PROP_DECIMAL = "{\"" + PROP_NAME + "\": 1.2345}";
	final String PROP_OBJECT = "{\"" + PROP_NAME + "\": {\"name\": \"nested object\"}}";
	final String PROP_ARRAY_OF_OBJECTS = "{\"" + PROP_NAME + "\":[{\"sub1\":\"value1\",\"sub2\":\"value2\"}]}";
	final String PROP_ARRAY_OF_ARRAYS = "{\"" + PROP_NAME + "\":[[{\"sub1\":\"value1\"}],[{\"sub2\":\"value2\"}]]}";
	final String PROP_DATE_LONG = "{\"" + PROP_NAME + "\": 1415577600000}";
	final String PROP_DATE_DATE = "{\"" + PROP_NAME + "\": new Date(1415577600000)}";	// Invalid
	final String PROP_DATE_ISO = "{\"" + PROP_NAME + "\":\"2014-01-01T23:28:56.782-0700\"}";
	final String PROP_DATE_ZULU = "{\"" + PROP_NAME + "\":\"2014-01-01T23:28:56.782Z\"}";
	final String PROP_DATE_ISO_2 = "{\"" + PROP_NAME + "\":\"2000-01-01T01:00:00.782-0700\"}";
}
