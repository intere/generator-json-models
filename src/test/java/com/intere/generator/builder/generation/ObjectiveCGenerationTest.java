package com.intere.generator.builder.generation;

import static com.intere.generator.test.TestUtils.parseJsonObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.intere.generator.test.TestStrings;

public class ObjectiveCGenerationTest implements TestStrings {
	
	private ObjectiveCGeneration generator;
	
	@Before
	public void setUp() {
		generator = new ObjectiveCGeneration();
	}
	
	//
	// Tests of buildGenerateDeserializePropertyString
	//
	@Test
	public void testBuildGenerateDeserializePropertyFromIsoDate() throws IOException {
		JsonNode node = parseJsonObject(PROP_DATE_ISO);
		String result = generator.buildGenerateDeserializePropertyString(node.get(PROP_NAME), "Foo",  PROP_NAME);
		assertEquals("object.prop1 = [Serializer safeGetDictString:dict withKey:SERIALIZE_PROP1];\n", result);
	}
	
	/*@Test
	public void testBuildGenerateDeserializePropertyFromLongDate() throws IOException {
		JsonNode node = parseJsonObject(PROP_DATE_LONG);
		String result = generator.buildGenerateDeserializePropertyString(node.get(PROP_NAME), "Foo",  PROP_NAME);
		assertEquals("object.prop1 = [Serializer getIntegerFromDict:dict forKey:SERIALIZE_PROP1 orDefaultTo:0];\n", result);
	}
	*/
	
	@Test
	public void testBuildGenerateDeserializePropertyStringFromArrayOfStrings() throws JsonParseException, IOException {
	  JsonNode node = parseJsonObject(PROP_ARRAY_OF_STRINGS);
	  String result = generator.buildGenerateDeserializePropertyString(node.get(PROP_NAME), "Foo",  PROP_NAME);
	  assertEquals("object.prop1 = [[NSMutableArray alloc]initWithArray:[Serializer getArrayFromDict:dict forKey:SERIALIZE_PROP1]];\n", result);
	}

	@Test
	public void testBuildGenerateDeserializePropertyStringFromArrayOfObjects() throws JsonParseException, IOException {
	  JsonNode node = parseJsonObject(PROP_ARRAY_OF_OBJECTS);
	  String result = generator.buildGenerateDeserializePropertyString(node.get(PROP_NAME), "Foo",  PROP_NAME);
	  assertEquals("object.prop1 = [FooProp1 fromArrayOfDictionaries:[Serializer getArrayFromDict:dict forKey:SERIALIZE_PROP1]];\n", result);
	}
	
	@Test
	public void testBuildGenerateDeserializePropertyStringFromArrayOfArrays() throws JsonParseException, IOException {
	  JsonNode node = parseJsonObject(PROP_ARRAY_OF_ARRAYS);
	  String result = generator.buildGenerateDeserializePropertyString(node.get(PROP_NAME), "Foo",  PROP_NAME);
	  assertEquals("", result);
	}

	@Test
	public void testBuildGenerateDeserializePropertyStringFromString() throws JsonParseException, IOException {
	  JsonNode node = parseJsonObject(PROP_STRING);
	  String result = generator.buildGenerateDeserializePropertyString(node.get(PROP_NAME), "Foo",  PROP_NAME);
	  assertEquals("object.prop1 = [Serializer safeGetDictString:dict withKey:SERIALIZE_PROP1];\n", result);
	}

	@Test
	public void testBuildGenerateDeserializePropertyStringFromInt() throws JsonParseException, IOException {
	  
	  JsonNode node = parseJsonObject(PROP_INT);
	  String result = generator.buildGenerateDeserializePropertyString(node.get(PROP_NAME), "Foo",  PROP_NAME);
	  assertEquals("object.prop1 = [Serializer getIntegerFromDict:dict forKey:SERIALIZE_PROP1 orDefaultTo:0];\n", result);
	}
	
	@Test
	public void testBuildGenerateDeserializePropertyStringFromBool() throws JsonParseException, IOException {
	  
	  JsonNode node = parseJsonObject(PROP_BOOLEAN);
	  String result = generator.buildGenerateDeserializePropertyString(node.get(PROP_NAME), "Foo",  PROP_NAME);
	  assertEquals("object.prop1 = [Serializer getBoolFromDict:dict forKey:SERIALIZE_PROP1 orDefaultTo:NO];\n", result);
	}

	@Test
	public void testBuildGenerateDeserializePropertyStringFromDecimal() throws JsonParseException, IOException {
	  
	  JsonNode node = parseJsonObject(PROP_DECIMAL);
	  String result = generator.buildGenerateDeserializePropertyString(node.get(PROP_NAME), "Foo",  PROP_NAME);
	  assertEquals("object.prop1 = [Serializer getDoubleFromDict:dict forKey:SERIALIZE_PROP1 orDefaultTo:0.0];\n", result);
	}
	
	//
	// Tests of buildPoundDefineSerializerDeclarations method
	//
	@Test
	public void testBuildPoundDefineSerializerDeclarationsFromArrayOfStrings() throws JsonParseException, IOException {
	  
	  String result = generator.buildPoundDefineSerializerDeclarations(PROP_NAME);
	  assertEquals("#define SERIALIZE_PROP1 @\"prop1\"\n", result);
	}

	@Test
	public void testBuildPoundDefineSerializerDeclarationsFromArrayOfObjects() throws JsonParseException, IOException {
	  
	  String result = generator.buildPoundDefineSerializerDeclarations(PROP_NAME);
	  assertEquals("#define SERIALIZE_PROP1 @\"prop1\"\n", result);
	}

