package com.intere.generator.builder.orchestration.language.utility;

import com.intere.generator.builder.orchestration.OrchestrationTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by einternicola on 8/9/15.
 */
@Service("SwiftLanguage")
public class SwiftLanguageUtility extends AbstractLanguageUtility {

    @Autowired @Qualifier("SwiftModelBuilder")
    ModelBuilder modelBuilder;
    @Autowired @Qualifier("SwiftTestBuilder")
    TestBuilder testBuilder;
    @Autowired @Qualifier("SwiftViewBuilder")
    ViewBuilder viewBuilder;
    @Autowired @Qualifier("SwiftServiceBuilder")
    ServiceBuilder serviceBuilder;

    @Override
    public Map<String, String> getPropertyMappings() {
        return null;
    }

    @Override
    public ModelBuilder getModelBuilder() {
        return modelBuilder;
    }

    @Override
    public TestBuilder getTestBuilder() {
        return testBuilder;
    }

    @Override
    public ViewBuilder getViewBuilder() {
        return null;
    }

    @Override
    public ServiceBuilder getServiceBuilder() {
        return serviceBuilder;
    }

    @Override
    public void enforceFilenames(OrchestrationTree tree) {

    }

    @Override
    public Map<File, String> copyModelResources(File sourcePath, OrchestrationTree tree) throws IOException {
        return null;
    }

    @Override
    public Map<File, String> copyViewResources(File viewPath, OrchestrationTree tree) throws IOException {
        return null;
    }
}
