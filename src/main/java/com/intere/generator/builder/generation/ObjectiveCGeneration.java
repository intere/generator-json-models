package com.intere.generator.builder.generation;

import static com.intere.generator.deserializer.JsonNodeUtils.getExtensionFromFilename;
import static com.intere.generator.deserializer.JsonNodeUtils.getFilenameWithoutPathWithoutExtension;
import static com.intere.generator.deserializer.JsonNodeUtils.isArray;
import static com.intere.generator.deserializer.JsonNodeUtils.isArrayOfObjects;
import static com.intere.generator.deserializer.JsonNodeUtils.isArrayofArrays;
import static com.intere.generator.deserializer.JsonNodeUtils.isBoolean;
import static com.intere.generator.deserializer.JsonNodeUtils.isDate;
import static com.intere.generator.deserializer.JsonNodeUtils.isFloat;
import static com.intere.generator.deserializer.JsonNodeUtils.isInteger;
import static com.intere.generator.deserializer.JsonNodeUtils.isLong;
import static com.intere.generator.deserializer.JsonNodeUtils.isObject;
import static com.intere.generator.deserializer.JsonNodeUtils.isText;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;

import com.intere.generator.App;
import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.ObjectiveCInterpreter;
import com.intere.generator.deserializer.JsonDeserializer;

/**
 * Code Generator for Objective-C.
 * 
 * @author einternicola
 */
public class ObjectiveCGeneration extends CodeGeneration {
	private static final JsonLanguageInterpreter INTERPRETER = new ObjectiveCInterpreter();

	public JsonLanguageInterpreter getInterpreter() {
		return INTERPRETER;
	}
	
	/**
	 * Generates the Header file using the JSON String.
	 * @return a String.
	 * @throws IOException 
	 * @throws JsonParseException 
	 */
	public String generateHeaderFile(JsonDeserializer deserializer) {
		String className = deserializer.getName();
		NavigableMap<String, List<JsonDeserializer>> subClasses = deserializer.getSubClasses();
		JsonNode node = deserializer.getNode();
		StringBuilder builder = new StringBuilder();
		builder.append(generateHeaderCommentBlock(className + ".h"));
		builder.append("#import <Foundation/Foundation.h>\n\n");
		
		if(subClasses.containsKey(className)) {
			for(JsonDeserializer des : subClasses.get(className)) {
				builder.append("#import \"" + des.getName() + ".h\"\n");
			}
		}
		
		builder.append("@interface " + className + " : NSObject\n\n" +
				"//\n" +
				"// Properties\n" + 
				"//\n\n");
		
		// Now - go generate all of the properties:
		Iterator<String> iter = node.getFieldNames();
		
		while(iter.hasNext()) {
			String name = iter.next();
			builder.append(buildPropertyDeclaration(node.get(name), className, name));
		}
		
		//
		// Now generate the serialize / deserialize declarations:
		//
		builder.append("\n\n" +
			"/**\n" + 
			" * Converts this " + className + " to a NSDictionary object (as a serialization mechanism).\n" + 
			" */\n" + 
			"-(NSDictionary *)toDictionary;\n" + 
			"\n" + 
			"/**\n" + 
			" * Converts this " + className + " to a JSON String (as a serializaton mechanism).\n" + 
			" */\n" + 
			"-(NSString *)toJson;\n" +
			"/**\n" +
			" * Converts this " + className + " to a JSON String (as a serialization mechanism) with optional pretty print.\n" +
			" */\n" + 
			"-(NSString *)toJson:(BOOL)pretty;\n" + 
			"\n" + 
			"/**\n" + 
			" * \"Deserializes\" the provided dictionary into a " + className + ".\n" + 
			" */\n" + 
			"+(" + className + " *)fromDictionary:(NSDictionary *)dict;\n" + 
			"\n" + 
			"/**\n" + 
			" * Deserializes the provided JSON String into a " + className + ".\n" + 
			" */\n" + 
			"+(" + className + " *)fromJsonString:(NSString *)json;\n" + 
			"/**\n" + 
			" * \"Deserializes\" the provided array of dictionaries into an array of " + className + ".\n" + 
			" */\n" + 
			"+(NSMutableArray *)fromArrayOfDictionaries:(NSArray *)array;\n" + 
			"\n" + 
			"/**\n" + 
			" * \"Serializes\" the provided Array of " + className + " to an Array of Dictionaries.\n" + 
			" */\n" + 
			"+(NSMutableArray *)toArrayOfDictionaries:(NSArray *)array;" +
			"\n\n");
		
		builder.append("@end\n\n");
		
		return builder.toString();
	}
	
