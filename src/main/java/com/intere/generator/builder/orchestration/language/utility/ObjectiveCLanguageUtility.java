package com.intere.generator.builder.orchestration.language.utility;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.models.ObjectiveCModelInterpreter;
import com.intere.generator.builder.orchestration.OrchestrationDataType;
import com.intere.generator.builder.orchestration.OrchestrationTree;
import com.intere.generator.builder.orchestration.OrchestrationUtils;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

public class ObjectiveCLanguageUtility extends AbstractLanguageUtility {
	private static final Logger LOGGER = LogManager.getLogger(ObjectiveCLanguageUtility.class);
	JsonLanguageInterpreter interpreter = new ObjectiveCModelInterpreter();
	
	@Override
	public String buildNamespace(ModelClass modelClass) {
		return "";
	}

	@Override
	public String buildClassDeclaration(ModelClass modelClass) {
		return "@class " + modelClass.getClassName() + " : NSObject\n";
	}

	@Override
	public String finishClass(ModelClass modelClass, boolean testClass) {
		return "@end\t" + singleLineComment("End of " + (testClass ? modelClass.getTestClassName() : modelClass.getClassName()) + " Class", 0) + "\n\n";
	}

	@Override
	public String buildSinglePropertyDeclaration(ModelClassProperty property) {
		StringBuilder builder = new StringBuilder();
		String propertyType = getPropertyType(property);
		String comment = (property.getIsArray() ? "\t\t" + singleLineComment("Array of " + property.getArraySubType()) : "");
		builder.append("@property " + getPropertyDecorations(property) + propertyType + " " 
				+ (property.getIsPrimitive() ? "" : "*") + property.getName() + ";" + comment + "\n");
		return builder.toString();
	}

	private String getPropertyDecorations(ModelClassProperty property) {
		OrchestrationDataType dt = OrchestrationDataType.fromModelProperty(property);
		if(null != dt) {
			switch(dt) {
			case ARRAY:
			case CLASS:
			case DATE:
			case IMAGE:
			case STRING:
			case UNKNOWN:
				return "(nonatomic, strong) ";
			default:
				return "";
			}
		}
		return "";
	}

