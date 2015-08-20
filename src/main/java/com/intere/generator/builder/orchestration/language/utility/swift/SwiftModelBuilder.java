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
        return "import Foundation\n";
    }

    @Override
    public String buildSerializationConstants(ModelClass modelClass) {
        throw new IllegalArgumentException("No need to call this for swift");
    }

    @Override
    public String buildClassImplementation(ModelClass modelClass) {
        StringBuilder builder = new StringBuilder();
        builder.append("public class " + modelClass.getClassName() + " : NSObject {\n");
        return builder.toString();
    }

    @Override
    public String buildModelUtilityDeclarationMethods(ModelClass modelClass) {
        StringBuilder builder = new StringBuilder();
        builder.append(commentBuilder.multiLineComment("This method converts this " + modelClass.getClassName()
                + " to a Map for serialization", 1) + "\n");
        builder.append(tabs(1) + "public func toMap() -> [String:AnyObject] {\n");
        builder.append(tabs(2) + "var nullableMap:[String:AnyObject?] = [:]\n");
        for(ModelClassProperty prop : modelClass.getProperty()) {
            if(!prop.getIsTransient()) {
                switch (prop.getDataType()) {
                    case CLASS:
                        builder.append(tabs(2) + "nullableMap[\"" + prop.getName() + "\"] = (nil != self." + prop.getAlias() + ") ? self." + prop.getAlias() + "!.toMap() : nil\n");
                        break;

                    case ARRAY:
                        // TODO: Handle Array
                        builder.append(tabs(2) + commentBuilder.singleLineComment("TODO: to the appropriate array type for " + prop.getType()) + "\n");
                        break;

                    case DATE:
                        // TODO: Handle Date
                        break;

                    default:
                        builder.append(tabs(2) + "nullableMap[\"" + prop.getName() + "\"] = self." + prop.getAlias() + "\n");
                        break;
                }
            }
        }
        builder.append(singleLineComment("Produce a Non-Null Map to return", 2) + "\n");
        builder.append(tabs(2) + "var nonNilMap: [String:AnyObject] = [:]\n");
        builder.append(tabs(2) + "for(key, value) in nullableMap {\n");
        builder.append(tabs(3) +"if nil != value {\n");
        builder.append(tabs(4) + "nonNilMap[key] = value\n");
        builder.append(tabs(3) + "}\n");
        builder.append(tabs(2) + "}\n");
        builder.append(tabs(2) + "return nonNilMap\n");
        builder.append(tabs(1) + "}\n\n");

        // TODO: fromMap Method

        return builder.toString();
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
        String propertyType = getPropertyType(property) + "?";
        if(OrchestrationDataType.ARRAY == OrchestrationDataType.fromModelProperty(property)) {
            propertyType = getArrayType(property);
        }
        builder.append(tabs(1) + "public var " + property.getAlias() + ": " + propertyType + "\n");
        return builder.toString();
    }

    @Override
    public String buildGetterAndSetter(ModelClassProperty prop) {
        throw new IllegalArgumentException("No need to call this for swift");
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

    //
    // Helper Methods
    //

    /**
     * Gets you the deeply nested array type (think: Array of Array of Array of Double).
     * @param property
     * @return
     */
    protected String getArrayType(ModelClassProperty property) {
        return getArrayType(property, 0);
    }

    /**
     * Helper Method that performs the work to build the deeply nested array.
     * @param property
     * @param level
     * @return
     */
    protected String getArrayType(ModelClassProperty property, int level) {
        String result = property.getType();

        if(OrchestrationDataType.ARRAY == property.getArraySubTypeProperty().getDataType()) {
            result += "<" + getArrayType(property.getArraySubTypeProperty(), level + 1) + ">";
        } else {
            result += "<" + property.getArraySubType() + ">";
        }

        if(level == 0) {
            result += "?";
        }

        return result;
    }

}
