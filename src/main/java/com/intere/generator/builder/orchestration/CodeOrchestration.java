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
 */
public class CodeOrchestration {
	private File outputDirectory;
	private OrchestrationTree tree;
	private LanguageOrchestrator orchestrator;
	
	/** Default Constructor, no initialization. */
	public CodeOrchestration() {
		super();
	}

	/**
	 * Constructor that takes the Path to the Orchestration File, and a Base output directory.
	 * @param orchestrationFilePath
	 * @param outputDirectory
	 * @throws IOException
	 */
	public CodeOrchestration(String orchestrationFilePath, File outputDirectory) throws IOException {
		initialize(orchestrationFilePath, outputDirectory);
	}
	
	/**
	 * Sets the output directory and builds the {@link OrchestrationTree} from the provided Orchestration File Path.
	 * @param orchestrationFilePath
	 * @param outputDirectory
	 * @throws IOException
	 */
	public void initialize(String orchestrationFilePath, File outputDirectory) throws IOException {
		this.outputDirectory = outputDirectory;
		this.tree = new OrchestrationTree(orchestrationFilePath);
	}
	
	/**
	 * This method handles the delegation of the code.
	 * @throws IOException
	 */
	public void generateCode() throws IOException {
		if(null == orchestrator) {
			throw new IllegalStateException("We don't have a code orchestrator yet, please set this before calling generateCode()");
		}
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
		
		if(shouldGenerateRestClients()) {
			File restClientPath = new File(outputDirectory, "rest-clients");
			if(ensureExists(restClientPath)) {
				orchestrator.generateRestClients(restClientPath, tree);
			}
		}
		
		if(shouldGenerateRestServices()) {
			File restServicePath = new File(outputDirectory, "rest-services");
			if(ensureExists(restServicePath)) {
				orchestrator.generateRestServices(restServicePath, tree);
			}
		}
	}

	/**
	 * Setter for the {@link LanguageOrchestrator}.
	 * @param orchestrator
	 */
	public void setOrchestrator(LanguageOrchestrator orchestrator) {
		this.orchestrator = orchestrator;
	}
	
	/**
	 * Getter for the Tree.
	 * @return The {@link OrchestrationTree} object.
	 */
	public OrchestrationTree getTree() {
		return tree;
	}
	
	private boolean hasTree() {
		return null != tree;
	}
	
	private boolean hasMetadata() {
		return hasTree() && null != tree.getMetadata();
	}
	
	private boolean hasGenerate() {
		return hasMetadata() && null != tree.getMetadata().getGenerate();
	}
	
	private boolean checkValue(Boolean value) {
		return null != value && value.booleanValue();
	}
	
	private boolean shouldGenerateRestClients() {
		return hasGenerate() && checkValue(tree.getMetadata().getGenerate().getRestClients());			
	}
	
	private boolean shouldGenerateRestServices() {
		return hasGenerate() && checkValue(tree.getMetadata().getGenerate().getRestServices());
	}

	private boolean shouldGenerateViews() {
		return hasGenerate() && checkValue(tree.getMetadata().getGenerate().getViews());
	}

	private boolean shouldGenerateServices() {
		return hasGenerate() && checkValue(tree.getMetadata().getGenerate().getServices());
	}

	private boolean shouldGenerateTests() {
		return hasGenerate() && checkValue(tree.getMetadata().getGenerate().getTests());
	}

	private boolean shouldGeenerateModels() {
		return hasGenerate() && checkValue(tree.getMetadata().getGenerate().getModels());
	}
}
