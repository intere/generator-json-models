package com.intere.generator.builder.orchestration.language.utility.objc;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.models.ObjectiveCModelInterpreter;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility.CommentBuilder;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility.ServiceBuilder;
import com.intere.generator.builder.orchestration.language.utility.base.BaseModelBuilder;
import com.intere.generator.builder.orchestration.language.utility.comments.CStyleCommentBuilder;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

public class ObjectiveCServiceBuilder extends BaseModelBuilder implements ServiceBuilder {
	protected CommentBuilder commentBuilder = new CStyleCommentBuilder();
	protected JsonLanguageInterpreter interpreter = new ObjectiveCModelInterpreter();

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
	public String buildImports(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildSerializationConstants(ModelClass modelClass) {
		return null;
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
	public String buildModelUtilityDeclarationMethods(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(multiLineComment("Singleton Accessor method for " + modelClass.getServiceClassName() + " instance.") + "\n");
		builder.append("+(" + modelClass.getServiceClassName() + " *)getSharedInstance;\n");
		return builder.toString();
	}

	@Override
	public String buildModelUtilityDefinitionMethods(ModelClass modelClass) {
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

	@Override
	public String buildSinglePropertyDeclaration(ModelClassProperty property) {
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
		// TODO Auto-generated method stub
		return null;
	}
}
