package com.intere.generator.builder.orchestration.language.utility.ruby;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility.CommentBuilder;
import com.intere.generator.builder.orchestration.language.utility.base.BaseRestClientBuilder;
import com.intere.generator.metadata.ModelClass;

@Service("RubyRestClientBuilder")
public class RubyRestClientBuilder extends BaseRestClientBuilder {

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
	public String buildImports(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildClassDeclaration(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildUtilityDeclarationMethods(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String finishClass(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildImplementationFileComment(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String buildHeaderFileComment(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildClassImplementation(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buildUtilityDefinitionMethods(ModelClass modelClass) {
		// TODO Auto-generated method stub
		return null;
	}

}
