package com.intere.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class JacksonTest {
	private static final ObjectMapper jsonMapper = new ObjectMapper();
	private static final JsonFactory factory = jsonMapper.getJsonFactory();
	private static final String JSON = "{\"address1\":\"78336 US Hwy 40\",\"address2\":null,\"category\":\"Pub & Grub\",\"city\":\"Winter Park\",\"contactEmail\":\"chrismoore11@msn.com\",\"contactName\":\"Chris Moore\",\"contactTitle\":\"Owner\",\"id\":\"541ae52baec039165e7e52b8\",\"logo\":\"http://www.fontenotswp.com/images/albums/NewAlbum_6ec89/tn_480_FontenotsLogo.GIF.jpg\",\"name\":\"Fontenot's\",\"phoneNumber\":\"(970) 726-4021\",\"siteUrl\":\"http://fontenotswp.com/\",\"state\":\"CO\",\"zip\":\"80482\"}";
	private JsonNode jsonNode;
	
	@Before
	public void setUp() throws JsonParseException, IOException {
		jsonNode = JacksonTest.parseJsonObject(JSON);
	}
	
	@Test
	public void testRawJson() {
		assertNotNull(jsonNode);
		assertEquals(JSON, jsonNode.toString());
	}

	@Test
	public void testPrettyPrint() throws JsonGenerationException, JsonMappingException, IOException {
		String prettyPrinted = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
		assertNotNull(prettyPrinted);
		assertNotEquals(JSON, prettyPrinted);
		System.out.println(prettyPrinted);
	}

	public static JsonNode parseJsonObject(String json) throws JsonParseException, IOException {
		JsonParser parser = factory.createJsonParser(json);
		return jsonMapper.readTree(parser);
	}
}
