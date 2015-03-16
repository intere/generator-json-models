package com.intere.generator.builder.orchestration.language.utility.base;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility.CommentBuilder;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

public abstract class BaseModelBuilder implements LanguageUtility.ModelBuilder, CommentBuilder {
	
	/** Gets you the Comment Builder for this class.  */
	public abstract CommentBuilder getCommentBuilder();
	
	/** Gets you the {@link JsonLanguageInterpreter} for this class.  */
	public abstract JsonLanguageInterpreter getInterpreter();
	
	public boolean hasTransientProperties(ModelClass modelClass) {
		for(ModelClassProperty prop : modelClass.getProperty()) {
			if(prop.getIsTransient()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasNamespace(ModelClass modelClass) {
		return null != modelClass && null != modelClass.getNamespace() && 0 != modelClass.getNamespace().trim().length();
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
	
	@Override
	public String tabs(int tabCount) {
		return getCommentBuilder().tabs(tabCount);
	}
	
	@Override
	public String singleLineComment(String comment) {
		return getCommentBuilder().singleLineComment(comment);
	}

	@Override
	public String singleLineComment(String comment, int tabCount) {
		return getCommentBuilder().singleLineComment(comment, tabCount);
	}

	@Override
	public String multiLineComment(String comment) {
		return getCommentBuilder().multiLineComment(comment);
	}

	@Override
	public String multiLineComment(String comment, int tabCount) {
		return getCommentBuilder().multiLineComment(comment, tabCount);
	}

	@Override
	public String buildFileComment(String filename) {
		return getCommentBuilder().buildFileComment(filename);
	}
}
