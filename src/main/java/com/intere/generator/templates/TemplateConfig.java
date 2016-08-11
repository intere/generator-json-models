package com.intere.generator.templates;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * Created by internicolae on 8/10/16.
 */
@Component
public class TemplateConfig {

    Configuration templateConfiguration;

    protected TemplateConfig() {
        templateConfiguration = new Configuration();
        templateConfiguration.setClassForTemplateLoading(getClass(), "/templates/");
        templateConfiguration.setDefaultEncoding("UTF-8");
        templateConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    public Configuration getTemplateConfiguration() {
        return templateConfiguration;
    }

    /** Lets you override the path to get the files from).  */
    public void setDirectoryForTemplateLoading(File file) throws IOException {
        templateConfiguration.setDirectoryForTemplateLoading(file);
    }

    /**
     * Generates the file using the provided model, template name and Writer.
     * @param model The model that we'll use to fill in the template with.
     * @param templateName The name of the template file to load.
     * @param out The writer to write the data to.
     * @throws IOException
     * @throws TemplateException
     */
    public void generateFile(Map<String, Object> model, String templateName, Writer out) throws IOException, TemplateException {
        Template template = templateConfiguration.getTemplate(templateName);
        template.process(model, out);
    }

}
