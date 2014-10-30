package com.intere.generator.deserializer;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.junit.Test;

public class JsonDeserializerTest {
	private static final String CLASS_NAME = "RegisterDto";
	private static final String JSON = "{\"userId\": \"abcd\",\"contestId\": \"efgh\"}";
	private static final String JSON_2 = "{\"_id\":10263,\"_index\":\"weedmaps__20141014185633\",\"_score\":null,\"_type\":\"dispensary\",\"fields\":{\"address\":[\"3600 S. Wadsworth Blvd\"],\"city\":[\"Lakewood\"],\"feature_level\":[\"gold\"],\"feature_level_raw\":[4],\"feature_order\":[1],\"has_testing\":[false],\"is_delivery\":[false],\"latitude\":[\"39.6495244\"],\"license_type\":[\"medical\"],\"longitude\":[\"-105.0807009\"],\"name\":[\"The Clinic on Wadsworth\"],\"package_level\":[\"listing_plus\"],\"package_level_raw\":[2],\"phone_number\":[\"(303) 484-8853\"],\"published\":[true],\"rating\":[\"4.49846153846154\"],\"reviews_count\":[130],\"slug\":[\"the-clinic-on-wadsworth\"],\"state\":[\"CO\"],\"zip_code\":[\"80235\"]},\"sort\":[1]}";
	private static final String JSON_3 = "{\"id\": \"abcd\",\"sub\":{\"contestId\": \"efgh\"}}";
	
	@Test
	public void testBuildName() throws Exception {
		JsonDeserializer des = new JsonDeserializer("Tacky", JSON);
		assertEquals("Build Name didn't live up to its name", "TackyToo", des.buildSubClassName("too"));
	}
	
	@Test
	public void testCleanVariableName() throws Exception {
		JsonDeserializer des = new JsonDeserializer("Tacky", JSON);
		assertEquals("Clean Variable Name isn't living up to its name", "id", des.cleanVariableName("_id"));
		assertEquals("Clean Variable Name isn't living up to its name", "gottaJiboo", des.cleanVariableName("GottaJiboo"));
	}

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
	public void testNestedObjects() throws Exception {
		JsonDeserializer des = new JsonDeserializer("Top", JSON_2);
		System.out.println(des.generateHeaderFile());
		for(String key : des.getSubClasses().keySet()) {
			for(JsonDeserializer cDes : des.getSubClasses().get(key)) {
				System.out.println(cDes.generateHeaderFile());
			}
		}
		System.out.println(des.generateImplementationFile());
		for(String key : des.getSubClasses().keySet()) {
			for(JsonDeserializer cDes : des.getSubClasses().get(key)) {
				System.out.println(cDes.generateImplementationFile());
			}
		}
	}

	@Test
	public void testCreateHeaderFile() throws Exception {

		JsonDeserializer des = new JsonDeserializer(CLASS_NAME, JSON);
		assertNotNull("The Node came back null", des.getNode());

		String header = des.generateHeaderFile();
		assertNotNull("The Header File was null", header);
		System.out.println(header);

	}

	@Test
	public void testCreateImplementationFile() throws Exception {
		JsonDeserializer des = new JsonDeserializer(CLASS_NAME, JSON);
		assertNotNull("The Node came back null", des.getNode());

		String implementation = des.generateImplementationFile();
		assertNotNull("The Implementation file was null", implementation);
		System.out.println(implementation);
	}

	@Test
	public void testCreateHeaderFileNestedObject() throws Exception {
		JsonDeserializer des = new JsonDeserializer(CLASS_NAME, JSON_2);
		assertNotNull("The Node came back null", des.getNode());
		
		String header = des.generateHeaderFile();
		assertNotNull("The headear was null", header);
		System.out.println(header);
	}
	
	@Test
	public void testNestedObject() throws Exception {
		JsonDeserializer des = new JsonDeserializer(CLASS_NAME, JSON_2);
		assertNotNull("The Node came back null", des.getNode());
		
		String impl = des.generateImplementationFile();
		assertNotNull("The implementation was null", impl);
		System.out.println(impl);
	}

}
