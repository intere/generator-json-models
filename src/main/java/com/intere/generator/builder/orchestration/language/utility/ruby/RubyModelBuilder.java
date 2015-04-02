package com.intere.generator.builder.orchestration.language.utility.ruby;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility.CommentBuilder;
import com.intere.generator.builder.orchestration.language.utility.base.BaseModelBuilder;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

@Service("RubyModelBuilder")
public class RubyModelBuilder extends BaseModelBuilder {
	@Autowired @Qualifier("ShellStyle")
	CommentBuilder commentBuilder;
	
	@Autowired @Qualifier("RubyInterpreter")
	JsonLanguageInterpreter interpreter;
	
	@Override
	public CommentBuilder getCommentBuilder() {
		return commentBuilder;
	}
	
	@Override
	public JsonLanguageInterpreter getInterpreter() {
		return interpreter;
	}
	
	@Override
	public String buildHeaderFileComment(ModelClass modelClass) {
		throw new IllegalAccessError("You're doing it wrong");
	}

	@Override
	public String buildImplementationFileComment(ModelClass modelClass) {
		return buildFileComment(modelClass.getFileName() + ".rb");
	}
	
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
	public String finishClass(ModelClass modelClass) {
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
	public String buildViewUtilityDefinitionMethods(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return "";
	}


}