	public String buildPoundDefineSerializerDeclarations(String name) {
		String defName = getInterpreter().createSerializeConstantSymbolName(name);
		return "#define " + defName + " @\"" + name + "\"\n";
	}
	
	/**
	 * Builds the ".m" (Implementation) file.
	 * @return The file contents.
	 */
	public String generateImplementationFile(JsonDeserializer deserializer) {		
		String className = deserializer.getName();
		JsonNode node = deserializer.getNode();
		
		StringBuilder builder = new StringBuilder();
		builder.append(generateHeaderCommentBlock(className + ".m"));
		builder.append("#import \"" + className + ".h\"\n" + 
				"#import \"Serializer.h\"\n\n");
		
		//
		// Create the serialization precompiler definitions:
		//
		Iterator<String> iter = node.getFieldNames();
		while(iter.hasNext()) {			
			String name = iter.next();
			builder.append(buildPoundDefineSerializerDeclarations(name));
		}
		
		//
		// Class Definition:
		//
		builder.append("\n\n@implementation " + className + "\n\n" +
			"#pragma mark Serialization Methods\n\n");
		
		//
		// toDictionary Method
		//
		builder.append("-(NSDictionary *)toDictionary {\n" + 
			"\tNSMutableDictionary *dict = [[NSMutableDictionary alloc]init];\n\n");
		iter = node.getFieldNames();
		
		while(iter.hasNext()) {
			String name = iter.next();
			builder.append("\t" + buildGeneratedSerializePropertyString(node.get(name), className, name));
		}
		builder.append("\n\treturn dict;\n}\n\n");
		
		//
		// To JSON methods:
		//
		builder.append("-(NSString *)toJson {\n" + 
				"\treturn [self toJson:NO];\n" + 
				"}\n" + 
				"\n" + 
				"-(NSString *)toJson:(BOOL)pretty {\n" + 
				"\tNSDictionary *dict = [self toDictionary];\n\n" + 
				"\treturn [Serializer jsonStringFromDictionary:dict withPrettyPrint:pretty];\n" + 
				"\n}");
		
		//
		// fromDictionary Method
		//
		builder.append("\n\n+ (" + className + " *)fromDictionary:(NSDictionary *)dict {\n" + 
				"\t" + className + " *object = [[" + className + " alloc]init];\n");
		iter = node.getFieldNames();
		while(iter.hasNext()) {
			String name = iter.next();
			builder.append("\t" + buildGenerateDeserializePropertyString(node.get(name), className, name));
		}
		
		builder.append("\treturn object;\n}\n\n");
		
		//
		// fromJSON Method:
		//
		builder.append("+(" + className + " *)fromJsonString:(NSString *)json {\n" + 
				"\tNSDictionary *dict = [Serializer fromJsonString:json];\n" + 
				"\treturn [" + className + " fromDictionary:dict];\n" + 
				"}\n\n");
		//
		// fromArrayOfDictionaries Method:
		//
		builder.append("+(NSMutableArray *)fromArrayOfDictionaries:(NSArray *)array {\n" + 
				"    NSMutableArray *result = [[NSMutableArray alloc]init];\n" + 
				"    \n" + 
				"    for (NSDictionary *dict in array) {\n" + 
				"        [result addObject:[" + className + " fromDictionary:dict]];\n" + 
				"    }\n" + 
				"    \n" + 
				"    return result;\n" + 
				"}\n\n");
		
		//
		// toArrayOfDictionaries Method:
		//
		builder.append("+(NSMutableArray *)toArrayOfDictionaries:(NSArray *)array {\n" + 
				"    NSMutableArray *result = [[NSMutableArray alloc]init];\n" + 
				"    \n" + 
				"    for(" + className + " *object in array) {\n" + 
				"        [result addObject:[object toDictionary]];\n" + 
				"    }\n" + 
				"    \n" + 
				"    return result;\n" + 
				"}\n\n");
		
		builder.append("@end\n\n");		
		return builder.toString();
	}
	
