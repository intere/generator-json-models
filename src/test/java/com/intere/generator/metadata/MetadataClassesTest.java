package com.intere.generator.metadata;

/**
 * MetadataClassesTest.java
 * 
 * Generated by JSON Model Generator v0.0.4 on Thu Apr 09 07:15:11 MDT 2015.
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


public class MetadataClassesTest {
	private static final ObjectMapper jsonMapper = new ObjectMapper();
	private MetadataClasses instance;

	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException {
		InputStream in = new ByteArrayInputStream("{}".getBytes());
		instance = jsonMapper.readValue(in, MetadataClasses.class);
	}

	protected String serialize(MetadataClasses object)  throws JsonGenerationException, JsonMappingException, IOException {
		return jsonMapper.writeValueAsString(object);
	}

	protected MetadataClasses deserialize(String json) throws JsonParseException, JsonMappingException, IOException {
		return jsonMapper.readValue(json, MetadataClasses.class);
	}

	@Test
	public void testClassName() throws Exception {
		final String expected = "test";
		instance.setClassName(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The className property didn't deserialize properly", expected, instance.getClassName());
	}

	@Test
	public void testJsonFile() throws Exception {
		final String expected = "test";
		instance.setJsonFile(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The jsonFile property didn't deserialize properly", expected, instance.getJsonFile());
	}

	@Test
	public void testKey() throws Exception {
		final String expected = "test";
		instance.setKey(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The key property didn't deserialize properly", expected, instance.getKey());
	}

	@Test
	public void testUrlPath() throws Exception {
		final String expected = "test";
		instance.setUrlPath(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The urlPath property didn't deserialize properly", expected, instance.getUrlPath());
	}

	@Test
	public void testReadonly() throws Exception {
		final Boolean expected = true;
		instance.setReadonly(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The readonly property didn't deserialize properly", expected, instance.getReadonly());
	}

	@Test
	public void testPropertyMap() throws Exception {
		final MetadataClassesPropertyMap object = new MetadataClassesPropertyMap();
		final List<MetadataClassesPropertyMap> expected = new ArrayList<MetadataClassesPropertyMap>();
		expected.add(object);
		instance.setPropertyMap(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The propertyMap property didn't deserialize properly", expected, instance.getPropertyMap());
	}

	@Test
	public void testTransientProperty() throws Exception {
		final MetadataClassesTransientProperty object = new MetadataClassesTransientProperty();
		final List<MetadataClassesTransientProperty> expected = new ArrayList<MetadataClassesTransientProperty>();
		expected.add(object);
		instance.setTransientProperty(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The transientProperty property didn't deserialize properly", expected, instance.getTransientProperty());
	}

	@Test
	public void testImports() throws Exception {
		final MetadataClassesImports object = new MetadataClassesImports();
		final List<MetadataClassesImports> expected = new ArrayList<MetadataClassesImports>();
		expected.add(object);
		instance.setImports(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The imports property didn't deserialize properly", expected, instance.getImports());
	}

	@Test
	public void testListSummary() throws Exception {
		final MetadataClassesListSummary object = new MetadataClassesListSummary();
		final List<MetadataClassesListSummary> expected = new ArrayList<MetadataClassesListSummary>();
		expected.add(object);
		instance.setListSummary(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The listSummary property didn't deserialize properly", expected, instance.getListSummary());
	}

}	// end MetadataClassesTest

