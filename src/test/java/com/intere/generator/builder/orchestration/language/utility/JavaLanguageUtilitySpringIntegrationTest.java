package com.intere.generator.builder.orchestration.language.utility;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/AppContext.xml"})
public class JavaLanguageUtilitySpringIntegrationTest {
	
	@Autowired
	@Qualifier("JavaLanguage")
	LanguageUtility javaUtil;

	@Test
	public void testServiceObjectExists() {
		assertNotNull(javaUtil);
	}
	
	@Test
	public void testModelBuilderExists() {
		assertNull(javaUtil.getModelBuilder());
	}
	
	@Test
	public void testTestBuilderExists() {
		assertNotNull(javaUtil.getTestBuilder());
	}
	
	@Test
	@Ignore("Not Yet Implemented")
	public void testViewBuilderExists() {
		assertNotNull(javaUtil.getViewBuilder());
	}
	
	@Test
	public void testServiceBuilderExists() {
		assertNotNull(javaUtil.getServiceBuilder());
	}

}
