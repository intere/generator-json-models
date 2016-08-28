package com.intere.generator.builder.generation.utils;

import com.intere.generator.metadata.ModelClassProperty;

/**
 * Created by internicolae on 8/27/16.
 */
public class JavaDataGenerator extends SwiftDataGenerator {

    @Override
    public String data(ModelClassProperty property) {
        switch(property.getDataType()) {
            case LONG:
                return super.data(property) + "L";
            default:
                return super.data(property);
        }
    }
}
