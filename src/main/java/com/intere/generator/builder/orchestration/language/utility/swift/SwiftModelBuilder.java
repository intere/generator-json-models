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
    @Autowired @Qualifier("SwiftStyle")
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
        return "import Foundation\n\n";
    }

    @Override
    public String buildSerializationConstants(ModelClass modelClass) {
        StringBuilder builder = new StringBuilder();
        builder.append(tabs(1) + "//MARK:- Serialization Constants\n\n");

        for( ModelClassProperty prop : modelClass.getProperty()) {
            builder.append(buildSerializationPropertyDeclaration(prop));
        }

        builder.append("\n");
        return builder.toString();
    }

    @Override
    public String buildPropertyDeclarations(ModelClass modelClass) {
        return tabs(1) + singleLineComment("MARK:- Member Properties") + "\n\n"
                + super.buildPropertyDeclarations(modelClass);
    }

    @Override
    public String buildClassImplementation(ModelClass modelClass) {
        StringBuilder builder = new StringBuilder();
        builder.append("public class " + modelClass.getClassName() + " {\n");
        return builder.toString();
    }

    @Override
    public String buildModelUtilityDeclarationMethods(ModelClass modelClass) {
        StringBuilder builder = new StringBuilder();

        builder.append(tabs(1) + commentBuilder.singleLineComment("MARK:- Serialization Methods\n\n"));

        //
        // toMap Method
        //
        builder.append(commentBuilder.multiLineComment("Serializes this " + modelClass.getClassName()
                + " object into a Map\n- Returns: A Map (of String:AnyObject) which is the (map) serialized version of this " + modelClass.getClassName(), 1) + "\n");
        builder.append(tabs(1) + "public func toMap() -> [String:AnyObject] {\n");
        builder.append(tabs(2) + "var dict = [String:AnyObject]()\n\n");

        for(ModelClassProperty prop : modelClass.getProperty()) {
            if(!prop.getIsTransient()) {
                switch (prop.getDataType()) {
                    case CLASS:
                        builder.append(tabs(2) + "dict[" + prop.getParentModel().getClassName() + "." + firstLetterUppercase(prop.getAlias()) + "] = " + prop.getAlias() + "?.toMap() ?? [:]\n");
                        break;

//                    case ARRAY:
//                        // TODO: Handle Array
//                        builder.append(tabs(2) + commentBuilder.singleLineComment("TODO: to the appropriate array type for " + prop.getAlias()) + "\n");
//                        break;

//                    case DATE:
//                        // TODO: Handle Date
//                        builder.append(tabs(3) + commentBuilder.singleLineComment("TODO: Handle Dates"));
//                        break;

                    default:
//                        builder.append(tabs(3) + "nullableMap[\"" + prop.getName() + "\"] = self." + prop.getAlias() + "\n");
                        builder.append(tabs(2) + "dict[" +  modelClass.getClassName() + "." + firstLetterUppercase(prop.getAlias()) + "] = " + prop.getAlias() + "\n");
                        break;
                }
            }
        }
        builder.append("\n");
        builder.append(tabs(2) + "return dict\n");
        builder.append(tabs(1) + "}\n\n");

        //
        // toMapArray Method
        //
        builder.append(multiLineComment("Converts the provided Array of " + modelClass.getClassName() + " Objects to an Array of Dictionaries.\n"
                + "- Parameter models: An Array of " + modelClass.getClass() + " Objects to be converted to an Array of Dictionaries.\n"
                + "- Returns: An array of Dictionaries.", 1) + "\n");
        builder.append(tabs(1) + "public class func toMapArray(models: [" + modelClass.getClassName() + "]) -> [[String:AnyObject?]] {\n");
        builder.append(tabs(2) + "var maps = [[String:AnyObject?]]()\n\n");
        builder.append(tabs(2) + "for model in models {\n");
        builder.append(tabs(3) + "maps.append(model.toMap())\n");
        builder.append(tabs(2) + "}\n\n");
        builder.append(tabs(2) + "return maps\n");
        builder.append(tabs(1) + "}\n\n");

        //
        // toJsonData Method
        //
        builder.append(multiLineComment("Converts this model to JSON Data.\n-Returns: Optional JSON Data (returns nil if there's a problem serializing the map).", 1) + "\n");
        builder.append(tabs(1) + "public func toJsonData() -> NSData? {\n");
        builder.append(tabs(2) + "let dict = toMap()\n");
        builder.append(tabs(2) + "do {\n");
        builder.append(tabs(3) + "return try NSJSONSerialization.dataWithJSONObject(dict, options: .PrettyPrinted)\n");
        builder.append(tabs(2) + "} catch {\n");
        builder.append(tabs(3) + "print(\"Error serializing this model to JSON: \\(dict)\")\n");
        builder.append(tabs(2) + "}\n\n");
        builder.append(tabs(2) + "return nil\n");
        builder.append(tabs(1) + "}\n\n");

        //
        // static toJsonData Method
        //
        builder.append(multiLineComment("Converts the provided array of models to JSON Data.\n"
                + "- Parameter array: Array of " + modelClass.getClassName() + " objects to be serialized.\n"
                + "- Returns: NSData that contains the serialized objects, or nil if there's a problem serializing them.", 1) + "\n");
        builder.append(tabs(1) + "public class func toJsonData(array: [" + modelClass.getClassName() + "]) -> NSData? {\n");
        builder.append(tabs(2) + "let mapArray = toMapArray(array) as! AnyObject\n");
        builder.append(tabs(2) + "do {\n");
        builder.append(tabs(3) + "return try NSJSONSerialization.dataWithJSONObject(mapArray, options: .PrettyPrinted)\n");
        builder.append(tabs(2) + "} catch {\n");
        builder.append(tabs(3) + "print(\"Error serializing array of models to JSON: \\(mapArray)\")");
        builder.append(tabs(2) + "}\n");
        builder.append(tabs(2) + "return nil\n");
        builder.append(tabs(1) + "}\n\n");

        //
        // toJsonString Method
        //
        builder.append(multiLineComment("Converts this model to a JSON String.\n"
                + "- Returns: Optional String (nil if there's a problem serializing the map to Data or converting it to a UTF-8 String).", 1) + "\n");
        builder.append(tabs(1) + "public func toJsonString() -> String? {\n");
        builder.append(tabs(2) + "if let data = toJsonData() {\n");
        builder.append(tabs(3) + "return NSString(data: data, encoding: NSUTF8StringEncoding) as? String\n");
        builder.append(tabs(2) + "}\n");
        builder.append(tabs(2) + "return nil\n");
        builder.append(tabs(1) + "}\n\n");

        //
        // static toJsonString method
        //
        builder.append(multiLineComment("Converts the provided array of models to a JSON String.\n"
                + "- Parameter array: Array of " + modelClass.getClassName() + " objects to be serialized\n"
                + "- Returns: Optional String (nil if there's a problem serializing to NSData or converting to a UTF-8 String).", 1) + "\n");
        builder.append(tabs(1) + "public class func toJsonString(array: [" + modelClass.getClassName() + "]) -> String? {\n");
        builder.append(tabs(2) + "if let data = toJsonData(array) {\n");
        builder.append(tabs(3) + "return NSString(data: data, encoding: NSUTF8StringEncoding) as? String\n");
        builder.append(tabs(2) + "}\n");
        builder.append(tabs(2) + "return nil\n");
        builder.append(tabs(1) + "}\n\n");

        builder.append(tabs(1) + singleLineComment("MARK:- De-Serialization Methods\n\n"));

        //
        // fromMap Method
        //
        builder.append(multiLineComment("Gets you a " + modelClass.getClassName() + " object from the provided map\n"
                + "- Parameter map: The Map to read the data from.\n"
                + "- Returns: A " + modelClass.getClassName() + " object (assuming the map you provided is non-nil)", 1) + "\n");
        builder.append(tabs(1) + "public class func fromMap(map: [String:AnyObject]?) -> " + modelClass.getClassName() + "? {\n");
        builder.append(tabs(2) + "if let map = map {\n");
        builder.append(tabs(3) + "let model = " + modelClass.getClassName() + "()\n\n");

        for(ModelClassProperty prop : modelClass.getProperty()) {
            if(!prop.getIsTransient()) {
                String propertyType = getPropertyType(prop);
                if (OrchestrationDataType.ARRAY == OrchestrationDataType.fromModelProperty(prop)) {
                    propertyType = getArrayType(prop);
                }
                switch(prop.getDataType()) {

                    case ARRAY:
                        switch(prop.getArraySubTypeProperty().getDataType()) {
                            case CLASS:
                                String classType = prop.getArraySubType();
                                builder.append(tabs(3) + "model." + prop.getAlias() + " = " + classType
                                        + ".fromArrayOfMaps(map[" + modelClass.getClassName() + "." + firstLetterUppercase(prop.getAlias())
                                        + "] as? [[String:AnyObject]])\n");
                                break;
                            default:
                                builder.append(tabs(3) + "model." + prop.getAlias() + " = map[" + modelClass.getClassName() + "." + firstLetterUppercase(prop.getAlias())
                                        + "] as? " + getArrayType(prop,1) + " ?? Default" + firstLetterUppercase(prop.getAlias()) + "\n");
                                break;
                        }
                        break;

                    case DATE:
                        // fromIsoDate
                        builder.append(tabs(3) + "model." + prop.getAlias() + " = fromIsoDate(map[" + modelClass.getClassName() + "." + firstLetterUppercase(prop.getAlias())
                                + "] as? Int)\n");
                        break;

                    default:
                        builder.append(tabs(3) + "model." + prop.getAlias() + " = map[" + modelClass.getClassName() + "." + firstLetterUppercase(prop.getAlias())
                                + "] as? " + propertyType + " ?? Default" + firstLetterUppercase(prop.getAlias()) + "\n");
                        break;
                }
            }
        }
        builder.append(tabs(3) + "return model\n");
        builder.append(tabs(2) + "}\n");
        builder.append(tabs(2) + "return nil\n");
        builder.append(tabs(1) + "}\n\n");

        //
        // fromArrayOfMaps
        //
        builder.append(multiLineComment("Creates an Array of " + modelClass.getClassName() + " objects for the provided Array of Maps.\n"
                + "- Parameter mapArray: An array of Maps, assumes that each map is a serialized " + modelClass.getClassName() + ".\n"
                + "- Returns: An array of " + modelClass.getClassName() + " objects (should be 1 to 1).", 1) + "\n");
        builder.append(tabs(1) + "public class func fromArrayOfMaps(mapArray: [[String:AnyObject]]?) -> [" + modelClass.getClassName() + "] {\n");
        builder.append(tabs(2) + "var modelArray = [" + modelClass.getClassName() + "]()\n\n");
        builder.append(tabs(2) + "if let mapArray = mapArray {\n");
        builder.append(tabs(3) + "for map in mapArray {\n");
        builder.append(tabs(4) + "if let model = fromMap(map) {\n");
        builder.append(tabs(5) + "modelArray.append(model)\n");
        builder.append(tabs(4) + "}\n");
        builder.append(tabs(3) + "}\n");
        builder.append(tabs(2) + "}\n\n");
        builder.append(tabs(2) + "return modelArray\n");
        builder.append(tabs(1) + "}\n\n");

        //
        // fromData method
        //
        builder.append(multiLineComment("Gives you back a collection of " + modelClass.getClassName()
                + " objects for the provided JSON Serialized Data.  This method handles both a single object (map) and an array of objects (array of maps).\n"
                + "- Parameter data: The NSData (optional) to pull the objects from."
                + "- Returns: An array of " + modelClass.getClassName() + " objects that were deserialized from the provided data.", 1) + "\n");
        builder.append(tabs(1) + "public class func fromData(data: NSData?) -> [" + modelClass.getClassName() + "] {\n");
        builder.append(tabs(2) + "if let data = data {\n");
        builder.append(tabs(3) + "do {\n");
        builder.append(tabs(4) + "let jsonData = try NSJSONSerialization.JSONObjectWithData(data, options: .AllowFragments)\n\n");
        builder.append(tabs(4) + singleLineComment("If we have an array of dictionaries:") + "\n");
        builder.append(tabs(4) + "if let jsonArray = jsonData as? [[String:AnyObject]] {\n");
        builder.append(tabs(5) + "return fromArrayOfMaps(jsonArray)\n");
        builder.append(tabs(4) + "} else if let jsonMap = jsonData as? [String:AnyObject] {\n");
        builder.append(tabs(5) + "if let model = fromMap(jsonMap) {\n");
        builder.append(tabs(6) + "return [model]\n");
        builder.append(tabs(5) + "}\n");
        builder.append(tabs(4) + "} else {\n");
        builder.append(tabs(5) + "print(\"Unknown JSON Data type: \\(jsonData)\")\n");
        builder.append(tabs(4) + "}\n");
        builder.append(tabs(3) + "} catch {\n");
        builder.append(tabs(4) + "print(\"Error deserializing json data\")\n");
        builder.append(tabs(3) + "}\n");
        builder.append(tabs(2) + "}\n");
        builder.append(tabs(2) + "return []\n");
        builder.append(tabs(1) + "}\n\n");

        //
        // fromJson Method
        //
        builder.append(multiLineComment("Gives you back a collection of " + modelClass.getClassName() + " objects after deserializing them from a "
                + "String to Data, Data to a Dictionary and then finally to a Model Object.\n"
                + "- Parameter json: The (UTF-8) JSON String to deserialize into " + modelClass.getClassName() + " objects.\n"
                + "- Returns: An Array of " + modelClass.getClassName() + " objects.", 1) + "\n");
        builder.append(tabs(1) + "public class func fromJson(json: String) -> [" + modelClass.getClassName() + "] {\n");
        builder.append(tabs(2) + "let data = (json as NSString).dataUsingEncoding(NSUTF8StringEncoding)\n");
        builder.append(tabs(2) + "return fromData(data)\n");
        builder.append(tabs(1) + "}\n\n");


        builder.append("// MARK: - Utility Methods\n\n");

        builder.append(tabs(1) + "public class func fromIsoDate(dateInt: Int?) -> NSDate? {\n");
        builder.append(tabs(2) + "if let dateInt = dateInt {\n");
        builder.append(tabs(3) + "let dateDouble = NSTimeInterval(Double(dateInt) / 1000)\n");
        builder.append(tabs(3) + "return NSDate(timeIntervalSince1970: dateDouble)\n");
        builder.append(tabs(2) + "}\n");
        builder.append(tabs(2) + "return nil\n");
        builder.append(tabs(1) + "}\n\n");

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

    public String buildSerializationPropertyDeclaration(ModelClassProperty property) {
        StringBuilder builder = new StringBuilder();

        // Build the Serialization Property Declaration
        if(!property.getIsTransient()) {
            builder.append(tabs(1) + "public static let " + firstLetterUppercase(property.getAlias()) + " = \"" + property.getName() + "\"\n");
        }

        // Build the Default Value
        String propertyType = getPropertyType(property);
        if(OrchestrationDataType.ARRAY == OrchestrationDataType.fromModelProperty(property)) {
            propertyType = getArrayType(property);
        }

        switch(property.getDataType()) {
            case DATE:
            case CLASS:
                builder.append(tabs(1) + "public static var Default" + firstLetterUppercase(property.getAlias()) + ": "
                        + propertyType + "?" + getDefaultValue(property) + "\n");
                break;


            default:
                builder.append(tabs(1) + "public static let Default" + firstLetterUppercase(property.getAlias()) + ": "
                        + propertyType + getDefaultValue(property) + "\n");
        }

        return builder.toString();
    }

    protected String getDefaultValue(ModelClassProperty property) {

        switch(property.getDataType()) {
            case STRING:
            case TEXT:
            case IMAGE:
                return " = \"\"";

            case BOOLEAN:
                return " = false";

            case LONG:
                return " = 0";

            case DOUBLE:
                return " = 0.0";


            default:
                return " = nil";
        }
//        StringBuilder builder = new StringBuilder(" = ");
//
//        switch(property.getDataType()) {
//            case ARRAY:
//                builder.append("[]");
//                break;
//
//            case BOOLEAN:
//                builder.append("false");
//                break;
//
//            case CLASS:
//                builder.append(property.getType() + "()");
//                break;
//
//            case DATE:
//                builder = new StringBuilder("{ return NSDate() }");
//                break;
//
//            case DOUBLE:
//                builder.append("0.0");
//                break;
//
//            case LONG:
//                builder.append("0");
//                break;
//
//            case STRING:
//            case TEXT:
//            case IMAGE:
//                builder.append("\"\"");
//                break;
//
//            default:
//                builder.append("\"\"");
//                break;
//        }
//
//        return builder.toString();
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

    protected String firstLetterUppercase(String name) {
        String fixedName = name;
        if (null != name) {
            if (fixedName.length() > 1) {
                fixedName = name.substring(0, 1).toUpperCase() + name.substring(1);
            } else {
                fixedName = fixedName.toUpperCase();
            }
        }

        return fixedName;
    }

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
