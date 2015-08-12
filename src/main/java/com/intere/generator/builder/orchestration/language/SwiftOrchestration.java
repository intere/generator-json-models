package com.intere.generator.builder.orchestration.language;

import com.intere.generator.builder.orchestration.OrchestrationTree;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility;
import com.intere.generator.metadata.ModelClass;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.intere.generator.io.FileIOUtils.ensureExists;

/**
 * Created by einternicola on 8/9/15.
 */
@Service(value="SwiftOrchestration")
public class SwiftOrchestration implements LanguageOrchestrator {
    private static final Logger LOGGER = LogManager.getLogger(SwiftOrchestration.class);

    @Autowired @Qualifier("SwiftLanguage")
    private LanguageUtility languageUtil;


    @Override
    public List<File> generateModels(File outputDirectory, OrchestrationTree tree) throws IOException {
        List<File> generatedClasses = new ArrayList<>();
        for(ModelClass modelClass : tree.getModelClasses()) {
            generatedClasses.add(buildModelClassFile(outputDirectory, modelClass));
        }
        return generatedClasses;
    }

    @Override
    public List<File> generateModelUnitTests(File outputDirectory, OrchestrationTree tree) throws IOException {
        List<File> generatedClasses = new ArrayList<>();
        for(ModelClass modelClass : tree.getModelClasses()) {
            generatedClasses.add(buildTestFile(outputDirectory, modelClass));
        }

        return generatedClasses;
    }

    @Override
    public List<File> generateServices(File servicePath, OrchestrationTree tree) throws IOException {
        return null;
    }

    @Override
    public List<File> generateViews(File viewPath, OrchestrationTree tree) throws IOException {
        return null;
    }

    @Override
    public List<File> generateRestServices(File restPath, OrchestrationTree tree) throws IOException {
        return null;
    }

    @Override
    public List<File> copyModelResources(File sourcePath, OrchestrationTree tree) throws IOException {
        return null;
    }

    @Override
    public void review(OrchestrationTree tree) {
        languageUtil.enforcePropertyMappings(tree);
    }

    @Override
    public List<File> copyViewResources(File viewPath, OrchestrationTree tree) throws IOException {
        return null;
    }

    /**
     * Builds the Model Class File (in the provided output directory).
     * @param outputDirectory
     * @param modelClass
     * @return
     * @throws IOException
     */
    private File buildModelClassFile(File outputDirectory, ModelClass modelClass) throws IOException {
        File completePath = outputDirectory;
        if(ensureExists(completePath)) {
            String fileContents = buildModelClass(modelClass);
            File outputFile = new File(completePath, modelClass.getFileName() + ".swift");
            LOGGER.info("About to create Model Class: " + outputFile.getAbsolutePath());
            FileOutputStream fout = new FileOutputStream(outputFile);
            IOUtils.write(fileContents, fout);
            fout.close();
            return outputFile;
        } else {
            LOGGER.error("Could not create directory: " + completePath);
        }
        return null;
    }

    /**
     * Builds the Test Class File (in the provided output directory).
     * @param outputDirectory
     * @param modelClass
     * @return
     */
    private File buildTestFile(File outputDirectory, ModelClass modelClass) throws IOException {
        File completePath = outputDirectory;
        if(ensureExists(completePath)) {
            String fileContents = buildTestClass(modelClass);
            File outputFile = new File(completePath, modelClass.getTestClassName() + ".swift");
            LOGGER.info("About to create Test Class: " + outputFile.getAbsolutePath());
            FileOutputStream fout = new FileOutputStream(outputFile);
            IOUtils.write(fileContents, fout);
            fout.close();
            return outputFile;
        } else {
            LOGGER.error("Could not create directory: " + completePath);
        }
        return null;
    }

    //
    // Helper Methods
    //

    private String buildTestClass(ModelClass modelClass) {
        StringBuilder builder = new StringBuilder();
        builder.append(languageUtil.getTestBuilder().buildImplementationFileComment(modelClass));
        builder.append(languageUtil.getTestBuilder().buildTestImports(modelClass));
        builder.append(languageUtil.getTestBuilder().buildTestClassDeclaration(modelClass));
        builder.append(languageUtil.getTestBuilder().buildTestSetupMethod(modelClass));
        builder.append(languageUtil.getTestBuilder().buildTestMethods(modelClass));
        builder.append(languageUtil.getTestBuilder().finishClass(modelClass));

        return builder.toString();
    }

    private String buildModelClass(ModelClass modelClass) {
        StringBuilder builder = new StringBuilder();
        builder.append(languageUtil.getModelBuilder().buildImplementationFileComment(modelClass));
        builder.append(languageUtil.getModelBuilder().buildImports(modelClass));
        builder.append(languageUtil.getModelBuilder().buildClassImplementation(modelClass));
        builder.append(languageUtil.getModelBuilder().buildSerializationConstants(modelClass));
        builder.append(languageUtil.getModelBuilder().buildPropertyDeclarations(modelClass));
        builder.append(languageUtil.getModelBuilder().buildGettersAndSetters(modelClass));
//        builder.append(languageUtil.getModelBuilder().buildViewUtilityDefinitionMethods(modelClass));
        builder.append(languageUtil.getModelBuilder().finishClass(modelClass));
        return builder.toString();
    }

}
