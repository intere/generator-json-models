package com.intere.generator.metadata;

/**
 * ModelClassPropertyTest.java
 * 
 * Generated by JSON Model Generator v0.0.3 on Fri Feb 27 07:54:08 MST 2015.
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
import java.io.Serializable;

public class ModelClassPropertyTest {
	private static final ObjectMapper jsonMapper = new ObjectMapper();
	private ModelClassProperty instance;

	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException {
		InputStream in = new ByteArrayInputStream("{}".getBytes());
		instance = jsonMapper.readValue(in, ModelClassProperty.class);
	}

	protected String serialize(ModelClassProperty object)  throws JsonGenerationException, JsonMappingException, IOException {
		return jsonMapper.writeValueAsString(object);
	}

	protected ModelClassProperty deserialize(String json) throws JsonParseException, JsonMappingException, IOException {
		return jsonMapper.readValue(json, ModelClassProperty.class);	}

	@Test
	public void testName() throws Exception {
		final String expected = "test";
		instance.setName(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The name property didn't deserialize properly", expected, instance.getName());
	}

	@Test
	public void testIsKey() throws Exception {
		final Boolean expected = true;
		instance.setIsKey(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The isKey property didn't deserialize properly", expected, instance.getIsKey());
	}

	@Test
	public void testType() throws Exception {
		final String expected = "test";
		instance.setType(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The type property didn't deserialize properly", expected, instance.getType());
	}

	@Test
	public void testIsPrimitive() throws Exception {
		final Boolean expected = true;
		instance.setIsPrimitive(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The isPrimitive property didn't deserialize properly", expected, instance.getIsPrimitive());
	}

	@Test
	public void testIsTransient() throws Exception {
		final Boolean expected = true;
		instance.setIsTransient(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The isTransient property didn't deserialize properly", expected, instance.getIsTransient());
	}

	@Test
	public void testIsVisible() throws Exception {
		final Boolean expected = true;
		instance.setIsVisible(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The isVisible property didn't deserialize properly", expected, instance.getIsVisible());
	}

	@Test
	public void testIsReadonly() throws Exception {
		final Boolean expected = true;
		instance.setIsReadonly(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The isReadonly property didn't deserialize properly", expected, instance.getIsReadonly());
	}

	@Test
	public void testIsArray() throws Exception {
		final Boolean expected = true;
		instance.setIsArray(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The isArray property didn't deserialize properly", expected, instance.getIsArray());
	}

	@Test
	public void testArraySubType() throws Exception {
		final String expected = "test";
		instance.setArraySubType(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The arraySubType property didn't deserialize properly", expected, instance.getArraySubType());
	}

	@Test
	public void testArraySubTypeProperty() throws Exception {
		final ModelClassProperty expected = new ModelClassProperty();
		instance.setArraySubTypeProperty(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The arraySubTypeProperty property didn't deserialize properly", expected, instance.getArraySubTypeProperty());
	}

	@Test
	public void testParentModel() throws Exception {
		final ModelClass expected = new ModelClass();
		instance.setParentModel(expected);
		String serialized = serialize(instance);
		instance = deserialize(serialized);
		assertEquals("The parentModel property didn't deserialize properly", expected, instance.getParentModel());
	}

}	// end ModelClassPropertyTest

