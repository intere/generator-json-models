package com.intere.generator.metadata;

/**
 * MetadataTest.java
 * 
 * Generated by JSON Model Generator v0.0.4 on Tue Mar 24 07:30:07 MDT 2015.
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
import java.util.ArrayList;
import java.util.List;


public class MetadataTest {
	private static final ObjectMapper jsonMapper = new ObjectMapper();
	private Metadata instance;

	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException {
		InputStream in = new ByteArrayInputStream("{}".getBytes());
		instance = jsonMapper.readValue(in, Metadata.class);
	}

	protected String serialize(Metadata object)  throws JsonGenerationException, JsonMappingException, IOException {
		return jsonMapper.writeValueAsString(object);
	}

	protected Metadata deserialize(String json) throws JsonParseException, JsonMappingException, IOException {
		return jsonMapper.readValue(json, Metadata.class);
	}

	@Test
	public void testLanguage() throws Exception {
		final String expected = "test";
		instance.setLanguage(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The language property didn't deserialize properly", expected, instance.getLanguage());
	}

	@Test
	public void testNamespace() throws Exception {
		final String expected = "test";
		instance.setNamespace(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The namespace property didn't deserialize properly", expected, instance.getNamespace());
	}

	@Test
	public void testClasses() throws Exception {
		final MetadataClasses object = new MetadataClasses();
		final List<MetadataClasses> expected = new ArrayList<MetadataClasses>();
		expected.add(object);
		instance.setClasses(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The classes property didn't deserialize properly", expected, instance.getClasses());
	}

	@Test
	public void testGenerate() throws Exception {
		final MetadataGenerate expected = new MetadataGenerate();
		instance.setGenerate(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The generate property didn't deserialize properly", expected, instance.getGenerate());
	}

}	// end MetadataTest

