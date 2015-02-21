package com.intere.generator.builder.orchestration.language;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.intere.generator.App;
import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.models.JavaModelInterpreter;
import com.intere.generator.builder.orchestration.OrchestrationDataType;
import com.intere.generator.builder.orchestration.OrchestrationTree;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

public class JavaOrchestration implements LanguageOrchestrator {
	
	LanguageUtility languageUtil = new JavaLanguageUtility();
	JsonLanguageInterpreter interpreter = new JavaModelInterpreter();

	@Override
	public void generateModels(File outputDirectory, OrchestrationTree tree) throws IOException {
		List<File> generatedClasses = new ArrayList<>();
		for(ModelClass modelClass : tree.getModelClasses()) {
			generatedClasses.add(buildModelClassFile(outputDirectory, modelClass));
		}
	}
	
	private File buildModelClassFile(File outputDirectory, ModelClass modelClass) throws IOException {
		String fileContents = buildModelClass(modelClass);
		File outputFile = new File(outputDirectory, modelClass.getFileName() + ".java");
		System.out.println("About to create model class: " + outputFile.getAbsolutePath());
		FileOutputStream fout = new FileOutputStream(outputFile);
		IOUtils.write(fileContents, fout);
		fout.close();
		return outputFile;
	}

	private String buildModelClass(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		builder.append(languageUtil.buildNamespace(modelClass));
		builder.append(languageUtil.buildFileComment(modelClass));
		builder.append(languageUtil.buildImports(modelClass));
		builder.append(languageUtil.buildClassDeclaration(modelClass));
		builder.append(languageUtil.buildPropertyDeclarations(modelClass));
		builder.append(languageUtil.buildGettersAndSetters(modelClass));
		builder.append(languageUtil.finishClass(modelClass));
		return builder.toString();
	}

	class JavaLanguageUtility implements LanguageUtility {
		@Override
		public String tabs(int tabCount) {
			StringBuilder builder = new StringBuilder();
			for(int i=0;i<tabCount; i++) {
				builder.append("\t");
			}
			return builder.toString();
		}

		@Override
		public String singleLineComment(String comment) {
			return singleLineComment(comment, 0);
		}
		
		@Override
		public String singleLineComment(String comment, int tabCount) {
			return tabs(tabCount) + "// " + comment;
		}
		
		@Override
		public String multiLineComment(String comment) {
			return multiLineComment(comment, 0);
		}

		@Override
		public String multiLineComment(String comment, int tabCount) {
			comment = comment.trim();
			String nlReplacement = "\n" + tabs(tabCount) + " * ";
			return tabs(tabCount) + "/**\n" + tabs(tabCount) + " * " + comment.replaceAll("\\\n", nlReplacement) + "\n" + tabs(tabCount) + "*/";
		}

		@Override
		public String buildNamespace(ModelClass modelClass) {
			if(null != modelClass.getNamespace()) {
				return "package " + modelClass.getNamespace() + ";\n";
			}
			return "";
		}

		@Override
		public String buildFileComment(ModelClass modelClass) {
			return multiLineComment("\n" +
					modelClass.getFileName() + ".java\n" +
					"\n" +
					"Generated by JSON Model Generator v" + App.getVersion() + " on " + getDate() + ".\n" +
					"https://github.com/intere/generator-json-models\n" +
					"\n" +
					"The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content\n" +
					"\n") + "\n\n";
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
		public String buildPropertyDeclarations(ModelClass modelClass) {
			StringBuilder builder = new StringBuilder();
			for(ModelClassProperty prop : modelClass.getProperty()) {
				builder.append(buildSinglePropertyDeclaration(prop));
			}
			builder.append("\n");
			return builder.toString();
		}
		
		@Override
		public String buildSinglePropertyDeclaration(ModelClassProperty property) {
			StringBuilder builder = new StringBuilder();
			String propertyType = getPropertyType(property);			
			builder.append(tabs(1) + "private " + propertyType + " " + property.getName());
			if(OrchestrationDataType.ARRAY == OrchestrationDataType.fromString(property.getType())) {
				if(null != property.getArraySubType()) {
					builder.append(" = new ArrayList<" + property.getArraySubType() + ">()" );
				} else {
					builder.append(" = new ArrayList()");
				}
			}
			builder.append(";\n");
			
			return builder.toString();
		}
		
		@Override
		public String getPropertyType(ModelClassProperty property) {
			OrchestrationDataType type = OrchestrationDataType.fromString(property.getType());
			if(null==type) {
				return property.getType();
			}
			
			switch(type) {
			case ARRAY:
				if(null != property.getArraySubType()) {
					return "List<" + property.getArraySubType() + ">";
				} else {
					return "List";
				}
				
			case UNKNOWN:
				return "String";
				
			default:
				return type.getJavaName();
			}
		}

		@Override
		public String buildGettersAndSetters(ModelClass modelClass) {
			StringBuilder builder = new StringBuilder();
			for(ModelClassProperty prop : modelClass.getProperty()) {
				builder.append(buildGetterAndSetter(prop));
			}
			return builder.toString();
		}

		@Override
		public String buildGetterAndSetter(ModelClassProperty prop) {
			StringBuilder builder = new StringBuilder();
			String propMethodBase = interpreter.buildGetterAndSetterName(prop.getName());
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
		public String buildImports(ModelClass modelClass) {
			StringBuilder builder = new StringBuilder();
			Map<String, String> imports = new HashMap<>();
			imports.put("Serializable", "java.io.Serializable");
			
			for(ModelClassProperty prop : modelClass.getProperty()) {
				OrchestrationDataType type = OrchestrationDataType.fromString(prop.getType());
				if(OrchestrationDataType.DATE == type) {
					imports.put("Date", "java.util.Date");
				} else if(OrchestrationDataType.ARRAY == type) {
					imports.put("List", "java.util.List");
					imports.put("ArrayList", "java.util.ArrayList");
				}
			}
			
			for(String key : imports.keySet()) {
				builder.append("import " + imports.get(key) + ";\n");
			}
			
			builder.append("\n");
			
			return builder.toString();
		}
	}
	
	public static String getDate() {
		return new Date().toString();
	}
}
