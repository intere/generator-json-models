package com.intere.generator.builder.orchestration.language.utility;

import com.intere.generator.builder.orchestration.OrchestrationTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by einternicola on 8/9/15.
 */
@Service("SwiftLanguage")
public class SwiftLanguageUtility extends AbstractLanguageUtility {

    @Override
    public Map<String, String> getPropertyMappings() {
        Map<String, String> mappings = new HashMap<>();
        return mappings;
    }

    @Override
    public ViewBuilder getViewBuilder() {
        return null;
    }

    @Override
    public ServiceBuilder getServiceBuilder() {
        return null;
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
