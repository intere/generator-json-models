package com.intere.generator.metadata.models;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.metadata.CustomClass;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

/**
 * Created by internicolae on 8/11/16.
 */
public class LanguageModelClassProperty extends ModelClassProperty {

    private JsonLanguageInterpreter interpreter;

    public LanguageModelClassProperty(ModelClassProperty original, JsonLanguageInterpreter interpreter) {
        this.interpreter = interpreter;
        setName(original.getName());
        setValue(original.getValue());
        setIsKey(original.getIsKey());
        setType(original.getType());
        setAlias(original.getAlias());
        setIsPrimitive(original.getIsPrimitive());
        setIsTransient(original.getIsTransient());
        setIsVisible(original.getIsVisible());
        setIsReadonly(original.getIsReadonly());
        setIsArray(original.getIsArray());
        setArraySubType(original.getArraySubType());
        setArraySubTypeProperty(original.getArraySubTypeProperty());
        setParentModel(original.getParentModel());
        setDataType(original.getDataType());
    }

    public String getMethodName() {
        return interpreter.buildClassName(getName());
    }

    public String getVariableName() {
        return interpreter.cleanVariableName(getName());
    }

    public String getRealmArrayType() {
        if(!getIsArray()) {
            return "nil";
        }

        switch(getArraySubTypeProperty().getDataType()) {
            case ARRAY:
                LanguageModelClassProperty prop = new LanguageModelClassProperty(getArraySubTypeProperty(), interpreter);
                return "List<" + prop.getRealmArrayType() + ">";

            case CLASS:
                CustomClass custom = (CustomClass)getArraySubTypeProperty().getParentModel();
                return "List<" + custom.getCustomName() + ">";

            case STRING:
            case TEXT:
                return "List<RealmString>";

            case DOUBLE:
                return "List<RealmDouble>";

            default:
                return "List<" + getArraySubType() + ">";
        }
    }

    public String getSwiftArrayType() {
        if(!getIsArray()) {
            return "nil";
        }

        switch(getArraySubTypeProperty().getDataType()) {
            case ARRAY:
                LanguageModelClassProperty prop = new LanguageModelClassProperty(getArraySubTypeProperty(), interpreter);
                return "[" + prop.getSwiftArrayType() + "]";

            case CLASS:
                CustomClass custom = (CustomClass)getArraySubTypeProperty().getParentModel();
                return "[" + custom.getClassName() + "]";

            default:
                return "[" + getArraySubType() + "]";
        }
    }

    public String getJavaArrayType() {
        if(!getIsArray()) {
            return "null";
        }

        switch(getArraySubTypeProperty().getDataType()) {
            case ARRAY:
                LanguageModelClassProperty prop = new LanguageModelClassProperty(getArraySubTypeProperty(), interpreter);
                return "<" + prop.getJavaArrayType() + ">";

            case CLASS:
                CustomClass custom = (CustomClass)getArraySubTypeProperty().getParentModel();
                return "<" + custom.getClassName() + ">";

            default:
                return "<" + getArraySubType() + ">";
        }
    }
}
