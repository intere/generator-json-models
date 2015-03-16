package com.intere.generator.metadata.utils;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import com.intere.generator.metadata.Metadata;

public class MetadataUtilsTest {
	private static final String CLASS_CONTEST = "Contest";
	private static final String CLASS_ENUM = "Enumeration";
	
	private static final ObjectMapper jsonMapper = new ObjectMapper();
	private Metadata metadata;
	private MetadataUtils utils;

	@Before
	public void setUp() throws JsonParseException, JsonMappingException, IOException {
		InputStream in = getClass().getResourceAsStream("/com/intere/generator/metadata/metadata.json");
		metadata = jsonMapper.readValue(in, Metadata.class);
		utils = new MetadataUtils(metadata);
	}
	
	@Test
	public void testConstructor() throws Exception {
		assertNotNull(utils);
		assertEquals(metadata, utils.getMetadata());
		assertNotNull(utils.getClassByName(CLASS_CONTEST));
	}
	
	@Test
	public void testIsReadonly() throws Exception {
		assertFalse(utils.isReadonly(CLASS_CONTEST));
		assertTrue(utils.isReadonly(CLASS_ENUM));
		assertFalse(utils.isReadonly(null));
		assertFalse(utils.isReadonly("INVALID CLASS"));
	}

}
