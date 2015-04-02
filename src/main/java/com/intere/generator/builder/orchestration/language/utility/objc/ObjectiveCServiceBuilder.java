package com.intere.generator.builder.orchestration.language.utility.objc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility.CommentBuilder;
import com.intere.generator.builder.orchestration.language.utility.base.BaseServiceBuilder;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

@Service(value="ObjectiveCServiceBuilder")
public class ObjectiveCServiceBuilder extends BaseServiceBuilder {
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
	public String buildNamespace(ModelClass modelClass) {
		return "";
	}

	@Override
	public String buildHeaderFileComment(ModelClass modelClass) {
		return buildFileComment(modelClass.getServiceClassName() + ".h");
	}

	@Override
	public String buildImplementationFileComment(ModelClass modelClass) {
		return buildFileComment(modelClass.getServiceClassName() + ".m");
	}

	@Override
	public String buildClassDeclaration(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("#import <Foundation/Foundation.h>\n\n");
		builder.append("@interface " + modelClass.getServiceClassName() + " : NSObject\n");
		return builder.toString();
	}
	
	@Override
	public String buildPropertyDeclarations(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("@property (nonatomic, strong) NSMutableArray *all" + modelClass.getClassName() + ";\n");
		builder.append("@property (nonatomic, strong) NSMutableDictionary *named" + modelClass.getClassName() + "Objects;\n");
		builder.append("@property (nonatomic, strong) NSMutableDictionary *named" + modelClass.getClassName() + "Lists;\n");
		return builder.toString();
	}

	@Override
	public String buildClassImplementation(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("#import \"" + modelClass.getServiceClassName() + ".h\"\n\n");
		builder.append("static " + modelClass.getServiceClassName() + " *shared" + modelClass.getServiceClassName() + ";\n\n");
		builder.append("@implementation " + modelClass.getServiceClassName() + "\n");
		builder.append("-(id)init {\n");
		builder.append(tabs(1) + "self = [super init];\n");
		builder.append(tabs(1) + "if(self) {\n");
		builder.append(tabs(2) + "self.all" + modelClass.getClassName() + " = [[NSMutableArray alloc]init];\n");
		builder.append(tabs(2) + "self.named" + modelClass.getClassName() + "Objects = [[NSMutableDictionary alloc]init];\n");
		builder.append(tabs(2) + "self.named" + modelClass.getClassName() + "Lists = [[NSMutableDictionary alloc]init];\n");
		builder.append(tabs(1) + "}\n");
		builder.append(tabs(1) + "return self;\n");
		builder.append("}\n\n");
		return builder.toString();
	}

	@Override
	public String buildUtilityDeclarationMethods(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(multiLineComment("Singleton Accessor method for " + modelClass.getServiceClassName() + " instance.") + "\n");
		builder.append("+(" + modelClass.getServiceClassName() + " *)getSharedInstance;\n");
		return builder.toString();
	}

	@Override
	public String buildUtilityDefinitionMethods(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append("+(" + modelClass.getServiceClassName() + " *)getSharedInstance {\n");
		builder.append(tabs(1) + "if(!shared" + modelClass.getServiceClassName() + ") {\n");
		builder.append(tabs(2) + "shared" + modelClass.getServiceClassName() + " = [[" + modelClass.getServiceClassName() + " alloc]init];\n");
		builder.append(tabs(1) + "}\n");
		builder.append(tabs(1) + "return shared" + modelClass.getServiceClassName() + ";\n");
		builder.append("}\n\n");
		
		return builder.toString();
	}

	@Override
	public String finishClass(ModelClass modelClass) {
		return "@end" + singleLineComment("End of " + modelClass.getServiceClassName() + " class", 3) + "\n\n";
	}
}
