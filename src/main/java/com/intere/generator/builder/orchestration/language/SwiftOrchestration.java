package com.intere.generator.builder.orchestration.language;

import com.intere.generator.builder.orchestration.OrchestrationTree;
import com.intere.generator.builder.orchestration.language.utility.LanguageUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by einternicola on 8/9/15.
 */
@Service(value="SwiftOrchestration")
public class SwiftOrchestration implements LanguageOrchestrator {
    private static final Logger LOGGER = LogManager.getLogger(SwiftOrchestration.class);

    @Autowired @Qualifier("SwiftLanguage")
    private LanguageUtility languageUtil;


    @Override
    public List<File> generateModels(File sourcePath, OrchestrationTree tree) throws IOException {
        return null;
    }

    @Override
    public List<File> generateModelUnitTests(File testPath, OrchestrationTree tree) throws IOException {
        return null;
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

    }

    @Override
    public List<File> copyViewResources(File viewPath, OrchestrationTree tree) throws IOException {
        return null;
    }
}
