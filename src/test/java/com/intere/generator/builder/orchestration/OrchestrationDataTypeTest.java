package com.intere.generator.builder.orchestration;

import static org.junit.Assert.*;

import org.junit.Test;

public class OrchestrationDataTypeTest {
	
	@Test
	public void testExplicitValuesFromString() {
		String values[] = {"Array"};
		
		for(String value : values) {
			assertNotNull(OrchestrationDataType.fromString(value));
		}
	}
	
//	@Test
//	public void test

	@Test
	public void testFromInternalName() {
		for(OrchestrationDataType type : OrchestrationDataType.values()) {
			if(type.hasInternalName()) {
				assertEquals(type, OrchestrationDataType.fromInternalName(type.getInternalName()));	
			}
		}
	}

	@Test
	public void testFromName() {
		for(OrchestrationDataType type : OrchestrationDataType.values()) {
			assertEquals(type, OrchestrationDataType.fromName(type.name()));
		}
	}

	@Test
	public void testFromString() {
		for(OrchestrationDataType type : OrchestrationDataType.values()) {
			assertEquals(type, OrchestrationDataType.fromString(type.name()));
			if(type.hasInternalName()) {
				assertEquals(type, OrchestrationDataType.fromString(type.getInternalName()));
			}
		}
	}

}
