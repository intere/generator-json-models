package com.intere.generator.builder.generation.utils;

import com.intere.generator.metadata.ModelClassProperty;

/**
 * Created by internicolae on 8/12/16.
 */
interface DataGenerator {

    /**
     * Generates array data for you based on the data type.  Note: implementations of this are language specific.
     * @param property The property to generate the data for.
     * @param length The number of items to generate.
     * @return a String that contains the array data.
     */
    String arrayData(ModelClassProperty property, Integer length);

    /**
     * Generates data based on the data type.  Note: implementations of this are language specific.
     * @param property The property to generate the data for.
     * @return A String that contains the data.
     */
    String data(ModelClassProperty property);
}
