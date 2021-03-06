package com.intere.generator.metadata;

/**
 * MetadataClassesPropertyMapTest.java
 * 
 * Generated by JSON Model Generator v0.0.4 on Thu Mar 26 08:14:39 MDT 2015.
 * https://github.com/intere/generator-json-models
 * 
 * The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
*/

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


public class MetadataClassesPropertyMapTest {
	private static final ObjectMapper jsonMapper = new ObjectMapper();
	private MetadataClassesPropertyMap instance;

	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException {
		InputStream in = new ByteArrayInputStream("{}".getBytes());
		instance = jsonMapper.readValue(in, MetadataClassesPropertyMap.class);
	}

	protected String serialize(MetadataClassesPropertyMap object)  throws JsonGenerationException, JsonMappingException, IOException {
		return jsonMapper.writeValueAsString(object);
	}

	protected MetadataClassesPropertyMap deserialize(String json) throws JsonParseException, JsonMappingException, IOException {
		return jsonMapper.readValue(json, MetadataClassesPropertyMap.class);
	}

	@Test
	public void testProperty() throws Exception {
		final String expected = "test";
		instance.setProperty(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The property property didn't deserialize properly", expected, instance.getProperty());
	}

	@Test
	public void testMapClassName() throws Exception {
		final String expected = "test";
		instance.setMapClassName(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The mapClassName property didn't deserialize properly", expected, instance.getMapClassName());
	}

	@Test
	public void testMapClassProperty() throws Exception {
		final String expected = "test";
		instance.setMapClassProperty(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The mapClassProperty property didn't deserialize properly", expected, instance.getMapClassProperty());
	}

}	// end MetadataClassesPropertyMapTest

