package com.intere.generator.builder.orchestration.language.utility.objc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.models.ObjectiveCModelInterpreter;
import com.intere.generator.builder.orchestration.OrchestrationDataType;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility.CommentBuilder;
import com.intere.generator.builder.orchestration.language.utility.base.BaseTestBuilder;
import com.intere.generator.builder.orchestration.language.utility.comments.CStyleCommentBuilder;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

public class ObjectiveCTestBuilder extends BaseTestBuilder {
	private static final Logger LOGGER = LogManager.getLogger(ObjectiveCTestBuilder.class);
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
	public String finishClass(ModelClass modelClass) {
		return "@end\t" + singleLineComment("End of " + modelClass.getTestClassName() + " Class", 0) + "\n\n";
	}

	@Override
	public String buildNamespace(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildHeaderFileComment(ModelClass modelClass) {
		return buildFileComment(modelClass.getTestClassName() + ".h");
	}

	@Override
	public String buildImplementationFileComment(ModelClass modelClass) {
		return buildFileComment(modelClass.getTestClassName() + ".m");
	}

	@Override
	public String buildImports(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildSerializationConstants(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildClassImplementation(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildModelUtilityDeclarationMethods(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildModelUtilityDefinitionMethods(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildClassDeclaration(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
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
