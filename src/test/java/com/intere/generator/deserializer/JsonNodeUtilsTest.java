package com.intere.generator.deserializer;

import static org.junit.Assert.*;

import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;

public class JsonNodeUtilsTest {
	private static final String PROP_ARRAY_OF_STRINGS = "{\"prop1\":[\"string1\",\"string2\"]}";
	private static final String PROP_STRING = "{\"prop1\": \"string1\"}";
	private static final String PROP_INT = "{\"prop1\":5}";
	private static final String PROP_BOOLEAN = "{\"prop1\":true}";
	private static final String PROP_DECIMAL = "{\"prop1\": 1.2345}";
	private static final String PROP_ARRAY_OF_OBJECTS = "{\"prop1\":[{\"sub1\":\"value1\",\"sub2\":\"value2\"}]}";
	private static final String PROP_ARRAY_OF_ARRAYS = "{\"prop1\":[[{\"sub1\":\"value1\"}],[{\"sub2\":\"value2\"}]]}";
	
	@Test
	public void testIsArrayOfObjects() throws Exception {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_OBJECTS).get("prop1");
		assertTrue("This is an array of objects", JsonNodeUtils.isArrayOfObjects(node));
	}

	@Test
	public void testIsNotArrayOfObjects() throws Exception {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_STRINGS).get("prop1");
		assertFalse("This is NOT an array of objects", JsonNodeUtils.isArrayOfObjects(node));
	}
	
	@Test
	public void testIsArrayOfArrays() throws Exception {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_ARRAYS).get("prop1");
		assertTrue("This is an array of arrays", JsonNodeUtils.isArrayofArrays(node));
	}
	
	@Test
	public void testIsNotArrayOfArrays() throws Exception {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_ARRAYS).get("prop1");
		assertFalse("This is NOT an array of arrays", JsonNodeUtils.isArrayOfObjects(node));
	}
	
	private JsonNode parseJsonObject(String json) throws JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getJsonFactory();
		JsonParser parser = factory.createJsonParser(json);
		return mapper.readTree(parser);
	}
}
