package com.intere.generator.builder.orchestration.language.utility;

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
		OrchestrationDataType dt = OrchestrationDataType.fromModelProperty(property);
		switch(dt) {
		case ARRAY:
			
		case CLASS:
			return tabs(tabIndex) + "def " + propName + "\n"
					+ tabs(tabIndex+1) + "@" + propName + " ||= " + subClass + ".new @json.try(:[], '" + property.getName() + "')\n"
					+ tabs(tabIndex) + "end\n\n";
		default:
			return tabs(tabIndex) + "def " + propName + "\n"
					+ tabs(tabIndex+1) + "@json.try(:[], '" + property.getName() + "')\n"
					+ tabs(tabIndex) + "end\n\n";
		}
	}

	@Override
	public String buildGettersAndSetters(ModelClass modelClass) {
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

	@Override
	public String buildImports(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
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
		return null;
	}

	@Override
	public String buildTestClassDeclaration(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildTestImports(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildTestSetupMethod(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildTestMethods(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
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
	
	private boolean hasNamespace(ModelClass modelClass) {
		return null != modelClass && null != modelClass.getNamespace() && 0 != modelClass.getNamespace().trim().length();
	}
}
