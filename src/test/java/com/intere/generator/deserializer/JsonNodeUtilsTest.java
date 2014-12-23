package com.intere.generator.deserializer;

import static com.intere.generator.test.TestUtils.parseJsonObject;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.junit.Test;

import com.intere.generator.test.TestStrings;

public class JsonNodeUtilsTest implements TestStrings {	
	
	@Test
	public void testIsArrayOfObjects() throws Exception {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_OBJECTS).get(PROP_NAME);
		assertTrue("This is an array of objects", JsonNodeUtils.isArrayOfObjects(node));
	}

	@Test
	public void testIsNotArrayOfObjects() throws Exception {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_STRINGS).get(PROP_NAME);
		assertFalse("This is NOT an array of objects", JsonNodeUtils.isArrayOfObjects(node));
	}
	
	@Test
	public void testIsArrayOfArrays() throws Exception {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_ARRAYS).get(PROP_NAME);
		assertTrue("This is an array of arrays", JsonNodeUtils.isArrayofArrays(node));
	}
	
	@Test
	public void testIsNotArrayOfArrays() throws Exception {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_ARRAYS).get(PROP_NAME);
		assertFalse("This is NOT an array of arrays", JsonNodeUtils.isArrayOfObjects(node));
	}
	
	@Test
	public void testIsText() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_STRING).get(PROP_NAME);
		assertTrue("String wasn't identified as JSON text", JsonNodeUtils.isText(node));
	}

	@Test
	public void testIsDateFromIso() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_DATE_ISO).get(PROP_NAME);
		assertTrue("ISO Date wasn't identified as JSON date", JsonNodeUtils.isDate(node));
	}
	
	@Test
	public void testIsDateFromDateLong() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_DATE_LONG).get(PROP_NAME);
		assertTrue("Long Date wasn't identified as JSON date", JsonNodeUtils.isDate(node));
	}
	
	@Test
	public void testIsDateFromIsoDate2() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_DATE_ISO_2).get(PROP_NAME);
		assertTrue("ISO Date wasn't identified as JSON date", JsonNodeUtils.isDate(node));
	}
	
	@Test
	public void testIsDateFromZulu() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_DATE_ISO).get(PROP_NAME);
		assertTrue("Zulu Date wasn't identified as JSON date", JsonNodeUtils.isDate(node));
	}
	
	@Test
	public void testIsNotDate() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_STRING).get(PROP_NAME);
		assertFalse("String was incorrectly fingerprinted as a date", JsonNodeUtils.isDate(node));
	}
	
	@Test
	public void testIsInteger() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_INT).get(PROP_NAME);
		assertTrue("Int value wasn't identified as JSON Int", JsonNodeUtils.isInteger(node));
	}
	
	@Test
	public void testIsLong() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_DATE_LONG).get(PROP_NAME);
		assertTrue("Long value wasn't identified as JSON long", JsonNodeUtils.isLong(node));
	}
	
	@Test
	public void testIsFloat() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_DECIMAL).get(PROP_NAME);
		assertTrue("Decimal value wasn't identified as JSON float", JsonNodeUtils.isFloat(node));
	}
	
	@Test
	public void testIsBoolean() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_BOOLEAN).get(PROP_NAME);
		assertTrue("Boolean value wasn't identified as JSON boolean", JsonNodeUtils.isBoolean(node));
	}
	
	@Test
	public void testIsObject() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_OBJECT).get(PROP_NAME);
		assertTrue("Object value wasn't identified as JSON Object", JsonNodeUtils.isObject(node));
	}
	
	
	
}
