package com.intere.generator.builder.orchestration.language.utility.swift;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility;
import com.intere.generator.builder.orchestration.language.utility.base.BaseTestBuilder;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by einternicola on 8/9/15.
 */
@Service(value="SwiftTestBuilder")
public class SwiftTestBuilder extends BaseTestBuilder {
    @Autowired @Qualifier("CStyle")
    protected LanguageUtility.CommentBuilder commentBuilder;
    @Autowired @Qualifier("SwiftInterpreter")
    protected JsonLanguageInterpreter interpreter;

    @Override
    public LanguageUtility.CommentBuilder getCommentBuilder() {
        return commentBuilder;
    }

    @Override
    public JsonLanguageInterpreter getInterpreter() {
        return interpreter;
    }

    @Override
    public String buildNamespace(ModelClass modelClass) {
        return modelClass.getNamespace() + "Test";
    }

    @Override
    public String buildHeaderFileComment(ModelClass modelClass) {
        return null;
    }

    @Override
    public String buildTestClassDeclaration(ModelClass modelClass) {
        StringBuilder builder = new StringBuilder();
        builder.append("public class    " + modelClass.getTestClassName() + " : XCTestCase {\n");
        return builder.toString();
    }

    @Override
    public String buildTestImports(ModelClass modelClass) {
        StringBuilder builder = new StringBuilder();
        builder.append("import XCTest\n");
        builder.append("import " + modelClass.getNamespace() + "\n\n");
        return builder.toString();
    }

    @Override
    public String buildTestSetupMethod(ModelClass modelClass) {
        return tabs(1) + "// TODO: Setup Method\n";
    }

    @Override
    public String buildTestMethods(ModelClass modelClass) {
        return tabs(1) + "// TODO: Test Methods\n";
    }

    @Override
    public String buildImplementationFileComment(ModelClass modelClass) {
        return buildFileComment(modelClass.getTestClassName() + ".swift");
    }

    @Override
    public String buildImports(ModelClass modelClass) {
        return null;
    }

    @Override
    public String buildSerializationConstants(ModelClass modelClass) {
        return null;
    }

    @Override
    public String buildClassImplementation(ModelClass modelClass) {
        return null;
    }

    @Override
    public String buildModelUtilityDeclarationMethods(ModelClass modelClass) {
        return null;
    }

    @Override
    public String buildViewUtilityDefinitionMethods(ModelClass modelClass) {
        return null;
    }

    @Override
    public String buildClassDeclaration(ModelClass modelClass) {
        return null;
    }

    @Override
    public String finishClass(ModelClass modelClass) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");
        builder.append("}\n");
        builder.append(singleLineComment("End of " + modelClass.getTestClassName() + " Class") + "\n");
        return builder.toString();
    }

    @Override
    public String buildSinglePropertyDeclaration(ModelClassProperty property) {
        return null;
    }

    @Override
    public String buildGetterAndSetter(ModelClassProperty prop) {
        return null;
    }

    @Override
    public String getPropertyType(ModelClassProperty property) {
        return null;
    }
}
