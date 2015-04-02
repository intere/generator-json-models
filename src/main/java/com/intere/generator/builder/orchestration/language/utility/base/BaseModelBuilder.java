package com.intere.generator.builder.orchestration.language.utility.base;

import com.intere.generator.builder.orchestration.language.utility.LanguageUtility;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

public abstract class BaseModelBuilder extends BaseCommentBuilder implements LanguageUtility.ModelBuilder {
	
	public boolean hasTransientProperties(ModelClass modelClass) {
		for(ModelClassProperty prop : modelClass.getProperty()) {
			if(prop.getIsTransient()) {
				return true;
			}
		}
		return false;
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
	public String buildGettersAndSetters(ModelClass modelClass) {
		StringBuilder builder = new StringBuilder();
		for(ModelClassProperty prop : modelClass.getProperty()) {
			builder.append(buildGetterAndSetter(prop));
		}
		return builder.toString();
	}
}
