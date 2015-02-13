package com.intere.generator.builder.generation.services;

import com.intere.generator.builder.generation.utils.CodeGenerationUtils;
import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.services.ObjectiveCServiceInterpreter;
import com.intere.generator.deserializer.JsonDeserializer;

public class ObjectiveCServiceGeneration extends ServiceCodeGeneration {
	
	private static final JsonLanguageInterpreter INTERPRETER = new ObjectiveCServiceInterpreter();
	
	@Override
	public JsonLanguageInterpreter getInterpreter() {
		return INTERPRETER;
	}

	@Override
	public String generateHeaderFile(JsonDeserializer deserializer) {
		String modelClassName = deserializer.getName();
		String serviceClassName = INTERPRETER.buildServiceFilenameFromClassname(modelClassName);
		String listenerProtocolName = modelClassName + "Listener";
		StringBuilder builder = new StringBuilder();
		builder.append(generateHeaderCommentBlock(serviceClassName + ".h"));
		
		// Listener Protocol declaration
		builder.append("@protocol " + listenerProtocolName + ";\n\n");
		
		// Service Class declaration
		builder.append("@interface " + serviceClassName + " : NSObject\n");
		
		builder.append(comment("Gets you the most recent array of " + modelClassName + " objects") + "\n");
		builder.append("-(NSArray *)get" + modelClassName + ";\n\n");
		
		builder.append(comment("Lets you set the most recent array of " + modelClassName + " objects") + "\n");
		builder.append("-(void)set" + modelClassName + ":(NSArray *)arrayOf" + modelClassName + ";\n\n");
		
		builder.append(comment("Registers a " + listenerProtocolName + " for data updates") + "\n");
		builder.append("-(void)registerListener:(NSObject<" + listenerProtocolName + "> *)listener;\n\n");
		
		builder.append(comment("Unregisters a " + listenerProtocolName + " from data updates") + "\n");
		builder.append("-(void)unregisterListener:(NSObject<" + listenerProtocolName + "> *)listener;\n\n");
		
		builder.append(comment("Singleton reference accessor method.") + "\n");
		builder.append("+(" + serviceClassName + " *)getSharedInstance;\n\n");
						
		builder.append("@end\n\n\n");
		
		// Listener Protocol definition
		builder.append(comment("Interface that listeners will implement to be notified of changes to the data model") + "\n");
		builder.append("@protocol " + listenerProtocolName + "<NSObject>\n");
		builder.append(comment("Notifies listeners of updates to the data model") + "\n");
		builder.append("-(void)updated" + modelClassName + ":(NSArray *)arrayOf" + modelClassName + ");\n\n");
		builder.append("@end\n\n");
		
		return builder.toString();
	}

	@Override
	public String generateImplementationFile(JsonDeserializer deserializer) {
		
		String modelClassName = deserializer.getName();
		String modelArrayName = "array" + modelClassName;
		String arrayInstanceName = "arrayOf" + modelClassName;
		String serviceClassName = INTERPRETER.buildServiceFilenameFromClassname(modelClassName);
		String sharedServiceName = "shared" + serviceClassName;
		String listenerProtocolName = modelClassName + "Listener";
		StringBuilder builder = new StringBuilder();
		builder.append(generateHeaderCommentBlock(serviceClassName + ".m"));
		
		builder.append("#import \"" + serviceClassName + "\"\n\n");
		
		builder.append("@interface " + serviceClassName + " {\n");
		builder.append("\tNSArray *" + modelArrayName + ";\n" );
		builder.append("\tNSMutableArray *listeners;");
		builder.append("}\n");
		builder.append("-(void)notifyListenersDataChanged;\n");
		builder.append("@end\n\n");
		
		builder.append("static " + serviceClassName + " *" + sharedServiceName + ";\n\n");
		builder.append("@implementation " + serviceClassName + "\n");
		builder.append("-(id)init {\n");
		builder.append("\tself = [super init];\n");
		builder.append("\tif(self) {\n");
		builder.append("\t\tlisteners = [[NSMutableArray alloc]init];\n");
		builder.append("\t\t" + modelArrayName + " = [[NSArray alloc]init]\n");
		builder.append("\t}\n");
		builder.append("\treturn self;\n");
		builder.append("}\n\n");
		
		builder.append("-(NSArray *)get" + modelClassName + " {\n");
		builder.append("\treturn self->" + modelArrayName + ";\n");
		builder.append("}\n\n");
		
		builder.append("-(void)set" + modelClassName + ":(NSArray *)" + arrayInstanceName + " {\n");
		builder.append("\tif(" + arrayInstanceName + " && " + arrayInstanceName + ".count > 0) {\n" );
		builder.append("\t\tself->" + modelArrayName + " = " + arrayInstanceName + ";\n");
		builder.append("\t\t[self notifyListenersDataChanged];\n");
		builder.append("\t}\n");
		builder.append("}\n\n");
		
		builder.append("-(void)registerListener:(NSObject<" + listenerProtocolName + "> *)listener {\n");
		builder.append("\t[listeners addObject:listener];\n");
		builder.append("}\n\n");
		
		builder.append("-(void)unregisterListener:(NSObject<" + listenerProtocolName + "> *)listener {\n");
		builder.append("\t[listeners removeObject:listener];\n");
		builder.append("}\n\n");
		
		builder.append("#pragma mark Static Methods\n\n");
		builder.append("+(" + serviceClassName + " *)getSharedInstance {\n");
		builder.append("\tif(!" + sharedServiceName + ") {\n");
		builder.append("\t\t" + sharedServiceName + " = [[" + serviceClassName + " alloc]init];\n" );
		builder.append("\t}\n");
		builder.append("\treturn " + sharedServiceName + ";\n");
		builder.append("}\n\n");
		
		builder.append("#pragma mark Private Methods\n\n");
		builder.append("-(void)notifyListenersDataChanged {\n");
		builder.append("\tfor(NSObject<" + listenerProtocolName + "> *listener in listeners) {\n");
		builder.append("\t\t[listener updated" + modelClassName + ":self->" + modelArrayName + "];\n");
		builder.append("\t}\n");
		builder.append("}\n\n");
		
		builder.append("@end\n\n");
		return builder.toString();
	}

	@Override
	public String generateTestFile(JsonDeserializer deserializer, String jsonFilename, String testJsonFilename) {
		// TODO Auto-generated method stub
		return null;
	}	

	protected String generateHeaderCommentBlock(String filename) {
		return CodeGenerationUtils.generateCStyleHeaderBlock(filename);
	}
	
	protected String comment(String message) {
		return "/** " + message + " */";
	}
}