	@Override
	public String generateTestFile(JsonDeserializer deserializer, String jsonFilename) {
		String className = deserializer.getName();
		String testClassName = deserializer.getTestFilename();
		JsonNode node = deserializer.getNode();
		String baseJsonFilename = getFilenameWithoutPathWithoutExtension(jsonFilename);
		String jsonExtension = getExtensionFromFilename(jsonFilename);
		
		StringBuilder builder = new StringBuilder();
		builder.append(generateHeaderCommentBlock(testClassName + ".m"));
		
		builder.append("#import <XCTest/XCTest.h>\n" +
			"#import \"" + className + ".h\"\n" + 
			"#import \"Serializer.h\"\n\n");
		builder.append("@interface " + testClassName + " : XCTestCase{\n" +
			"\tNSString *sampleJson;\n" +
			"\t" + className + " *deserialized;\n" +
			"}\n" +
			"@end\n\n");
		
		builder.append("#define DATE_LONG 1411065116782\n" + 
				"#define DATE_ISO @\"2014-09-18T11:31:56.782-07:00\"\n" + 
				"#define DATE_ZULU @\"2014-09-18T11:31:56.782Z\"\n" + 
				"#define TEST_KEY @\"date\"\n\n");
		
		builder.append("@implementation " + testClassName + "\n\n");
		builder.append("- (void)setUp\n" + 
				"{\n" + 
				"\t[super setUp];\n" + 
				"\tNSString *pathToResource = [[NSBundle bundleForClass:self.class] pathForResource:@\"" + baseJsonFilename + "\" ofType:@\"" + jsonExtension + "\"];\n" + 
				"\tNSError *error = nil;\n" + 
				"\tsampleJson = [NSString stringWithContentsOfFile:pathToResource encoding:NSUTF8StringEncoding error:&error];\n" + 
				"\tif(error) {\n" + 
				"\t\tXCTFail(@\"Failed to load file: contests.json: %@\", error.localizedDescription);\n" + 
				"\t}\n");
		if(deserializer.isArray()) {
			builder.append("\tNSDictionary *dict = [[Serializer arrayFromJsonString:sampleJson andError:error] objectAtIndex:0];\n" +
					"\tXCTAssertNil(error);\n");
		} else {
			builder.append("\tNSDictionary *dict = [Serializer fromJsonString:sampleJson];\n");
		}
		builder.append("\tdeserialized = [" + className + " fromDictionary:dict];\n" +
				"\tXCTAssertNotNil(deserialized, @\"The " + className + " failed to deserialize properly\");\n" +
				"}\n\n");
		
		builder.append("- (void)tearDown\n" + 
				"{\n" + 
				"    // Put teardown code here; it will be run once, after the last test case.\n" + 
				"    [super tearDown];\n" + 
				"}\n\n");
		
		boolean addDateMethods = false;
		
		Iterator<String> iter = node.getFieldNames();
		while(iter.hasNext()) {			
			String name = iter.next();
			JsonNode childNode = node.get(name);
			String propertyName = getInterpreter().cleanVariableName(name);
			String methodPropName = getInterpreter().buildSubClassName("",name);
			
			if(isDate(childNode)) {
				addDateMethods = true;
				builder.append(generateDateMethods(childNode, propertyName));				
			} else if(isText(childNode)) {
				builder.append("-(void)test" + methodPropName + " {\n" +
					"\tXCTAssertEqualObjects(@\"" + childNode.getTextValue() + "\", deserialized." + propertyName + ", @\"Failed to properly deserialize the " + propertyName + "\");\n" + 
					"}\n\n");
			} else if(isInteger(childNode)) {
				builder.append("-(void)test" + methodPropName + " {\n" +
					"\tXCTAssertEqual((NSInteger)" + childNode.getIntValue() + ", deserialized." + propertyName + ", @\"Failed to properly deserialize the " + propertyName + "\");\n" +
					"}\n\n"
				);
			} else if(isFloat(childNode)) {
				builder.append("-(void)test" + methodPropName + " {\n" +
					"\tXCTAssertEqualWithAccuracy((CGFloat)" + childNode.getDoubleValue() + ", deserialized." + propertyName + 
					", (CGFloat)0.01, @\"Failed to properly deserialize the " + propertyName + "\");\n" +
					"}\n\n"
				);
			} else if(isObject(childNode)) {
				// TODO - this class will need its own test file
			} else if(isArrayOfObjects(childNode)) {
				System.out.println("What is it?");
			} else if(isArrayofArrays(childNode)) {
				System.out.println("What is it?");
			} else if(isArray(childNode)) {
				builder.append("-(void)test" + name + " {\n" +
					"\tXCTAssertEqual((NSUInteger)" + childNode.size() + ", deserialized." + propertyName + ".count, @\"Failed to properly deserialize the " + propertyName + "\");\n" +
					"}\n\n"
				);
			}
		}		
		
		if(addDateMethods) {
			builder.append(testDateMethods());
		}
		
		builder.append("@end\n");
		return builder.toString();
	}
	
