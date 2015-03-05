package com.intere.generator.builder.orchestration.language.utility;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.interpreter.models.RubyModelInterpreter;
import com.intere.generator.builder.orchestration.OrchestrationTree;
import com.intere.generator.builder.orchestration.language.utility.ruby.RubyModelBuilder;
import com.intere.generator.builder.orchestration.language.utility.ruby.RubyTestBuilder;
import com.intere.generator.builder.orchestration.language.utility.ruby.RubyViewBuilder;
import com.intere.generator.metadata.ModelClass;

public class RubyLanguageUtility extends AbstractLanguageUtility {
	JsonLanguageInterpreter interpreter = new RubyModelInterpreter();
	ModelBuilder modelBuilder = new RubyModelBuilder();
	TestBuilder testBuilder = new RubyTestBuilder();
	ViewBuilder viewBuilder = new RubyViewBuilder();
	

	@Override
	public ModelBuilder getModelBuilder() {
		return modelBuilder;
	}
	
	@Override
	public TestBuilder getTestBuilder() {
		return testBuilder;
	}
	
	@Override
	public ViewBuilder getViewBuilder() {
		return viewBuilder;
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

	private String readJsonModelFile() throws IOException {
		return readResourceAndReplaceHeaders("/json_model.rb");
	}
	
	public Map<String,String> getPropertyMappings() {
		return new HashMap<>();
	};
}
