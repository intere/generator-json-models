package com.intere.generator.builder.orchestration.language.utility.ruby;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.models.RubyModelInterpreter;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility.CommentBuilder;
import com.intere.generator.builder.orchestration.language.utility.base.BaseTestBuilder;
import com.intere.generator.builder.orchestration.language.utility.comments.ShellStyleCommentBuilder;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

public class RubyTestBuilder extends BaseTestBuilder {
	CommentBuilder commentBuilder = new ShellStyleCommentBuilder();
	JsonLanguageInterpreter interpreter = new RubyModelInterpreter();
	
	@Override
	public CommentBuilder getCommentBuilder() {
		return commentBuilder;
	}
	
	@Override
	public JsonLanguageInterpreter getInterpreter() {
		return interpreter;
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
	public String buildNamespace(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildHeaderFileComment(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildImplementationFileComment(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildImports(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildSerializationConstants(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildClassImplementation(ModelClass modelClass) {
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
		return null;
	}

	@Override
	public String buildClassDeclaration(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String finishClass(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildSinglePropertyDeclaration(ModelClassProperty property) {
		// TODO Auto-generated method stub
		return null;
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
}