	private Object generateDateMethods(JsonNode childNode, String propertyName) {
		return "-(void)testDeserialize" + propertyName + "AsLongDate {\n" +
				"\tNSDictionary *dict = [self createDictionaryWithObject:[NSNumber numberWithLongLong:DATE_LONG]];\n" + 
				"\tNSDate *date = [Serializer getDateFromDict:dict forKey:TEST_KEY orDefaultTo:nil];\n" + 
				"\tXCTAssertNotNil(date, @\"The Serializer did not deserialize the date correctly\");\n" + 
				"}\n\n" + 
				"-(void)testSerialize" + propertyName + "AsIsoDate {\n" + 
				"\tNSString *formattedDate = [self formatIsoDateToString:DATE_ISO];\n" + 
				"\tNSDate *toSerialize = [self formatIsoDateToDate:formattedDate];\n" + 
				"\tNSMutableDictionary *dict = [[NSMutableDictionary alloc]init];\n" + 
				"\tNSString *key = @\"date\";\n" + 
				"\t[Serializer setDict:dict dateValue:toSerialize forKey:key];\n" + 
				"\tXCTAssertNotNil([dict objectForKey:key], @\"The Serializer failed to set the date for key: %@\", key);\n" + 
				"\tXCTAssertTrue([[dict objectForKey:key] isKindOfClass:[NSString class]], @\"We're not even getting an NSString back...\");\n" + 
				"\tNSDate *date = [Serializer getDateFromDict:dict forKey:key orDefaultTo:nil];\n" + 
				"\tNSString *fromDate = [self formatDateToIsoString:date];\n" + 
				"\t\n" + 
				"\tXCTAssertEqualObjects(fromDate, formattedDate, @\"The Serialization of the date failed\");\n" + 
				"}\n\n" + 
				"-(void)testDeserialize" + propertyName + "AsZuluDate {\n" + 
				"\tNSString *formattedDate = [self formatZuluDateToString:DATE_ZULU];\n" + 
				"\tNSDate *toSerialize = [self formatZuluDateToDate:formattedDate];\n" + 
				"\tNSMutableDictionary *dict = [[NSMutableDictionary alloc]init];\n" + 
				"\tNSString *key = @\"date\";\n" + 
				"\t[Serializer setDict:dict dateValue:toSerialize forKey:key];\n" + 
				"\tXCTAssertNotNil([dict objectForKey:key], @\"The Serializer failed to set the date for key: %@\", key);\n" + 
				"\tXCTAssertTrue([[dict objectForKey:key] isKindOfClass:[NSString class]], @\"We're not even getting an NSString back...\");\n" + 
				"\tNSDate *date = [Serializer getDateFromDict:dict forKey:key orDefaultTo:nil];\n" + 
				"\tNSString *fromDate = [self formatDateToZuluString:date];\n" + 
				"\t\n" + 
				"\tXCTAssertEqualObjects(fromDate, formattedDate, @\"The Serialization of the date failed\");\n" + 
				"}\n\n";
	}

