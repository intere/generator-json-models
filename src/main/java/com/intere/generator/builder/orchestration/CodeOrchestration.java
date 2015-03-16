package com.intere.generator.builder.orchestration;

import static com.intere.generator.io.FileIOUtils.ensureExists;

import java.io.File;
import java.io.IOException;

import com.intere.generator.builder.orchestration.language.LanguageOrchestrator;

/**
 * This is the "brain" of the Code Orchestration.  It manages the orchestration of everything.  The workflow is the following:
 * <ol>
 * <li>Read the OrchestrationFile and build an {@link OrchestrationTree} from it.</li>
 * <li>Based on the Orchestration File configuration, generate code</li>
 * </ol>
 * 
 */
public class CodeOrchestration {
	private File outputDirectory;
	private OrchestrationTree tree;

	/**
	 * Constructor that takes the Path to the Orchestration File, and a Base output directory.
	 * @param orchestrationFilePath
	 * @param outputDirectory
	 * @throws IOException
	 */
	public CodeOrchestration(String orchestrationFilePath, File outputDirectory) throws IOException {
		this.outputDirectory = outputDirectory;
		tree = new OrchestrationTree(orchestrationFilePath);
		generateCode();
	}
	
	/**
	 * Getter for the Tree.
	 * @return The {@link OrchestrationTree} object.
	 */
	public OrchestrationTree getTree() {
		return tree;
	}
	
	/**
	 * This method handles the delegation of the code.
	 * @throws IOException
	 */
	private void generateCode() throws IOException {
		LanguageOrchestrator orchestrator = OrchestrationUtils.getLanguageOrchestrator(tree.getMetadata());
		orchestrator.review(tree);
		
		if(shouldGeenerateModels()) {
			File srcPath = new File(outputDirectory, "src");
			if(ensureExists(srcPath)) {
				orchestrator.generateModels(srcPath, tree);
				orchestrator.copyModelResources(srcPath, tree);
			}
		}
		
		if(shouldGenerateTests()) {
			File testPath = new File(outputDirectory, "test");
			if(ensureExists(testPath)) {
				orchestrator.generateModelUnitTests(testPath, tree);
			}
		}
		
		if(shouldGenerateServices()) {
			File servicePath = new File(outputDirectory, "services");
			if(ensureExists(servicePath)) {
				orchestrator.generateServices(servicePath, tree);
			}
		}
		
		if(shouldGenerateViews()) {
			File viewPath = new File(outputDirectory, "views");
			if(ensureExists(viewPath)) {
				orchestrator.generateViews(viewPath, tree);
				orchestrator.copyViewResources(viewPath, tree);
			}
		}
		
		if(shouldGenerateRestServices()) {
			File restPath = new File(outputDirectory, "rest-services");
			if(ensureExists(restPath)) {
				orchestrator.generateRestServices(restPath, tree);
			}
		}
	}
	
	private boolean shouldGenerateRestServices() {
		return tree.getMetadata().getGenerate().getRestServices();
	}

	private boolean shouldGenerateViews() {
		return tree.getMetadata().getGenerate().getViews();
	}

	private boolean shouldGenerateServices() {
		return tree.getMetadata().getGenerate().getServices();
	}

	private boolean shouldGenerateTests() {
		return tree.getMetadata().getGenerate().getTests();
	}

	private boolean shouldGeenerateModels() {
		return tree.getMetadata().getGenerate().getModels();
	}
}