	@Test
	public void testBuildPoundDefineSerializerDeclarationsFromString() throws JsonParseException, IOException {
	  
	  String result = generator.buildPoundDefineSerializerDeclarations(PROP_NAME);
	  assertEquals("#define SERIALIZE_PROP1 @\"prop1\"\n", result);
	}

	@Test
	public void testBuildPoundDefineSerializerDeclarationsFromInt() throws JsonParseException, IOException {
	  
	  String result = generator.buildPoundDefineSerializerDeclarations(PROP_NAME);
	  assertEquals("#define SERIALIZE_PROP1 @\"prop1\"\n", result);
	}

	@Test
	public void testBuildPoundDefineSerializerDeclarationsFromDecimal() throws JsonParseException, IOException {
	  
	  String result = generator.buildPoundDefineSerializerDeclarations(PROP_NAME);
	  assertEquals("#define SERIALIZE_PROP1 @\"prop1\"\n", result);
	}
	
	//
	// Tests of buildGeneratedSerializePropertyString method
	//
	@Test
	public void testBuildGeneratedSerializePropertyStringFromArrayOfObjects() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_OBJECTS);
		String classPROP_NAME = "Parent";
		
		String result = generator.buildGeneratedSerializePropertyString(node.get(PROP_NAME), classPROP_NAME, PROP_NAME);
		assertNotEquals("", result);
	}
	
	@Test
	@Ignore("TODO - I haven't figured out how I'm going to address this yet")
	public void testBuildGeneratedSerializePropertyStringFromArrayOfArrays() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_ARRAYS);
		String classPROP_NAME = "Parent";
		
		String result = generator.buildGeneratedSerializePropertyString(node.get(PROP_NAME), classPROP_NAME, PROP_NAME);
		assertNotEquals("", result);
	}
	
	@Test
	public void testBuildGeneratedSerializePropertyStringFromArrayOfStrings() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_STRINGS);
		String classPROP_NAME = "Parent";
		
		String result = generator.buildGeneratedSerializePropertyString(node.get(PROP_NAME), classPROP_NAME, PROP_NAME);
		assertEquals("[Serializer setDict:dict object:self.prop1 forKey:SERIALIZE_PROP1];\n", result);
	}
	
	@Test
	public void testBuildGeneratedSerializePropertyStringFromDecimal() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_DECIMAL);
		String classPROP_NAME = "Parent";
		
		String result = generator.buildGeneratedSerializePropertyString(node.get(PROP_NAME), classPROP_NAME, PROP_NAME);
		assertEquals("[Serializer setDict:dict doubleValue:self.prop1 forKey:SERIALIZE_PROP1];\n", result);
	}
	
	@Test
	public void testBuildGeneratedSerializePropertyStringFromBool() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_BOOLEAN);
		String classPROP_NAME = "Parent";
		
		String result = generator.buildGeneratedSerializePropertyString(node.get(PROP_NAME), classPROP_NAME, PROP_NAME);
		assertEquals("[Serializer setDict:dict boolValue:self.prop1 forKey:SERIALIZE_PROP1];\n", result);
	}
	
	@Test
	public void testBuildGeneratedSerializePropertyStringFromInt() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_INT);
		String classPROP_NAME = "Parent";
		
		String result = generator.buildGeneratedSerializePropertyString(node.get(PROP_NAME), classPROP_NAME, PROP_NAME);
		assertEquals("[Serializer setDict:dict intValue:self.prop1 forKey:SERIALIZE_PROP1];\n", result);
	}
	
	@Test
	public void testBuildGeneratedSerializePropertyStringFromString() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_STRING);
		String classPROP_NAME = "Parent";
		
		String result = generator.buildGeneratedSerializePropertyString(node.get(PROP_NAME), classPROP_NAME, PROP_NAME);
		assertEquals("[Serializer setDict:dict object:self.prop1 forKey:SERIALIZE_PROP1];\n", result);
	}
	
	//
	// Tests of buildPropertyDeclaration
	//
	@Test
	public void testBuildPropertyDeclarationFromArrayOfObjects() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_OBJECTS);
		
		String result = generator.buildPropertyDeclaration(node.get(PROP_NAME), "Foo", PROP_NAME);
		assertEquals("@property (nonatomic, strong) NSMutableArray *prop1;\n", result);
	}
	
	@Test
	public void testBuildPropertyDeclarationFromArrayOfStrings() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_ARRAY_OF_STRINGS);
		
		String result = generator.buildPropertyDeclaration(node.get(PROP_NAME), "Foo", PROP_NAME);
		assertEquals("@property (nonatomic, strong) NSMutableArray *prop1;\n", result);
	}

	@Test
	public void testBuildPropertyDeclarationFromString() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_STRING);
		
		String result = generator.buildPropertyDeclaration(node.get(PROP_NAME), "Foo", PROP_NAME);
		assertEquals("@property (nonatomic, strong) NSString *prop1;\n", result);
	}
	
	@Test
	public void testBuildPropertyDeclarationFromInt() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_INT);
		
		String result = generator.buildPropertyDeclaration(node.get(PROP_NAME), "Foo", PROP_NAME);
		assertEquals("@property (nonatomic) NSInteger prop1;\n", result);
	}
	
	@Test
	public void testBuildPropertyDeclarationFromBool() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_BOOLEAN);
		
		String result = generator.buildPropertyDeclaration(node.get(PROP_NAME), "Foo", PROP_NAME);
		assertEquals("@property (nonatomic) BOOL prop1;\n", result);
	}
	
	@Test
	public void testBuildPropertyDeclarationFromDecimal() throws JsonParseException, IOException {
		JsonNode node = parseJsonObject(PROP_DECIMAL);
		
		String result = generator.buildPropertyDeclaration(node.get(PROP_NAME), "Foo", PROP_NAME);
		assertEquals("@property (nonatomic) double prop1;\n", result);
	}
}
