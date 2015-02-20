package com.intere.generator.builder.orchestration;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

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
	public void testOverlays() throws Exception {
		OrchestrationTree tree = new OrchestrationTree(metadataPath);
		assertNotNull(tree);
		
	}
}
