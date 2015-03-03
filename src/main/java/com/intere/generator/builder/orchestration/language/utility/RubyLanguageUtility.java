package com.intere.generator.builder.orchestration.language.utility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.models.RubyModelInterpreter;
import com.intere.generator.builder.orchestration.OrchestrationDataType;
import com.intere.generator.builder.orchestration.OrchestrationTree;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

public class RubyLanguageUtility extends AbstractLanguageUtility {
	private static final Logger LOGGER = LogManager.getLogger(RubyLanguageUtility.class);
	JsonLanguageInterpreter interpreter = new RubyModelInterpreter();

	@Override
	public String buildNamespace(ModelClass modelClass) {
		if(hasNamespace(modelClass)) {
			return "module " + modelClass.getNamespace() + "\n";
		}
		return "";
	}

	@Override
	public String buildClassDeclaration(ModelClass modelClass) {
		int tabIndex = (hasNamespace(modelClass) ? 1 : 0);
		return tabs(tabIndex) + "class " + modelClass.getClassName() + " < JsonModel\n";
	}

	@Override
	public String finishClass(ModelClass modelClass, boolean testClass) {
		int tabIndex = (hasNamespace(modelClass) ? 1 : 0);
		StringBuilder builder = new StringBuilder();
		builder.append(tabs(tabIndex) + "end\t\t" + singleLineComment("End of " + modelClass.getClassName()) + "\n");
		if(hasNamespace(modelClass)) {
			builder.append("end " + singleLineComment("End of " + modelClass.getNamespace() + " module\n"));
		}
		builder.append("\n");
		
		return builder.toString();
	}

	@Override
	public String buildSinglePropertyDeclaration(ModelClassProperty property) {
		int tabIndex = (hasNamespace(property.getParentModel()) ? 2 : 1);
		String propName = interpreter.cleanVariableName(property.getName());
		String subClass = interpreter.buildSubClassName(property.getParentModel().getClassName(), property.getName());
		switch(property.getDataType()) {
		case ARRAY:
			switch(property.getArraySubTypeProperty().getDataType()) {
			case CLASS:
				return tabs(tabIndex) + "def " + propName + "\n"
					+ tabs(tabIndex+1) + "@" + propName + " ||= json.try(:[], :" + property.getName() + ").map {|o| " + subClass + ".new(" + property.getName() + ")}\n"
					+ tabs(tabIndex) + "end\n\n";
			default:
				return tabs(tabIndex) + "def " + propName + "\n"
					+ tabs(tabIndex+1) + "@" + propName + " ||= json.try(:[], :" + property.getName() + ")\n"
					+ tabs(tabIndex) + "end\n\n";
			}
		case CLASS:
			return tabs(tabIndex) + "def " + propName + "\n"
					+ tabs(tabIndex+1) + "@" + propName + " ||= " + subClass + ".new json.try(:[], :" + property.getName() + ")\n"
					+ tabs(tabIndex) + "end\n\n";
		default:
			return tabs(tabIndex) + "def " + propName + "\n"
					+ tabs(tabIndex+1) + "json.try(:[], :" + property.getName() + ")\n"
					+ tabs(tabIndex) + "end\n\n";
		}
	}

	@Override
	public String buildGettersAndSetters(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String buildGetterAndSetter(ModelClassProperty prop) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPropertyType(ModelClassProperty property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildImports(ModelClass modelClass) {
		// TODO
		return "";
	}

	@Override
	public String buildClassImplementation(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildSerializationConstants(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildModelUtilityDeclarationMethods(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildModelUtilityDefinitionMethods(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String buildTestClassDeclaration(ModelClass modelClass) {
		if(hasNamespace(modelClass)) {
			return "describe " + modelClass.getNamespace() + "::" + modelClass.getClassName() + " do\n";
		} else {
			return "describe " + modelClass.getClassName() + " do\n";
		}
	}

	@Override
	public String buildTestImports(ModelClass modelClass) {
		return "require 'rails_helper'\n\n";
	}

	@Override
	public String buildTestSetupMethod(ModelClass modelClass) {
		String newClassName = (hasNamespace(modelClass) ? modelClass.getNamespace() + "::" : "") + modelClass.getClassName();
		StringBuilder builder = new StringBuilder();
		builder.append(tabs(1) + "before do\n");
		builder.append(tabs(2) + "@model = " + newClassName + ".new({})\n");
		builder.append(tabs(1) + "end\n\n");
		
		builder.append(tabs(1) + "context '#json' do\n");
		builder.append(tabs(2) + "it 'does symblize keys' do\n");
		builder.append(tabs(3) + "@model = " + newClassName + ".new({'key'=>'value'})\n");
		builder.append(tabs(3) + "expect(@model.json[:key]).to eq('value')\n");
		builder.append(tabs(2) + "end\n");
		builder.append(tabs(1) + "end\n\n");
		return builder.toString();
	}

	@Override
	public String buildTestMethods(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		for(ModelClassProperty prop : modelClass.getProperty()) {
			if(!prop.getIsTransient()) {
				String propName = interpreter.cleanVariableName(prop.getName());
				String subClass = interpreter.buildSubClassName(modelClass.getClassName(), prop.getName());
				builder.append(tabs(1) + "context '#" + propName + "' do\n");
				builder.append(tabs(2) + "it 'does get a valid " + propName + " from symbols' do\n");
				switch(prop.getDataType()) {
				case CLASS:
					builder.append(tabs(3) + "allow(@model).to receive(:json).and_return({:" + prop.getName() + "=>{}})\n");
					builder.append(tabs(3) + "expect(@model." + propName + ".class).to be " + subClass  + "\n");
					break;
				case ARRAY:
					builder.append(tabs(3) + "allow(@model).to receive(:json).and_return({:" + prop.getName() + "=>[]})\n");
					builder.append(tabs(3) + "expect(@model." + propName + ".class).to be Array\n");
					
					break;
				default:
					builder.append(tabs(3) + "allow(@model).to receive(:json).and_return({:" + prop.getName() + "=>'value'})\n");
					builder.append(tabs(3) + "expect(@model." + propName + ").to eq('value')\n");
					break;
				}
				builder.append(tabs(2) + "end\n");
				builder.append(tabs(1) + "end\n\n");
			}
		}
		return builder.toString();
	}
	
	@Override
	public String singleLineComment(String comment, int tabCount) {
		return tabs(tabCount) + "# " + comment;
	}
	
	@Override
	public String multiLineComment(String comment, int tabCount) {
		comment = comment.trim();
		String nlReplacement = "\n" + tabs(tabCount) + "# ";
		return tabs(tabCount) + "#\n" + tabs(tabCount) + "# " + comment.replaceAll("\\\n", nlReplacement) + "\n" + tabs(tabCount) + "#";
	}

	@Override
	public void enforceFilenames(OrchestrationTree tree) {
		for(ModelClass clazz : tree.getModelClasses()) {
			clazz.setFileName(interpreter.buildFilenameFromClassname(clazz.getClassName()));
		}
	}
	
	@Override
	public Map<File, String> copyModelResources(File sourcePath, OrchestrationTree tree) throws IOException {
		Map<File, String> generatedResource = new HashMap<>();
		generatedResource.put(new File(sourcePath, "json_model.rb"), readJsonModelFile());		
		return generatedResource;
	}
	
	private boolean hasNamespace(ModelClass modelClass) {
		return null != modelClass && null != modelClass.getNamespace() && 0 != modelClass.getNamespace().trim().length();
	}

	private String readJsonModelFile() throws IOException {
		return readResourceAndReplaceHeaders("/json_model.rb");
	}
}
