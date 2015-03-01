package com.intere.generator.builder.orchestration;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;

public class OrchestrationTreeTest {
	
	private String metadataPath;
	
	@Before
	public void setUp() throws Exception {
		URL url = getClass().getResource("/metadata/metadata.json");
		metadataPath = new File(url.toURI()).getAbsolutePath();
		assertNotNull(metadataPath);
	}

	@Test
	public void testCreation() throws Exception {
		OrchestrationTree tree = new OrchestrationTree(metadataPath);
		assertNotNull(tree);
		
		// Perform Assertions that we actually do have some classes in the collection:
		assertTrue(tree.getModelClasses().size() > 0);
		
		// Perform Assertions that we have specific models in the model class map:
		assertTrue(tree.getModelClassMap().containsKey("Contest"));
		assertTrue(tree.getModelClassMap().containsKey("Enumeration"));
		assertTrue(tree.getModelClassMap().containsKey("ContestResults"));
		assertTrue(tree.getModelClassMap().containsKey("EnumerationPurseTypes"));
		
		// Perform Assertions that classes have properties:
		assertTrue(tree.getModelClassMap().get("Contest").getProperty().size()>0);
		assertTrue(tree.getModelClassMap().get("Enumeration").getProperty().size()>0);
		assertTrue(tree.getModelClassMap().get("ContestResults").getProperty().size()>0);
		assertTrue(tree.getModelClassMap().get("EnumerationPurseTypes").getProperty().size()>0);
		
		// Perform Assertions about "Readonly" property of classes:
		assertFalse(tree.getModelClassMap().get("Contest").getReadonly());
		assertTrue(tree.getModelClassMap().get("Enumeration").getReadonly());
		assertTrue(tree.getModelClassMap().get("EnumerationPurseTypes").getReadonly());		
	}
	
	@Test
	public void testPropertyDataTypes() throws Exception {
		OrchestrationTree tree = new OrchestrationTree(metadataPath);
		assertNotNull(tree);
		for(ModelClass model : tree.getModelClasses()) {
			assertNotNull(model);
			for(ModelClassProperty prop : model.getProperty()) {
				assertNotNull(prop);
				OrchestrationDataType dt = OrchestrationDataType.fromModelProperty(prop);
				String propInfo = prop.getName() + " - " + prop.getType() + (prop.getIsArray() ? "<" + prop.getArraySubType() + ">" : "");
				assertNotNull(propInfo + " was null", dt);
			}
		}
	}
	
	@Test
	public void testArraySubTypes() throws Exception {
		OrchestrationTree tree = new OrchestrationTree(metadataPath);
		assertNotNull(tree);
		
		for(ModelClass clazz : tree.getModelClasses()) {
			for(ModelClassProperty prop : clazz.getProperty()) {
				assertNotNull(prop.getDataType());
				if(prop.getDataType() == OrchestrationDataType.ARRAY) {
					assertNotNull(prop.getArraySubTypeProperty());
				}
			}
		}
	}
	
	@Test
	public void testOverlays() throws Exception {
		OrchestrationTree tree = new OrchestrationTree(metadataPath);
		assertNotNull(tree);
		// TODO
		assertNotNull(tree.getModelClassMap().get("Contest").getProperty());
		for(ModelClassProperty prop : tree.getModelClassMap().get("Contest").getProperty()) {
			if(prop.getName().equals("transientProperty")) {
				assertTrue(prop.getIsTransient());
			}
		}
	}
}
