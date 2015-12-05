package com.intere.generator.builder.orchestration;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.intere.generator.builder.orchestration.language.LanguageOrchestrator;

@Component
public class CodeOrchestratorFactory {
	
	@Autowired @Qualifier("JavaOrchestration")
	private LanguageOrchestrator javaOrchestration;
	
	@Autowired @Qualifier("ObjectiveCOrchestration")
	private LanguageOrchestrator objcOrchestration;
	
	@Autowired @Qualifier("RubyOrchestration")
	private LanguageOrchestrator rubyOrchestration;

	@Autowired @Qualifier("SwiftOrchestration")
	private LanguageOrchestrator swiftOrchestration;

	public void beginOrchestration(String orchestrationFilePath, File outputDirectory) throws IOException {
		CodeOrchestration orchestrator = new CodeOrchestration(orchestrationFilePath, outputDirectory);
		switch (orchestrator.getTree().getLanguage()) {
		case Java:
			orchestrator.setOrchestrator(javaOrchestration);
			break;
			
		case ObjC:
			orchestrator.setOrchestrator(objcOrchestration);
			break;
			
		case Ruby:
			orchestrator.setOrchestrator(rubyOrchestration);
			break;

		case Swift:
			orchestrator.setOrchestrator(swiftOrchestration);
			break;
			
		default:
			throw new IllegalStateException("We don't currently have a spring mapper for Language Type: " 
				+ orchestrator.getTree().getLanguage().name());
		}
		
		orchestrator.generateCode();
	}	
}