	@Override
	public String buildGettersAndSetters(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildGetterAndSetter(ModelClassProperty prop) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPropertyType(ModelClassProperty property) {
		OrchestrationDataType type = OrchestrationDataType.fromModelProperty(property);
		if(OrchestrationDataType.CLASS == type) {
			return property.getType();
		}
		switch(type) {				
			default:
				return type.getObjectiveCName();
		}
	}

	@Override
	public String buildImports(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		for(ModelClassProperty prop : modelClass.getProperty()) {
			OrchestrationDataType dt = OrchestrationDataType.fromModelProperty(prop);
			if(null != dt) {
				switch(dt) {
				case CLASS:
					builder.append("#import \"" + prop.getType() + ".h\"\n");
					break;
				default:
					break;
				}
			} else if(null == prop.getArraySubType()) {
				builder.append("#import \"" + prop.getType() + ".h\"\n");
			}
			if(null != prop.getArraySubType()) {
				OrchestrationDataType subType = OrchestrationDataType.fromString(prop.getArraySubType());
				if(null != subType) {
					switch(subType) {
					case CLASS:
						builder.append("#import \"" + prop.getArraySubType() + ".h\"\n");
						break;
					default:
						break;
					}
				} else {
					builder.append("#import \"" + prop.getArraySubType() + ".h\"\n");
				}
			}
		}
		builder.append("\n");
		
		return builder.toString();
	}

	@Override
	public String buildClassImplementation(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("#import \"" + modelClass.getClassName() + ".h\"\n");
		builder.append("#import \"Serializer.h\"\n\n");
		builder.append("@implementation " + modelClass.getClassName() + "\n");
		return builder.toString();
	}

	@Override
	public String buildSerializationConstants(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		for(ModelClassProperty prop : modelClass.getProperty()) {
			builder.append("#define " + interpreter.createSerializeConstantSymbolName(prop.getName()) 
					+ " @\"" + prop.getName() + "\"\n");
		}
		builder.append("\n");
		return builder.toString();
	}

	@Override
	public String buildModelUtilityDeclarationMethods(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(multiLineComment("Converts this " + modelClass.getClassName() 
				+ " to a NSDictionary object (as a serialization mechanism).", 0) + "\n");
		builder.append("-(NSDictionary *)toDictionary;\n\n");
		
		builder.append(multiLineComment("Converts this " + modelClass.getClassName() 
				+ " to a JSON String (as a serializaton mechanism).", 0) + "\n");
		builder.append("-(NSString *)toJson;\n\n");
		
		builder.append(multiLineComment("Converts this " + modelClass.getClassName() 
				+ " to a JSON String (as a serialization mechanism) with optional pretty print.", 0) + "\n");
		builder.append("-(NSString *)toJson:(BOOL)pretty;\n\n");
		
		builder.append(multiLineComment("\"Deserializes\" the provided dictionary into a " 
				+ modelClass.getClassName() + ".", 0) + "\n");
		builder.append("+(" + modelClass.getClassName() + " *)fromDictionary:(NSDictionary *)dict;\n\n");
		
		builder.append(multiLineComment("Deserializes the provided JSON String into a " 
				+ modelClass.getClassName() + ".", 0) + "\n");
		builder.append("+(" + modelClass.getClassName() + " *)fromJsonString:(NSString *)json;\n\n");
		
		builder.append(multiLineComment("Deserializes the provided array of dictionaries into an array of " 
				+ modelClass.getClassName() + ".", 0) + "\n");
		builder.append("+(NSMutableArray *)fromArrayOfDictionaries:(NSArray *)array;\n\n");
		
		builder.append(multiLineComment("Serializes the provided Array of " + modelClass.getClassName() 
				+ " to an Array of Dictionaries.", 0) + "\n");
		builder.append("+(NSMutableArray *)toArrayOfDictionaries:(NSArray *)array;\n\n");			
		return builder.toString();
	}

	@Override
	public String buildModelUtilityDefinitionMethods(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("#pragma mark Serialization Methods\n\n");
		
		// toDictionary method
		builder.append("-(NSDictionary *)toDictionary {\n"
				+ tabs(1) + "NSMutableDictionary *dict = [[NSMutableDictionary alloc]init];\n");
		for(ModelClassProperty prop : modelClass.getProperty()) {
			builder.append(addSerializeProperty(prop));
		}
		builder.append(tabs(1) + "return dict;\n");
		builder.append("}\n\n");
		
		// toJson Methods
		builder.append("-(NSString *)toJson {\n");
		builder.append(tabs(1) + "return [self toJson:NO];\n");
		builder.append("}\n\n");
		builder.append("-(NSString *)toJson:(BOOL)pretty {\n");
		builder.append(tabs(1) + "NSDictionary *dict = [self toDictionary];\n");
		builder.append(tabs(1) + "return [Serializer jsonStringFromDictionary:dict withPrettyPrint:pretty];\n");
		builder.append("}\n\n");
		
		// fromDictionary method
		builder.append("+(" + modelClass.getClassName() + "*)fromDictionary:(NSDictionary *)dict {\n");
		builder.append(tabs(1) + modelClass.getClassName() + " *object = [[" + modelClass.getClassName() + " alloc]init];\n");
		for(ModelClassProperty prop : modelClass.getProperty()) {
			builder.append(addDeserializeProperty(prop));
		}
		builder.append(tabs(1) + "return object;\n");
		builder.append("}\n\n");
		
		// fromJsonString method
		builder.append("+(" + modelClass.getClassName() + "*)fromJsonString:(NSString *)json {\n");
		builder.append(tabs(1) + "NSDictionary *dict = [Serializer fromJsonString:json];\n"); 
		builder.append(tabs(1) + "return [" + modelClass.getClassName() + " fromDictionary:dict];\n");
		builder.append("}\n\n");
		
		// fromArrayOfDictionaries method
		builder.append("+(NSMutableArray *)fromArrayOfDictionaries:(NSArray *)array {\n");
		builder.append(tabs(1) + "NSMutableArray *result = [[NSMutableArray alloc]init];\n");
		builder.append(tabs(1) + "for (NSDictionary *dict in array) {\n");
		builder.append(tabs(2) + "[result addObject:[" + modelClass.getClassName() + " fromDictionary:dict]];\n");
		builder.append(tabs(1) + "}\n");
		builder.append(tabs(1) + "return result;\n");
		builder.append("}\n\n");
		
		// toArrayOfDictionaries method
		builder.append("+(NSMutableArray *)toArrayOfDictionaries:(NSArray *)array {\n");
		builder.append(tabs(1) + "NSMutableArray *result = [[NSMutableArray alloc]init];\n");
		builder.append(tabs(1) + "for(" + modelClass.getClassName() + " *object in array) {\n");
		builder.append(tabs(2) + "[result addObject:[object toDictionary]];\n");
		builder.append(tabs(1) + "}\n");
		builder.append(tabs(1) + "return result;\n");
		builder.append("}\n\n");				
		
		return builder.toString();
	}
	
	@Override
	public Map<File, String> copyModelResources(File sourcePath, OrchestrationTree tree) throws IOException {
		Map<File, String> resources = new HashMap<>();
		resources.put(new File(sourcePath, "Serializer.h"), readSerializerHeader());
		resources.put(new File(sourcePath, "Serializer.m"), readSerializerImplementation());
		return resources;
	}
	
	/**
	 * Reads the contents of the static Serializer.m file.
	 * @return The contents of the entire Serializer.m file
	 * @throws IOException
	 */
	private String readSerializerImplementation() throws IOException {
		return readResourceAndReplaceHeaders("/Serializer.m");
	}

	/**
	 * Reads the contents of the static Serializer.h file.
	 * @return The contents of the entire Serializer.h file.
	 * @throws IOException
	 */
	private String readSerializerHeader() throws IOException {
		return readResourceAndReplaceHeaders("/Serializer.h");
	}

	private Object addDeserializeProperty(ModelClassProperty prop) {
		OrchestrationDataType dt = OrchestrationDataType.fromModelProperty(prop);
		final String BASE = tabs(1) + "self." + prop.getName() + " = ";
		final String propName = prop.getName();
		final String serConstant = interpreter.createSerializeConstantSymbolName(propName);
		switch(dt) {
		case STRING:
		case IMAGE:
			return BASE + "[Serializer safeGetDictString:dict withKey:" + serConstant + "];\n";
		case DATE:
			return BASE + "[Serializer getDateFromDict:dict forKey:" + serConstant + " orDefaultTo:nil];\n";
		case BOOLEAN:
			return BASE + "[Serializer getBoolFromDict:dict forKey:" + serConstant + " orDefaultTo:NO];\n";
		case DOUBLE:
			return BASE + "[Serializer getDoubleFromDict:dict forKey:" + serConstant + " orDefaultTo:0.0];\n";
		case LONG:
			return BASE + "[Serializer getIntegerFromDict:dict forKey:" + serConstant + " orDefaultTo:0];\n";
		case CLASS:
			return tabs(1) + "if([dict objectForKey:" + serConstant + "] && ![[NSNull null] isEqual:[dict objectForKey:" + serConstant + "]]) {\n" + 
					tabs(1) + BASE + "[" + propName + " fromDictionary:[dict objectForKey:" + serConstant + "]];\n" + 
					tabs(1) + "}\n";
		case ARRAY:
			OrchestrationDataType subType = OrchestrationDataType.fromString(prop.getArraySubType());
			if(null != subType) {
				switch(subType) {
				case STRING:
				case BOOLEAN:
				case DATE:
				case DOUBLE:
				case LONG:
				case IMAGE:
					return BASE + "[[NSMutableArray alloc]initWithArray:[Serializer getArrayFromDict:dict forKey:" + serConstant + "]];\n";
					
				case ARRAY:	
				case UNKNOWN:
					
					return BASE + "[[NSMutableArray alloc]initWithArray:[Serializer getArrayFromDict:dict forKey:" + serConstant + "]];\n";
				case CLASS:
					return BASE + "[" + prop.getArraySubType() + " fromArrayOfDictionaries:[Serializer getArrayFromDict:dict forKey:" + serConstant + "]];\n";
				}
			} else {
				LOGGER.warn("No Subtype for: " + prop.getArraySubType());
			}
			return "\n";
		default:
			break;	
		}
		return "";
	}

	private String addSerializeProperty(ModelClassProperty prop) {
		OrchestrationDataType dt = OrchestrationDataType.fromModelProperty(prop);
		final String BASE = tabs(1) + "[Serializer setDict:dict ";
		final String propName = prop.getName();
		final String serConstant = interpreter.createSerializeConstantSymbolName(propName);
		
		switch(dt) {
		case CLASS:
			return BASE + "object:[self." + propName + " toDictionary] forKey:" + serConstant + "];\n";
			
		case STRING:
			return BASE + "object:self." + propName + " forKey:" + serConstant + "];\n";
			
		case DATE:
			return BASE + "dateValue:self." + propName + " forKey:" + serConstant + "];\n";
			
		case DOUBLE:
			return BASE + "doubleValue:self." + propName + " forKey:" + serConstant + "];\n";
			
		case LONG:
			return BASE + "intValue:self." + propName + " forKey:" + serConstant + "];\n";
			
		case BOOLEAN:
			return BASE + "boolValue:self." + propName + " forKey:" + serConstant + "];\n";
		
		case ARRAY:
			if(null != prop.getArraySubType()) {
				OrchestrationDataType subType = OrchestrationDataType.fromString(prop.getArraySubType());
				if(null != subType) {
					switch(subType) {
					case UNKNOWN:
					case CLASS:
						return BASE + "object:[" + prop.getArraySubType() + " toArrayOfDictionaries:self." + propName 
								+ "] forKey:" + serConstant + "];\n"; 
					default:
						return BASE + "object:self." + propName + " forKey:" + serConstant + "];\n";
					}
				}
			}					 
			
		case UNKNOWN:
			return "// TODO: UNKNOWN Serialization: " + propName + " (" + prop.getType() + ", " + prop.getArraySubType() + ")\n";
			
		default:
			break;
		}
		
		return "";
	}

	@Override
	public String buildTestClassDeclaration(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("@interface " + modelClass.getTestClassName() + " : XCTestCase\n");
		builder.append(singleLineComment("TODO: Any properties your tests might need", 1) + "\n");
		builder.append("@end\n\n");
		builder.append("@implementation " + modelClass.getTestClassName() + "\n");
		return builder.toString();
	}

	@Override
	public String buildTestImports(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("#import <UIKit/UIKit.h>\n");
		builder.append("#import <XCTest/XCTest.h>\n");
		builder.append("#import \"Serializer.h\"\n");
		builder.append("#import \"" + modelClass.getClassName() + ".h\";\n\n");
		return builder.toString();
	}

	@Override
	public String buildTestSetupMethod(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("- (void)setUp {\n");
		builder.append(tabs(1) + "[super setUp];\n");
		builder.append(singleLineComment("Put setup code here. This method is called before the invocation of each test method in the class.",1) + "\n");
		builder.append("}\n\n");
		builder.append("- (void)tearDown {\n");
		builder.append(singleLineComment("Put teardown code here. This method is called after the invocation of each test method in the class.",1) + "\n");
		builder.append(tabs(1) + "[super tearDown];\n");
		builder.append("}\n\n");
		return builder.toString();
	}

	@Override
	public String buildTestMethods(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		for(ModelClassProperty prop : modelClass.getProperty()) {
			builder.append(buildTestMethod(prop));
		}
		return builder.toString();
	}

	private String buildTestMethod(ModelClassProperty prop) {
		StringBuilder builder = new StringBuilder();
		builder.append(multiLineComment("Tests Serialization / Deserialization of the " + prop.getName() + " property", 0) + "\n");
		builder.append("test" + interpreter.buildClassName(prop.getName()) + " {\n");
		OrchestrationDataType dt = OrchestrationDataType.fromModelProperty(prop);
		switch(dt) {
		case BOOLEAN:
			builder.append("NSString *json=@\"{\\\"" + prop.getName() + "\\\":true}\";\n");
		}
		builder.append("}\n\n");
		return builder.toString();
	}

	@Override
	public void enforceFilenames(OrchestrationTree tree) {
		// No-Op for Objective C		
	}		
}

