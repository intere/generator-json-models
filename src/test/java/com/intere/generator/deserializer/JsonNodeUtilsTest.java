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
	private static final String PROP_DECIMAL = "{\"prop1\": 1.2345}";
	private static final String PROP_ARRAY_OF_OBJECTS = "{\"prop1\":[{\"sub1\":\"value1\",\"sub2\":\"value2\"}]}";
	
	//
	// Tests of buildGenerateDeserializePropertyString
	//
	@Test
	public void testBuildGenerateDeserializePropertyStringFromArrayOfStrings() throws JsonParseException, IOException {
	  String name = "prop1";
	  JsonNode node = parseJsonObject(PROP_ARRAY_OF_STRINGS);
	  String result = JsonNodeUtils.buildGenerateDeserializePropertyString(node.get(name), "Foo",  name);
	  assertEquals("object.prop1 = [[NSMutableArray alloc]initWithArray:[Serializer getArrayFromDict:dict forKey:SERIALIZE_PROP1]];\n", result);
	}

	@Test
	@Ignore("This needs to be fixed immediately")
	public void testBuildGenerateDeserializePropertyStringFromArrayOfObjects() throws JsonParseException, IOException {
	  String name = "prop1";
	  JsonNode node = parseJsonObject(PROP_ARRAY_OF_OBJECTS);
	  String result = JsonNodeUtils.buildGenerateDeserializePropertyString(node.get(name), "Foo",  name);
	  assertEquals("", result);
	}

	@Test
	public void testBuildGenerateDeserializePropertyStringFromString() throws JsonParseException, IOException {
	  String name = "prop1";
	  JsonNode node = parseJsonObject(PROP_STRING);
	  String result = JsonNodeUtils.buildGenerateDeserializePropertyString(node.get(name), "Foo",  name);
	  assertEquals("object.prop1 = [Serializer safeGetDictString:dict withKey:SERIALIZE_PROP1];\n", result);
	}

	@Test
	public void testBuildGenerateDeserializePropertyStringFromInt() throws JsonParseException, IOException {
	  String name = "prop1";
	  JsonNode node = parseJsonObject(PROP_INT);
	  String result = JsonNodeUtils.buildGenerateDeserializePropertyString(node.get(name), "Foo",  name);
	  assertEquals("object.prop1 = [Serializer getIntegerFromDict:dict forKey:SERIALIZE_PROP1 orDefaultTo:0];\n", result);
	}

	@Test
	public void testBuildGenerateDeserializePropertyStringFromDecimal() throws JsonParseException, IOException {
	  String name = "prop1";
	  JsonNode node = parseJsonObject(PROP_DECIMAL);
	  String result = JsonNodeUtils.buildGenerateDeserializePropertyString(node.get(name), "Foo",  name);
	  assertEquals("object.prop1 = [Serializer getDoubleFromDict:dict forKey:SERIALIZE_PROP1 orDefaultTo:0.0];\n", result);
	}
	
	//
	// Tests of buildPoundDefineSerializerDeclarations method
	//
	@Test
	public void testBuildPoundDefineSerializerDeclarationsFromArrayOfStrings() throws JsonParseException, IOException {
	  String name = "prop1";
	  String result = JsonNodeUtils.buildPoundDefineSerializerDeclarations(name);
	  assertEquals("#define SERIALIZE_PROP1 @\"prop1\"\n", result);
	}

	@Test
	public void testBuildPoundDefineSerializerDeclarationsFromArrayOfObjects() throws JsonParseException, IOException {
	  String name = "prop1";
	  String result = JsonNodeUtils.buildPoundDefineSerializerDeclarations(name);
	  assertEquals("#define SERIALIZE_PROP1 @\"prop1\"\n", result);
	}

	@Test
	public void testBuildPoundDefineSerializerDeclarationsFromString() throws JsonParseException, IOException {
	  String name = "prop1";
	  String result = JsonNodeUtils.buildPoundDefineSerializerDeclarations(name);
	  assertEquals("#define SERIALIZE_PROP1 @\"prop1\"\n", result);
	}

	@Test
	public void testBuildPoundDefineSerializerDeclarationsFromInt() throws JsonParseException, IOException {
	  String name = "prop1";
	  String result = JsonNodeUtils.buildPoundDefineSerializerDeclarations(name);
	  assertEquals("#define SERIALIZE_PROP1 @\"prop1\"\n", result);
	}

	@Test
	public void testBuildPoundDefineSerializerDeclarationsFromDecimal() throws JsonParseException, IOException {
	  String name = "prop1";
	  String result = JsonNodeUtils.buildPoundDefineSerializerDeclarations(name);
	  assertEquals("#define SERIALIZE_PROP1 @\"prop1\"\n", result);
	}
	
	//
	// Tests of buildGeneratedSerializePropertyString method
	//
	@Test
	@Ignore("This needs to be fixed ASAP")
	public void testBuildGeneratedSerializePropertyStringFromArrayOfObjects() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_OBJECTS);
		String name = "prop1";
		String result = JsonNodeUtils.buildGeneratedSerializePropertyString(node.get(name), name);
		assertNotEquals("", result);
	}
	
	@Test
	@Ignore("This needs to be fixed ASAP")
	public void testBuildGeneratedSerializePropertyStringFromArrayOfStrings() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_STRINGS);
		String name = "prop1";
		String result = JsonNodeUtils.buildGeneratedSerializePropertyString(node.get(name), name);
		assertNotEquals("", result);
	}
	
	@Test
	public void testBuildGeneratedSerializePropertyStringFromDecimal() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_DECIMAL);
		String name = "prop1";
		String result = JsonNodeUtils.buildGeneratedSerializePropertyString(node.get(name), name);
		assertEquals("[Serializer setDict:dict doubleValue:self.prop1 forKey:SERIALIZE_PROP1];\n", result);
	}
	
	@Test
	public void testBuildGeneratedSerializePropertyStringFromInt() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_INT);
		String name = "prop1";
		String result = JsonNodeUtils.buildGeneratedSerializePropertyString(node.get(name), name);
		assertEquals("[Serializer setDict:dict intValue:self.prop1 forKey:SERIALIZE_PROP1];\n", result);
	}
	
	@Test
	public void testBuildGeneratedSerializePropertyStringFromString() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_STRING);
		String name = "prop1";
		String result = JsonNodeUtils.buildGeneratedSerializePropertyString(node.get(name), name);
		assertEquals("[Serializer setDict:dict object:self.prop1 forKey:SERIALIZE_PROP1];\n", result);
	}
	
	//
	// Tests of buildPropertyDeclaration
	//
	@Test
	public void testBuildPropertyDeclarationFromArrayOfObjects() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_OBJECTS);
		String name = "prop1";
		String result = JsonNodeUtils.buildPropertyDeclaration(node.get(name), "Foo", name);
		assertEquals("@property (nonatomic, strong) NSMutableArray *prop1;\n", result);
	}
	
	@Test
	public void testBuildPropertyDeclarationFromArrayOfStrings() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_STRINGS);
		String name = "prop1";
		String result = JsonNodeUtils.buildPropertyDeclaration(node.get(name), "Foo", name);
		assertEquals("@property (nonatomic, strong) NSMutableArray *prop1;\n", result);
	}

	@Test
	public void testBuildPropertyDeclarationFromString() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_STRING);
		String name = "prop1";
		String result = JsonNodeUtils.buildPropertyDeclaration(node.get(name), "Foo", name);
		assertEquals("@property (nonatomic, strong) NSString *prop1;\n", result);
	}
	
	@Test
	public void testBuildPropertyDeclarationFromInt() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_INT);
		String name = "prop1";
		String result = JsonNodeUtils.buildPropertyDeclaration(node.get(name), "Foo", name);
		assertEquals("@property (nonatomic) NSInteger prop1;\n", result);
	}
	
	@Test
	public void testBuildPropertyDeclarationFromDecimal() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_DECIMAL);
		String name = "prop1";
		String result = JsonNodeUtils.buildPropertyDeclaration(node.get(name), "Foo", name);
		assertEquals("@property (nonatomic) double prop1;\n", result);
	}
	
	private JsonNode parseJsonObject(String json) throws JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getJsonFactory();
		JsonParser parser = factory.createJsonParser(json);
		return mapper.readTree(parser);
	}
}
