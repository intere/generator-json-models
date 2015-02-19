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
		assertTrue(tree.getModelClasses().size() > 0);
		assertTrue(tree.getModelClassMap().containsKey("Contest"));
		assertTrue(tree.getModelClassMap().containsKey("Enumeration"));
		assertTrue(tree.getModelClassMap().containsKey("ContestResults"));
		assertTrue(tree.getModelClassMap().containsKey("EnumerationPurseTypes"));
	}

}