	protected String testDateMethods() {
		return "-(NSDictionary *)createDictionaryWithObject:(NSObject *)object {\n" + 
				"\tNSMutableDictionary *dict = [[NSMutableDictionary alloc]init];\n" + 
				"\t[dict setObject:object forKey:TEST_KEY];\n" + 
				"\treturn dict;\n" + 
				"}\n\n" +
				"-(NSString *)formatZuluDateToString:(NSString *)dateString {\n" + 
				"\tNSDateFormatter *dateFormat = [NSDateFormatter new];\n" + 
				"\tdateFormat.dateFormat = @\"yyyy-MM-dd'T'HH:mm:ss.SSSZ\";\n" + 
				"\tNSDate *parsedDate = [dateFormat dateFromString:DATE_ISO];\n" + 
				"\treturn [dateFormat stringFromDate:parsedDate];\n" + 
				"}\n\n" +
				"-(NSDate *)formatZuluDateToDate:(NSString *)dateString {\n" + 
				"\tNSDateFormatter *dateFormat = [NSDateFormatter new];\n" + 
				"\tdateFormat.dateFormat = @\"yyyy-MM-dd'T'HH:mm:ss.SSSZ\";\n" + 
				"\treturn [dateFormat dateFromString:DATE_ISO];\n" + 
				"}\n\n" +
				"-(NSString *)formatDateToZuluString:(NSDate *)date {\n" + 
				"\tNSDateFormatter *dateFormat = [NSDateFormatter new];\n" + 
				"\tdateFormat.dateFormat = @\"yyyy-MM-dd'T'HH:mm:ss.SSSZ\";\n" + 
				"\treturn [dateFormat stringFromDate:date];\n" + 
				"}\n\n" + 
				"-(NSString *)formatIsoDateToString:(NSString *)dateString {\n" + 
				"\tNSDateFormatter *dateFormat = [NSDateFormatter new];\n" + 
				"\tdateFormat.dateFormat = @\"yyyy-MM-dd'T'HH:mm:ss.SSSXXX\";\n" + 
				"\tNSDate *parsedDate = [dateFormat dateFromString:DATE_ISO];\n" + 
				"\treturn [dateFormat stringFromDate:parsedDate];\n" + 
				"}\n\n" +
				"-(NSDate *)formatIsoDateToDate:(NSString *)dateString {\n" + 
				"\tNSDateFormatter *dateFormat = [NSDateFormatter new];\n" + 
				"\tdateFormat.dateFormat = @\"yyyy-MM-dd'T'HH:mm:ss.SSSXXX\";\n" + 
				"\treturn [dateFormat dateFromString:DATE_ISO];\n" + 
				"}\n\n" +
				"-(NSString *)formatDateToIsoString:(NSDate *)date {\n" + 
				"\tNSDateFormatter *dateFormat = [NSDateFormatter new];\n" + 
				"\tdateFormat.dateFormat = @\"yyyy-MM-dd'T'HH:mm:ss.SSSXXX\";\n" + 
				"\treturn [dateFormat stringFromDate:date];\n" + 
				"}\n";
	}
	
	/**
	 * Builds the Serialization snippet per property.  
	 * Think: Serializing properties into the dictionary prior to serializing to JSON.
	 * @param node The node to be serialized.
	 * @param nodeName The name of the node that is going to be serialized.
	 * @return
	 */
	public String buildGeneratedSerializePropertyString(JsonNode node, String className, String nodeName) {
		String variableName = getInterpreter().cleanVariableName(nodeName);
		String defName = getInterpreter().createSerializeConstantSymbolName(nodeName);
		String subClassName = getInterpreter().buildSubClassName(className, nodeName);
		
		if(isText(node)) {
			return "[Serializer setDict:dict object:self." + variableName + " forKey:" + defName + "];\n";
		} else if(isDate(node)) {
			return "[Serializer setDict:dict dateValue:self." + variableName + " forKey:key];";
		} else if(isInteger(node) || isLong(node)) {
			return "[Serializer setDict:dict intValue:self." + variableName + " forKey:" + defName + "];\n";	
		} else if(isFloat(node)) {
			return "[Serializer setDict:dict doubleValue:self." + variableName + " forKey:" + defName + "];\n";
		} else if(isBoolean(node)) {
			return "[Serializer setDict:dict boolValue:self." + variableName + " forKey:" + defName + "];\n";
		} else if(isArrayOfObjects(node)) {
			return "[Serializer setDict:dict object:[" + subClassName + " toArrayOfDictionaries:self."+ variableName + "] forKey:" + defName + "];\n"; 
		} else if(isArrayofArrays(node)) {
				// TODO: how do we handle an array of arrays?
		} else if (isArray(node)) {
			return "[Serializer setDict:dict object:self." + variableName + " forKey:" + defName + "];\n";
		} else if(isObject(node)) {
			return "[Serializer setDict:dict object:[self." + variableName + " toDictionary] forKey:" + defName + "];\n";
		} else {
			System.out.println("Unknown node type: " + node.toString());
		}
		return "";
	}
	
