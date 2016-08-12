package com.intere.generator.builder.orchestration.language;

import com.intere.generator.builder.interpreter.JsonLanguageInterpreter;
import com.intere.generator.builder.orchestration.OrchestrationTree;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility;
import com.intere.generator.metadata.ModelClass;
import com.intere.generator.metadata.ModelClassProperty;
import com.intere.generator.metadata.models.LanguageModelClassProperty;
import com.intere.generator.templates.TemplateConfig;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static com.intere.generator.io.FileIOUtils.ensureExists;

/**
 * Created by einternicola on 8/9/15.
 */
@Service(value="SwiftOrchestration")
public class SwiftOrchestration implements LanguageOrchestrator {
    private static final Logger LOGGER = LogManager.getLogger(SwiftOrchestration.class);

    @Autowired @Qualifier("SwiftLanguage")
    private LanguageUtility languageUtil;

    @Autowired @Qualifier("SwiftInterpreter")
    JsonLanguageInterpreter interpreter;

    @Autowired TemplateConfig template;

    @Override
    public List<File> generateModels(File outputDirectory, OrchestrationTree tree) throws IOException {
        List<File> generatedClasses = new ArrayList<>();
        for(ModelClass modelClass : tree.getModelClasses()) {
            try {
                generatedClasses.add(buildModelClassFile(outputDirectory, modelClass));
            } catch(TemplateException ex) {
                throw new IOException(ex);
            }
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
//        List<File> generatedResources = new ArrayList<>();
//        Map<File, String> resources = languageUtil.copyModelResources(sourcePath, tree);
//        for(File f : resources.keySet()) {
//            LOGGER.info("About to copy resource: " + f.getAbsolutePath());
//            FileOutputStream fout = new FileOutputStream(f);
//            IOUtils.write(resources.get(f), fout);
//            fout.close();
//            generatedResources.add(f);
//        }
//        return generatedResources;
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
    private File buildModelClassFile(File outputDirectory, ModelClass modelClass) throws IOException, TemplateException {
        File completePath = outputDirectory;
        File outputFile = new File(completePath, modelClass.getFileName() + ".swift");

        if(ensureExists(completePath)) {
            LOGGER.info("About to create Model Class: " + outputFile.getAbsolutePath());

            Map<String, Object> model = new HashMap<>();

            model.put("date", new Date());
            model.put("model", modelClass);
            model.put("filename", modelClass.getFileName() + ".swift");
            model.put("properties", getProperties(modelClass));

            template.generateFile(model, "SwiftClass.ftlh", new FileWriter(outputFile));

//            String fileContents = buildModelClass(modelClass);
//            FileOutputStream fout = new FileOutputStream(outputFile);
//            IOUtils.write(fileContents, fout);
//            fout.close();

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
//        builder.append(languageUtil.getModelBuilder().buildGettersAndSetters(modelClass));
//        builder.append(languageUtil.getModelBuilder().buildViewUtilityDefinitionMethods(modelClass));
        builder.append(languageUtil.getModelBuilder().buildModelUtilityDeclarationMethods(modelClass));
        builder.append(languageUtil.getModelBuilder().finishClass(modelClass));
        return builder.toString();
    }


    //
    // Helpers
    //

    private List<LanguageModelClassProperty> getProperties(ModelClass modelClass) {
        List<LanguageModelClassProperty> list = new ArrayList<>();
        for(ModelClassProperty prop : modelClass.getProperty()) {
            list.add(new LanguageModelClassProperty(prop, interpreter));
        }

        return list;
    }

}
