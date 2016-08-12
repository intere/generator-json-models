package com.intere.generator.builder.generation.utils;

import com.intere.generator.metadata.ModelClassProperty;

import java.util.Date;

/**
 * Created by internicolae on 8/12/16.
 */
public class SwiftDataGenerator implements DataGenerator {

    @Override
    public String arrayData(ModelClassProperty property, Integer length) {
        if(!property.getIsArray()) {
            throw new IllegalArgumentException("Invalid property type, it must be an array");
        }

        switch(property.getArraySubTypeProperty().getDataType()) {
            case CLASS:
                return generateClasses(length);

            case ARRAY:
                return generateArray(property, length);

            case BOOLEAN:
                return generateBooleans(length);

            case DATE:
                return generateDates(length);

            case DOUBLE:
                return generateDoubles(length);

            case IMAGE:
                return generateImage(length);

            case LONG:
                return generateLongs(length);

            case STRING:
            case TEXT:
                return generateStrings(length);

            default:
                throw new IllegalArgumentException("Invalid property type: " + property.getArraySubTypeProperty().getDataType().name());
        }

    }

    @Override
    public String data(ModelClassProperty property) {

        switch(property.getDataType()) {
            case CLASS:
                return "[:]";

            case BOOLEAN:
                return ((int)(Math.random() * 100)) % 2 == 0 ? "true" : "false";

            case DATE:
                return String.valueOf(new Date().getTime());

            case DOUBLE:
                return String.valueOf(Math.random() * Double.MAX_VALUE);

            case IMAGE:
                return "\"" + randomImage() + "\"";

            case LONG:
                return String.valueOf((long)(Math.random() * Long.MAX_VALUE));

            case STRING:
            case TEXT:
                return "\"" + randomString() + "\"";

            default:
                throw new IllegalArgumentException("Invalid property type: " + property.getDataType().name());
        }
    }

    //
    // Helpers
    //

    protected String generateStrings(Integer length) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for(int i=0; i<length; i++) {
            if(i!=0) {
                builder.append(", ");
            }
            builder.append("\"" + randomString() + "\"");
        }

        builder.append("]");

        return builder.toString();
    }

    protected String generateLongs(Integer length) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for(int i=0; i<length; i++) {
            if(i!=0) {
                builder.append(", ");
            }
            Long randomLong = (long)(Math.random() * Long.MAX_VALUE);
            builder.append(String.valueOf(randomLong));
        }

        builder.append("]");

        return builder.toString();
    }

    protected String generateImage(Integer length) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for(int i=0; i<length; i++) {
            if(i!=0) {
                builder.append(", ");
            }
            builder.append("\"" + randomImage() + "\"");
        }

        builder.append("]");

        return builder.toString();
    }

    protected String generateDoubles(Integer length) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for(int i=0; i<length; i++) {
            if(i!=0) {
                builder.append(", ");
            }
            builder.append(String.valueOf(Math.random() * Double.MAX_VALUE));
        }

        builder.append("]");

        return builder.toString();
    }

    protected String generateDates(Integer length) {
        StringBuilder builder = new StringBuilder();

        builder.append("[");

        Date startDate = new Date();
        for(int i=0; i<length; i++) {
            if(i!=0) {
                builder.append(", ");
            }
            builder.append(String.valueOf(startDate.getTime() + 1000 * 60 * 60 * 24));
        }

        builder.append("]");

        return builder.toString();
    }

    protected String generateBooleans(Integer length) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for(int i=0; i<length; i++) {
            if(i!=0) {
                builder.append(", ");
            }
            builder.append(i%2==0 ? "true" : "false");
        }
        builder.append("]");

        return builder.toString();
    }

    protected String generateArray(ModelClassProperty property, Integer length) {

        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for(int i=0; i<length; i++) {
            if(i!=0) {
                builder.append(", ");
            }
            builder.append(arrayData(property.getArraySubTypeProperty(), length));
        }

        builder.append("]");

        return builder.toString();
    }

    protected String generateClasses(Integer length) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for(int i=0; i<length; i++) {
            if(i!=0) {
                builder.append(", ");
            }
            builder.append("[:]");
        }
        builder.append("]");

        return builder.toString();
    }

    // Random Data

    protected String randomImage() {
        String images[] = {
            "http://placekitten.com/200/300",
            "https://www.placecage.com/c/200/200",
            "https://unsplash.it/200/200",
            "http://lorempixel.com/200/200/"
        };
        int index = (int)(Math.random() * images.length);
        return images[index];
    }

    protected String randomString() {
        String strings[] = {
            "It's only after we've lost everything that we're free to do anything.",
                "I don't want to die without any scars.",
                "You know how they say you only hurt the ones you love? Well, it works both ways.",
                "The things you used to own, now they own you.",
                "At the time, my life just seemed too complete, and maybe we have to break everything to make something better out of ourselves.",
                "We've all been raised on television to believe that one day we'd all be millionaires, and movie gods, and rock stars. But we won't. And we're slowly learning that fact. And we're very, very pissed off.",
                "I let go. Lost in oblivion. Dark and silent and complete. I found freedom. Losing all hope was freedom.",
                "I look at God behind his desk, taking notes on a pad, but God’s got this all wrong.",
                "Yeah. Well. Whatever. You can’t teach God anything.",
                "I wanted to destroy everything beautiful I'd never have."
        };

        int index = (int)(Math.random() * strings.length);
        return strings[index];
    }

}
