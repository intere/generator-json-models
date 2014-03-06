package com.intere.generator.deserializer;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.junit.Test;

public class JsonDeserializerTest {
	
	private static final String JSON = "{\n" + 
			"    \"trackId\": \"53172f34c2e6f07785e2ebed\",\n" + 
			"    \"trackName\": \"2012-03-31_19-41-17\",\n" + 
			"    \"geojson\": {\n" + 
			"      \"type\": \"Polygon\",\n" + 
			"      \"coordinates\": [\n" + 
			"        [\n" + 
			"          39.905472,\n" + 
			"          -105.789886\n" + 
			"        ],\n" + 
			"        [\n" + 
			"          39.868843,\n" + 
			"          -105.765106\n" + 
			"        ]\n" + 
			"      ]\n" + 
			"    },\n" + 
			"    \"numPoints\": 3299\n" + 
			"  }";

	@Test
	public void test() throws Exception {
		String json = "{\"name\":\"value\"}";
		
		JsonDeserializer des = new JsonDeserializer("Stupify", json);
		
		assertNotNull("The JSON Node was null", des.getNode());
		assertNotNull("The child was null", des.getNode().getFieldNames());
		
		assertNotNull("The Name Node was null", des.getNode().get("name"));
		assertEquals("The Name Node was not what we expected", "value", des.getNode().get("name").asText());
		
	}
	
	
	@Test
	public void testCreateHeaderFile() throws Exception {
		
		JsonDeserializer des = new JsonDeserializer("UserTrackOverview", JSON);
		assertNotNull("The Node came back null", des.getNode());
		
		String header = des.generateHeaderFile();
		assertNotNull("The Header File was null", header);
		System.out.println(header);
		
	}
	
	@Test
	public void testCreateImplementationFile() throws Exception {
		
		JsonDeserializer des = new JsonDeserializer("UserTrackOverview", JSON);
		assertNotNull("The Node came back null", des.getNode());
		
		String implementation = des.generateImplementationFile();
		assertNotNull("The Implementation file was null", implementation);
		System.out.println(implementation);
		
	}
	

}
