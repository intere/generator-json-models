package com.intere.generator.builder.orchestration.language.utility.objc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.models.ObjectiveCModelInterpreter;
import com.intere.generator.builder.orchestration.OrchestrationDataType;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility.CommentBuilder;
import com.intere.generator.builder.orchestration.language.utility.base.BaseModelBuilder;
import com.intere.generator.builder.orchestration.language.utility.comments.CStyleCommentBuilder;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

@Service("ObjectiveCModelBuilder")
public class ObjectiveCModelBuilder extends BaseModelBuilder {
	private static final Logger LOGGER = LogManager.getLogger(ObjectiveCModelBuilder.class);
	@Autowired @Qualifier("CStyle")
	protected CommentBuilder commentBuilder;
	@Autowired @Qualifier("ObjectiveCInterpreter")
	protected JsonLanguageInterpreter interpreter;
	
	@Override
	public CommentBuilder getCommentBuilder() {
		return commentBuilder;
	}

	@Override
	public JsonLanguageInterpreter getInterpreter() {
		return interpreter;
	}

	@Override
	public String buildHeaderFileComment(ModelClass modelClass) {
		return buildFileComment(modelClass.getClassName() + ".h");
	}

	@Override
	public String buildImplementationFileComment(ModelClass modelClass) {
		return buildFileComment(modelClass.getClassName() + ".m");
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
	public String buildViewUtilityDefinitionMethods(ModelClass modelClass) {
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
	public String buildNamespace(ModelClass modelClass) {
		return "";
	}

	@Override
	public String buildClassDeclaration(ModelClass modelClass) {
		return "@interface " + modelClass.getClassName() + " : NSObject\n";
	}

	@Override
	public String finishClass(ModelClass modelClass) {
		return "@end\t" + singleLineComment("End of " + modelClass.getClassName() + " Class", 0) + "\n\n";
	}

	@Override
	public String buildSinglePropertyDeclaration(ModelClassProperty property) {
		StringBuilder builder = new StringBuilder();
		String propertyType = getPropertyType(property);
		String comment = (property.getIsArray() ? "\t\t" + singleLineComment("Array of " + property.getArraySubType()) : "");
		builder.append("@property " + getPropertyDecorations(property) + propertyType + " " 
				+ (property.getIsPrimitive() ? "" : "*") + property.getAlias() + ";" + comment + "\n");
		return builder.toString();
	}

	protected String getPropertyDecorations(ModelClassProperty property) {
		OrchestrationDataType dt = OrchestrationDataType.fromModelProperty(property);
		if(null != dt) {
			switch(dt) {
			case ARRAY:
			case CLASS:
			case DATE:
			case IMAGE:
			case STRING:
			case TEXT:
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
		builder.append("#import <Foundation/Foundation.h>\n");
		for(ModelClassProperty prop : modelClass.getProperty()) {
			switch(prop.getDataType()) {
			case CLASS:
				builder.append("#import \"" + prop.getType() + ".h\"\n");
				break;
			case ARRAY:
				switch(prop.getArraySubTypeProperty().getDataType()) {
				case CLASS:
					builder.append("#import \"" + prop.getArraySubType() + ".h\"\n");
					break;
				default:
					break;
				}
			default:
				break;
			}
		}
		builder.append("\n");
		
		return builder.toString();
	}
	
	
	private Object addDeserializeProperty(ModelClassProperty prop) {
		OrchestrationDataType dt = OrchestrationDataType.fromModelProperty(prop);
		final String BASE = tabs(1) + "object." + prop.getAlias() + " = ";
		final String propName = prop.getName();
		final String serConstant = interpreter.createSerializeConstantSymbolName(propName);
		final String subClassName = interpreter.buildSubClassName(prop.getParentModel().getClassName(), prop.getName());
		
		switch(dt) {
		case STRING:
		case TEXT:
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
					tabs(1) + BASE + "[" + subClassName + " fromDictionary:[dict objectForKey:" + serConstant + "]];\n" + 
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
		final String propName = prop.getAlias();
		final String serConstant = interpreter.createSerializeConstantSymbolName(prop.getName());
		
		switch(dt) {
		case CLASS:
			return BASE + "object:[self." + propName + " toDictionary] forKey:" + serConstant + "];\n";
			
		case STRING:
		case TEXT:
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
