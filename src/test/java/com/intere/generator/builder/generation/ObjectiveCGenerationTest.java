package com.intere.generator.builder.generation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ObjectiveCGenerationTest {
	
	private static final String PROP_ARRAY_OF_STRINGS = "{\"prop1\":[\"string1\",\"string2\"]}";
	private static final String PROP_STRING = "{\"prop1\": \"string1\"}";
	private static final String PROP_INT = "{\"prop1\":5}";
	private static final String PROP_BOOLEAN = "{\"prop1\":true}";
	private static final String PROP_DECIMAL = "{\"prop1\": 1.2345}";
	private static final String PROP_ARRAY_OF_OBJECTS = "{\"prop1\":[{\"sub1\":\"value1\",\"sub2\":\"value2\"}]}";
	private static final String PROP_ARRAY_OF_ARRAYS = "{\"prop1\":[[{\"sub1\":\"value1\"}],[{\"sub2\":\"value2\"}]]}";
	private ObjectiveCGeneration generator;
	
	@Before
	public void setUp() {
		generator = new ObjectiveCGeneration();
	}
	
	//
	// Tests of buildGenerateDeserializePropertyString
	//
	@Test
	public void testBuildGenerateDeserializePropertyStringFromArrayOfStrings() throws JsonParseException, IOException {
	  String name = "prop1";
	  JsonNode node = parseJsonObject(PROP_ARRAY_OF_STRINGS);
	  String result = generator.buildGenerateDeserializePropertyString(node.get(name), "Foo",  name);
	  assertEquals("object.prop1 = [[NSMutableArray alloc]initWithArray:[Serializer getArrayFromDict:dict forKey:SERIALIZE_PROP1]];\n", result);
	}

	@Test
	public void testBuildGenerateDeserializePropertyStringFromArrayOfObjects() throws JsonParseException, IOException {
	  String name = "prop1";
	  JsonNode node = parseJsonObject(PROP_ARRAY_OF_OBJECTS);
	  String result = generator.buildGenerateDeserializePropertyString(node.get(name), "Foo",  name);
	  assertEquals("object.prop1 = [FooProp1 fromArrayOfDictionaries:[Serializer getArrayFromDict:dict forKey:SERIALIZE_PROP1]];\n", result);
	}
	
	@Test
	public void testBuildGenerateDeserializePropertyStringFromArrayOfArrays() throws JsonParseException, IOException {
	  String name = "prop1";
	  JsonNode node = parseJsonObject(PROP_ARRAY_OF_ARRAYS);
	  String result = generator.buildGenerateDeserializePropertyString(node.get(name), "Foo",  name);
	  assertEquals("", result);
	}

	@Test
	public void testBuildGenerateDeserializePropertyStringFromString() throws JsonParseException, IOException {
	  String name = "prop1";
	  JsonNode node = parseJsonObject(PROP_STRING);
	  String result = generator.buildGenerateDeserializePropertyString(node.get(name), "Foo",  name);
	  assertEquals("object.prop1 = [Serializer safeGetDictString:dict withKey:SERIALIZE_PROP1];\n", result);
	}

	@Test
	public void testBuildGenerateDeserializePropertyStringFromInt() throws JsonParseException, IOException {
	  String name = "prop1";
	  JsonNode node = parseJsonObject(PROP_INT);
	  String result = generator.buildGenerateDeserializePropertyString(node.get(name), "Foo",  name);
	  assertEquals("object.prop1 = [Serializer getIntegerFromDict:dict forKey:SERIALIZE_PROP1 orDefaultTo:0];\n", result);
	}
	
	@Test
	public void testBuildGenerateDeserializePropertyStringFromBool() throws JsonParseException, IOException {
	  String name = "prop1";
	  JsonNode node = parseJsonObject(PROP_BOOLEAN);
	  String result = generator.buildGenerateDeserializePropertyString(node.get(name), "Foo",  name);
	  assertEquals("object.prop1 = [Serializer getBoolFromDict:dict forKey:SERIALIZE_PROP1 orDefaultTo:NO];\n", result);
	}

	@Test
	public void testBuildGenerateDeserializePropertyStringFromDecimal() throws JsonParseException, IOException {
	  String name = "prop1";
	  JsonNode node = parseJsonObject(PROP_DECIMAL);
	  String result = generator.buildGenerateDeserializePropertyString(node.get(name), "Foo",  name);
	  assertEquals("object.prop1 = [Serializer getDoubleFromDict:dict forKey:SERIALIZE_PROP1 orDefaultTo:0.0];\n", result);
	}
	
	//
	// Tests of buildPoundDefineSerializerDeclarations method
	//
	@Test
	public void testBuildPoundDefineSerializerDeclarationsFromArrayOfStrings() throws JsonParseException, IOException {
	  String name = "prop1";
	  String result = generator.buildPoundDefineSerializerDeclarations(name);
	  assertEquals("#define SERIALIZE_PROP1 @\"prop1\"\n", result);
	}

	@Test
	public void testBuildPoundDefineSerializerDeclarationsFromArrayOfObjects() throws JsonParseException, IOException {
	  String name = "prop1";
	  String result = generator.buildPoundDefineSerializerDeclarations(name);
	  assertEquals("#define SERIALIZE_PROP1 @\"prop1\"\n", result);
	}

	@Test
	public void testBuildPoundDefineSerializerDeclarationsFromString() throws JsonParseException, IOException {
	  String name = "prop1";
	  String result = generator.buildPoundDefineSerializerDeclarations(name);
	  assertEquals("#define SERIALIZE_PROP1 @\"prop1\"\n", result);
	}

	@Test
	public void testBuildPoundDefineSerializerDeclarationsFromInt() throws JsonParseException, IOException {
	  String name = "prop1";
	  String result = generator.buildPoundDefineSerializerDeclarations(name);
	  assertEquals("#define SERIALIZE_PROP1 @\"prop1\"\n", result);
	}

	@Test
	public void testBuildPoundDefineSerializerDeclarationsFromDecimal() throws JsonParseException, IOException {
	  String name = "prop1";
	  String result = generator.buildPoundDefineSerializerDeclarations(name);
	  assertEquals("#define SERIALIZE_PROP1 @\"prop1\"\n", result);
	}
	
	//
	// Tests of buildGeneratedSerializePropertyString method
	//
	@Test
	public void testBuildGeneratedSerializePropertyStringFromArrayOfObjects() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_OBJECTS);
		String className = "Parent";
		String name = "prop1";
		String result = generator.buildGeneratedSerializePropertyString(node.get(name), className, name);
		assertNotEquals("", result);
	}
	
	@Test
	@Ignore("TODO - I haven't figured out how I'm going to address this yet")
	public void testBuildGeneratedSerializePropertyStringFromArrayOfArrays() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_ARRAYS);
		String className = "Parent";
		String name = "prop1";
		String result = generator.buildGeneratedSerializePropertyString(node.get(name), className, name);
		assertNotEquals("", result);
	}
	
	@Test
	public void testBuildGeneratedSerializePropertyStringFromArrayOfStrings() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_STRINGS);
		String className = "Parent";
		String name = "prop1";
		String result = generator.buildGeneratedSerializePropertyString(node.get(name), className, name);
		assertEquals("[Serializer setDict:dict object:self.prop1 forKey:SERIALIZE_PROP1];\n", result);
	}
	
	@Test
	public void testBuildGeneratedSerializePropertyStringFromDecimal() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_DECIMAL);
		String className = "Parent";
		String name = "prop1";
		String result = generator.buildGeneratedSerializePropertyString(node.get(name), className, name);
		assertEquals("[Serializer setDict:dict doubleValue:self.prop1 forKey:SERIALIZE_PROP1];\n", result);
	}
	
	@Test
	public void testBuildGeneratedSerializePropertyStringFromBool() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_BOOLEAN);
		String className = "Parent";
		String name = "prop1";
		String result = generator.buildGeneratedSerializePropertyString(node.get(name), className, name);
		assertEquals("[Serializer setDict:dict boolValue:self.prop1 forKey:SERIALIZE_PROP1];\n", result);
	}
	
	@Test
	public void testBuildGeneratedSerializePropertyStringFromInt() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_INT);
		String className = "Parent";
		String name = "prop1";
		String result = generator.buildGeneratedSerializePropertyString(node.get(name), className, name);
		assertEquals("[Serializer setDict:dict intValue:self.prop1 forKey:SERIALIZE_PROP1];\n", result);
	}
	
	@Test
	public void testBuildGeneratedSerializePropertyStringFromString() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_STRING);
		String className = "Parent";
		String name = "prop1";
		String result = generator.buildGeneratedSerializePropertyString(node.get(name), className, name);
		assertEquals("[Serializer setDict:dict object:self.prop1 forKey:SERIALIZE_PROP1];\n", result);
	}
	
	//
	// Tests of buildPropertyDeclaration
	//
	@Test
	public void testBuildPropertyDeclarationFromArrayOfObjects() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_OBJECTS);
		String name = "prop1";
		String result = generator.buildPropertyDeclaration(node.get(name), "Foo", name);
		assertEquals("@property (nonatomic, strong) NSMutableArray *prop1;\n", result);
	}
	
	@Test
	public void testBuildPropertyDeclarationFromArrayOfStrings() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_STRINGS);
		String name = "prop1";
		String result = generator.buildPropertyDeclaration(node.get(name), "Foo", name);
		assertEquals("@property (nonatomic, strong) NSMutableArray *prop1;\n", result);
	}

	@Test
	public void testBuildPropertyDeclarationFromString() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_STRING);
		String name = "prop1";
		String result = generator.buildPropertyDeclaration(node.get(name), "Foo", name);
		assertEquals("@property (nonatomic, strong) NSString *prop1;\n", result);
	}
	
	@Test
	public void testBuildPropertyDeclarationFromInt() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_INT);
		String name = "prop1";
		String result = generator.buildPropertyDeclaration(node.get(name), "Foo", name);
		assertEquals("@property (nonatomic) NSInteger prop1;\n", result);
	}
	
	@Test
	public void testBuildPropertyDeclarationFromBool() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_BOOLEAN);
		String name = "prop1";
		String result = generator.buildPropertyDeclaration(node.get(name), "Foo", name);
		assertEquals("@property (nonatomic) BOOL prop1;\n", result);
	}
	
	@Test
	public void testBuildPropertyDeclarationFromDecimal() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_DECIMAL);
		String name = "prop1";
		String result = generator.buildPropertyDeclaration(node.get(name), "Foo", name);
		assertEquals("@property (nonatomic) double prop1;\n", result);
	}
	
	private JsonNode parseJsonObject(String json) throws JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getJsonFactory();
		JsonParser parser = factory.createJsonParser(json);
		return mapper.readTree(parser);
	}

}
