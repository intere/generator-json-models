package com.intere.generator.builder.interpreter.services;

import com.intere.generator.builder.interpreter.models.ObjectiveCModelInterpreter;

public class ObjectiveCServiceInterpreter extends ObjectiveCModelInterpreter {	
	public String getServiceClassName(String className) {
		return buildClassName(className);
	}
	@Override
	public String buildClassName(String className) {
		return super.buildClassName(className) + "Service";
	}
}
