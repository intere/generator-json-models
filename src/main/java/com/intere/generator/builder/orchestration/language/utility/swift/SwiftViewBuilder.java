package com.intere.generator.builder.orchestration.language.utility.swift;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility;
import com.intere.generator.builder.orchestration.language.utility.base.BaseViewBuilder;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by einternicola on 8/9/15.
 */
@Service(value="SwiftViewBuilder")
public class SwiftViewBuilder extends BaseViewBuilder {
    @Autowired @Qualifier("CStyle")
    protected LanguageUtility.CommentBuilder commentBuilder;
    @Autowired @Qualifier("SwiftInterpreter")
    protected JsonLanguageInterpreter interpreter;

    @Override
    public LanguageUtility.CommentBuilder getCommentBuilder() {
        return null;
    }

    @Override
    public JsonLanguageInterpreter getInterpreter() {
        return null;
    }

    @Override
    public String buildNamespace(ModelClass modelClass) {
        return null;
    }

    @Override
    public String buildHeaderFileComment(ModelClass modelClass) {
        return null;
    }

    @Override
    public String buildImplementationFileComment(ModelClass modelClass) {
        return null;
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
        return null;
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
