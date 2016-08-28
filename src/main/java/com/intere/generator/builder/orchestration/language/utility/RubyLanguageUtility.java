package com.intere.generator.builder.orchestration.language.utility;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.orchestration.OrchestrationTree;
import com.intere.generator.metadata.ModelClass;

@Service("RubyLanguage")
public class RubyLanguageUtility extends AbstractLanguageUtility {
	@Autowired @Qualifier("RubyInterpreter")
	JsonLanguageInterpreter interpreter;


	@Override
	public ViewBuilder getViewBuilder() {
		return null;
	}
	
	@Override
	public ServiceBuilder getServiceBuilder() {
		return null;
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
	
	@Override
	public Map<File, String> copyViewResources(File viewPath,OrchestrationTree tree) throws IOException {
		// Currently a No-Op for Ruby;
		return new HashMap<>();
	}

	private String readJsonModelFile() throws IOException {
		return readResourceAndReplaceHeaders("/json_model.rb");
	}
	
	public Map<String,String> getPropertyMappings() {
		return new HashMap<>();
	};
}