	/**
	 * Generates the <code>@property</code> definitions.
	 * @param node
	 * @param className
	 * @param name
	 * @return
	 */
	public String buildPropertyDeclaration(JsonNode node, String className, String name) {
		String variableName = getInterpreter().cleanVariableName(name);
		String subClassName = getInterpreter().buildSubClassName(className, name);
		
		if(isText(node)) {
			return "@property (nonatomic, strong) NSString *" + variableName + ";\n";
		} else if(isDate(node)) {
			return "@property (nonatomic, strong) NSDate *" + variableName + ";\n";
		} else if(isInteger(node) || isLong(node)) {
			return "@property (nonatomic) NSInteger " + variableName + ";\n";
		} else if(isFloat(node)) {
			return "@property (nonatomic) double " + variableName + ";\n";
		} else if(isObject(node)) {
			return "@property (nonatomic, strong) " + subClassName + " *" + variableName + ";\n";
		} else if(isBoolean(node)) {
			return "@property (nonatomic) BOOL " + variableName + ";\n";
		} else if(isArray(node)) {
			return "@property (nonatomic, strong) NSMutableArray *" + variableName + ";\n";
		} else {
			System.out.println(name + " is some other type of node...");
		}
		return "";
	}
	
	/**
	 * Builds the Deserialization snippet per property.
	 * @param node
	 * @param parentClassName
	 * @param name
	 * @return
	 */
	public String buildGenerateDeserializePropertyString(JsonNode node, String parentClassName, String name) {
		String defName = getInterpreter().createSerializeConstantSymbolName(name);
		String variableName = getInterpreter().cleanVariableName(name);
		String className = getInterpreter().buildSubClassName(parentClassName, name);
		
		if(isText(node)) {
			return "object." + variableName + " = [Serializer safeGetDictString:dict withKey:" + defName + "];\n";
		} else if(isDate(node)) {
			return "object." + variableName + " = [Serializer getDateFromDict:dict withKey:" + defName + " orDefaultTo:nil];\n";
		} else if(isInteger(node) || isLong(node)) {
			return "object." + variableName + " = [Serializer getIntegerFromDict:dict forKey:" + defName + " orDefaultTo:0];\n";
		} else if(isFloat(node)) {
			return "object." + variableName + " = [Serializer getDoubleFromDict:dict forKey:" + defName + " orDefaultTo:0.0];\n";
		} else if(isBoolean(node)) {
			return "object." + variableName + " = [Serializer getBoolFromDict:dict forKey:" + defName + " orDefaultTo:NO];\n";
		} else if(isArrayOfObjects(node)) {
			return "object." + variableName + " = [" + className + " fromArrayOfDictionaries:[Serializer getArrayFromDict:dict forKey:" + defName + "]];\n";
		} else if(isArrayofArrays(node)) {
			// TODO ??
		} else if (isArray(node)){
			return "object." + variableName + " = [[NSMutableArray alloc]initWithArray:[Serializer getArrayFromDict:dict forKey:" + defName + "]];\n";
		} else if(isObject(node)) {
			return "object." + variableName + " = [" + className + " fromDictionary:[dict objectForKey:" + defName + "]];\n";
		} else {
			System.out.println("Unknown Node Type: " + node.toString());
		}
		return "";
	}
	
	protected String generateHeaderCommentBlock(String filename) {
		return "//\n" + 
				"//  " + filename + "\n" + 
				"//\n" + 
				"//  Generated by JSON Model Generator v" + App.getVersion() + " on " + getDate() + ".\n" + 
				"//    https://github.com/intere/generator-json-models\n" +
				"//\n" +
				"//    The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content\n" +
				"//\n" + 
				"//\n\n";
	}
}
