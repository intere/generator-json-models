package com.intere.generator.builder.orchestration.language.utility.java;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.models.JavaModelInterpreter;
import com.intere.generator.builder.orchestration.OrchestrationDataType;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility.CommentBuilder;
import com.intere.generator.builder.orchestration.language.utility.base.BaseModelBuilder;
import com.intere.generator.builder.orchestration.language.utility.comments.CStyleCommentBuilder;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassImports;
import com.intere.generator.metadata.ModelClassProperty;

/**
 * This is the Model Builder for Java.
 * @author einternicola
 */
@Service(value="JavaModelBuilder")
public class JavaModelBuilder extends BaseModelBuilder {
	@Autowired @Qualifier("CStyle")
	protected CommentBuilder commentBuilder;
	@Autowired @Qualifier("JavaInterpreter")
	protected JsonLanguageInterpreter interpreter;
	
	@Override
	public CommentBuilder getCommentBuilder() {
		return commentBuilder;
	}

	@Override
	public JsonLanguageInterpreter getInterpreter() {
		return interpreter;
	}
	
	@Override
	public String buildNamespace(ModelClass modelClass) {
		if(null != modelClass.getNamespace()) {
			return "package " + modelClass.getNamespace() + ";\n\n";
		}
		return "";
	}
	
	@Override
	public String buildSerializationConstants(ModelClass modelClass) {
		// Not Used for this language
		return null;
	}	
	
	@Override
	public String buildImplementationFileComment(ModelClass modelClass) {
		return buildFileComment(modelClass.getClassName() + ".java");
	}
	
	@Override
	public String buildHeaderFileComment(ModelClass modelClass) {
		// not used for java
		return null;
	}

	@Override
	public String buildImports(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		Map<String, String> imports = new HashMap<>();
		imports.put("Serializable", "java.io.Serializable");
		
		for(ModelClassProperty prop : modelClass.getProperty()) {
			OrchestrationDataType type = OrchestrationDataType.fromModelProperty(prop);
			if(OrchestrationDataType.DATE == type) {
				imports.put("Date", "java.util.Date");
			} else if(OrchestrationDataType.ARRAY == type) {
				imports.put("List", "java.util.List");
				imports.put("ArrayList", "java.util.ArrayList");
			}
		}
		
		if(hasTransientProperties(modelClass)) {
			imports.put("JsonIgnore", "org.codehaus.jackson.annotate.JsonIgnore");
		}
		for(ModelClassImports theImport : modelClass.getImports()) {
			imports.put(theImport.getImportName(), theImport.getImportName());
		}
		
		for(String key : imports.keySet()) {
			builder.append("import " + imports.get(key) + ";\n");
		}
		
		builder.append("\n");
		
		return builder.toString();
	}

	@Override
	public String buildClassImplementation(ModelClass modelClass) {
		// Not Used for this language
		return null;
	}

	@Override
	public String buildModelUtilityDeclarationMethods(ModelClass modelClass) {
		// Not currently used
		return null;
	}
	
	@Override
	public String buildViewUtilityDefinitionMethods(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		
		// hashCode method
		builder.append(tabs(1) + "@Override\n");
		builder.append(tabs(1) + "public int hashCode() {\n");
		builder.append(tabs(2) + "final int prime = 31;\n");
		builder.append(tabs(2) + "int result = 1;\n");
		for(ModelClassProperty prop : modelClass.getProperty()) {
			builder.append(tabs(2) + "result = prime * result + ((" 
					+ prop.getName() + " == null) ? 0 : " + prop.getName() + ".hashCode());\n");
		}
		builder.append(tabs(2) + "return result;\n");
		builder.append(tabs(1) + "}\n\n");
		
		// equals method
		builder.append(tabs(1) + "@Override\n");
		builder.append(tabs(1) + "public boolean equals(Object obj) {\n");
		builder.append(tabs(2) + "if (this == obj)\n");
		builder.append(tabs(3) + "return true;\n");
		builder.append(tabs(2) + "if (obj == null)\n");
		builder.append(tabs(3) + "return false;\n");
		builder.append(tabs(2) + "if (getClass() != obj.getClass())\n");
		builder.append(tabs(3) + "return false;\n");
		builder.append(tabs(2) + modelClass.getClassName() + " other = (" + modelClass.getClassName() + ")obj;\n");
		for(ModelClassProperty prop : modelClass.getProperty()) {
			builder.append(tabs(2) + "if(" + prop.getName() + " == null) {\n");
			builder.append(tabs(3) + "if(other." + prop.getName() + " != null)\n");
			builder.append(tabs(4) + "return false;\n");
			builder.append(tabs(2) + "} else if(!" + prop.getName() + ".equals(other." + prop.getName() + "))\n");
			builder.append(tabs(3) + "return false;\n");
		}
		
		builder.append(tabs(2) + "return true;\n");
		builder.append(tabs(1) + "}\n\n");		
		return builder.toString();
	}
	
	@Override
	public String buildClassDeclaration(ModelClass modelClass) {
		return  "@SuppressWarnings(\"serial\")\n" + 
				"public class " + modelClass.getClassName() + " implements Serializable {\n";
	}

	@Override
	public String finishClass(ModelClass modelClass) {
		return "}\t" + singleLineComment("end " + modelClass.getClassName()) + "\n\n";
	}

	@Override
	public String buildSinglePropertyDeclaration(ModelClassProperty property) {
		StringBuilder builder = new StringBuilder();
		String propertyType = getPropertyType(property);
		if(property.getIsTransient()) {
			builder.append(tabs(1) + "@JsonIgnore\n");
		}
		builder.append(tabs(1) + "private " + propertyType + " " + property.getName());
		if(null != property.getInitializer()) {
			builder.append(" = " + property.getInitializer());
		}
		builder.append(";" + (property.getIsTransient() ? tabs(2) + singleLineComment("Transient Property") : "") + "\n");
		
		return builder.toString();
	}

	@Override
	public String buildGetterAndSetter(ModelClassProperty prop) {
		StringBuilder builder = new StringBuilder();
		String propMethodBase = getInterpreter().buildGetterAndSetterName(prop.getName());
		String type = getPropertyType(prop);
		
		builder.append(multiLineComment("Setter for " + prop.getName() + " property", 1) + "\n");
		builder.append(tabs(1) + "public void set" + propMethodBase + "(" + type + " " + prop.getName() + ") {\n");
		builder.append(tabs(2) + "this." + prop.getName() + " = " + prop.getName() + ";\n");
		builder.append(tabs(1) + "}\n\n");
		
		builder.append(multiLineComment("Getter for " + prop.getName() + " property", 1) + "\n");
		builder.append(tabs(1) + "public " + type + " get" + propMethodBase + "() {\n");
		builder.append(tabs(2) + "return this." + prop.getName() + ";\n");
		builder.append(tabs(1) + "}\n\n");

		return builder.toString();
	}

	@Override
	public String getPropertyType(ModelClassProperty property) {
		OrchestrationDataType type = OrchestrationDataType.fromModelProperty(property);
		
		switch(type) {
		case ARRAY:
			if(null != property.getArraySubType()) {
				return "List<" + property.getArraySubType() + ">";
			} else {
				return "List";
			}
			
		case UNKNOWN:
			return "String";
			
		case CLASS:
			return property.getType();
			
		default:
			return type.getJavaName();
		}
	}
}
