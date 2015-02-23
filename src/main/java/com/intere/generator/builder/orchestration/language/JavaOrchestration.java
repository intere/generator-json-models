package com.intere.generator.builder.orchestration.language;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

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
		builder.append(languageUtil.buildFileComment(modelClass, ".java"));
		builder.append(languageUtil.buildImports(modelClass));
		builder.append(languageUtil.buildClassDeclaration(modelClass));
		builder.append(languageUtil.buildPropertyDeclarations(modelClass));
		builder.append(languageUtil.buildGettersAndSetters(modelClass));
		builder.append(languageUtil.finishClass(modelClass));
		return builder.toString();
	}

	class JavaLanguageUtility extends AbstractLanguageOrchestrator {
		@Override
		public String buildNamespace(ModelClass modelClass) {
			if(null != modelClass.getNamespace()) {
				return "package " + modelClass.getNamespace() + ";\n";
			}
			return "";
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
			builder.append(tabs(1) + "private " + propertyType + " " + property.getName());
			if(OrchestrationDataType.ARRAY == OrchestrationDataType.fromModelProperty(property)) {
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
			OrchestrationDataType type = OrchestrationDataType.fromModelProperty(property);
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
				OrchestrationDataType type = OrchestrationDataType.fromModelProperty(prop);
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

		@Override
		public String buildClassImplementation(ModelClass modelClass) {
			// Not Used for this language
			return null;
		}

		@Override
		public String buildSerializationConstants(ModelClass modelClass) {
			// Not Used for this language
			return null;
		}

		@Override
		public String buildModelUtilityDeclarationMethods(ModelClass modelClass) {
			// Not currently used
			return null;
		}

		@Override
		public String buildModelUtilityDefinitionMethods(ModelClass modelClass) {
			// Not currently used
			return null;
		}
	}
}
