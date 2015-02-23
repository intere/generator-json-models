package com.intere.generator.builder.orchestration.language;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.models.ObjectiveCModelInterpreter;
import com.intere.generator.builder.orchestration.OrchestrationDataType;
import com.intere.generator.builder.orchestration.OrchestrationTree;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

public class ObjectiveCOrchestration implements LanguageOrchestrator {
	LanguageUtility languageUtil = new ObjectiveCLanguageUtility();
	JsonLanguageInterpreter interpreter = new ObjectiveCModelInterpreter();
	
	@Override
	public void generateModels(File outputDirectory, OrchestrationTree tree) throws IOException {
		List<File> generatedClasses = new ArrayList<>();
		for(ModelClass modelClass : tree.getModelClasses()) {
			generatedClasses.add(buildModelHeaderFile(outputDirectory, modelClass));
			generatedClasses.add(buildModelImplementationFile(outputDirectory, modelClass));
		}
	}

	private File buildModelImplementationFile(File outputDirectory, ModelClass modelClass) throws IOException {
		String fileContents = buildClassImplementation(modelClass);
		File outputFile = new File(outputDirectory, modelClass.getFileName() + ".m");
		return writeFile(outputFile, fileContents);
	}

	private File writeFile(File outputFile, String fileContents) throws IOException {
		System.out.println("About to create model class: " + outputFile.getAbsolutePath());
		FileOutputStream fout = new FileOutputStream(outputFile);
		IOUtils.write(fileContents, fout);
		fout.close();
		return outputFile;
	}

	private File buildModelHeaderFile(File outputDirectory, ModelClass modelClass) throws IOException {
		String fileContents = buildClassDeclaration(modelClass);
		File outputFile = new File(outputDirectory, modelClass.getFileName() + ".h");
		return writeFile(outputFile, fileContents);
	}

	private String buildClassDeclaration(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(languageUtil.buildFileComment(modelClass, ".h"));
		builder.append(languageUtil.buildImports(modelClass));
		builder.append(languageUtil.buildClassDeclaration(modelClass));
		builder.append(languageUtil.buildPropertyDeclarations(modelClass));
		builder.append(languageUtil.buildModelUtilityDeclarationMethods(modelClass));
		builder.append(languageUtil.finishClass(modelClass));
		return builder.toString();
	}

	private String buildClassImplementation(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(languageUtil.buildFileComment(modelClass, ".m"));
		builder.append(languageUtil.buildSerializationConstants(modelClass));
		builder.append(languageUtil.buildClassImplementation(modelClass));
		builder.append(languageUtil.buildModelUtilityDefinitionMethods(modelClass));
		builder.append(languageUtil.finishClass(modelClass));
		
		return builder.toString();
	}

	class ObjectiveCLanguageUtility extends AbstractLanguageOrchestrator {
		@Override
		public String buildNamespace(ModelClass modelClass) {
			return "";
		}

		@Override
		public String buildClassDeclaration(ModelClass modelClass) {
			return "@class " + modelClass.getClassName() + " : NSObject\n";
		}

		@Override
		public String finishClass(ModelClass modelClass) {
			return "@end\t" + singleLineComment("End of " + modelClass.getClassName() + " Class", 0) + "\n\n";
		}

		@Override
		public String buildSinglePropertyDeclaration(ModelClassProperty property) {
			StringBuilder builder = new StringBuilder();
			String propertyType = getPropertyType(property);
			String comment = (property.getArray() ? "\t\t" + singleLineComment("Array of " + property.getArraySubType()) : "");
			builder.append("@property " + getPropertyDecorations(property) + propertyType + " " 
					+ (property.getPrimitive() ? "" : "*") + property.getName() + ";" + comment + "\n");
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
			
			//
					
			
			return builder.toString();
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
					System.out.println("No Subtype for: " + prop.getArraySubType());
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
	}
}
