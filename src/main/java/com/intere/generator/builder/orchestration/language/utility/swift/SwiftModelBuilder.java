package com.intere.generator.builder.orchestration.language.utility.swift;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.orchestration.OrchestrationDataType;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility;
import com.intere.generator.builder.orchestration.language.utility.base.BaseModelBuilder;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by einternicola on 8/9/15.
 */
@Service(value="SwiftModelBuilder")
public class SwiftModelBuilder extends BaseModelBuilder {
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
        return "";
    }

    @Override
    public String buildHeaderFileComment(ModelClass modelClass) {
        return null;
    }

    @Override
    public String buildImplementationFileComment(ModelClass modelClass) {
        return buildFileComment(modelClass.getClassName() + ".swift");
    }

    @Override
    public String buildImports(ModelClass modelClass) {
        return "";
    }

    @Override
    public String buildSerializationConstants(ModelClass modelClass) {
        StringBuilder builder = new StringBuilder();
        builder.append(tabs(1) + commentBuilder.singleLineComment("") + "\n");
        builder.append(tabs(1) + commentBuilder.singleLineComment("Serialization Values: ") + "\n");
        builder.append(tabs(1) + commentBuilder.singleLineComment("") + "\n");

        for(ModelClassProperty prop : modelClass.getProperty()) {
            builder.append(tabs(1) + "static let " + interpreter.createSerializeConstantSymbolName(prop.getName())
                    + ": String = \"" + prop.getName() + "\"\n");
        }

        return builder.toString();
    }

    @Override
    public String buildClassImplementation(ModelClass modelClass) {
        StringBuilder builder = new StringBuilder();
        builder.append("public class " + modelClass.getClassName() + " : NSObject {\n");
        return builder.toString();
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
        builder.append(singleLineComment("End of " + modelClass.getClassName() + " Class") + "\n");
        return builder.toString();
    }

    @Override
    public String buildSinglePropertyDeclaration(ModelClassProperty property) {
        StringBuilder builder = new StringBuilder();
        String propertyType = getPropertyType(property);
        String comment = (property.getIsArray() ? tabs(2) + singleLineComment("Array of " + property.getArraySubType()) : "");
        builder.append("var " + property.getAlias() + ": " + propertyType + comment + "\n");
        return builder.toString();
    }

    @Override
    public String buildGetterAndSetter(ModelClassProperty prop) {
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
                return type.getSwiftName();
        }
    }

}
