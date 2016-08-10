package com.intere.generator.blackbox;

import com.intere.generator.metadata.ModelClass;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.junit.Test;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by internicolae on 8/10/16.
 */
public class FreemarkerTest {

    @Test
    public void testCreateTemplate() throws Exception {
        // Create your Configuration instance, and specify if up to what FreeMarker
        // version (here 2.3.25) do you want to apply the fixes that are not 100%
        // backward-compatible. See the Configuration JavaDoc for details.
        Configuration cfg = new Configuration();

        // Specify the source where the template files come from. Here I set a
        // plain directory for it, but non-file-system sources are possible too:
        cfg.setDirectoryForTemplateLoading(new File(getClass().getResource("/user.json").getFile()).getParentFile());

        // Set the preferred charset template files are stored in. UTF-8 is
        // a good choice in most applications:
        cfg.setDefaultEncoding("UTF-8");

        // Sets how errors will appear.
        // During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        // cfg.setTemplateExceptionHandler(this);

        Template temp = cfg.getTemplate("test.ftlh");

        Writer out = new StringWriter();
        temp.process(createDataModel(), out);

        String result = out.toString();
        assertNotNull(result);

        System.out.println(result);
    }

    // Create the Data Model
    public Map<String, Object> createDataModel() {
        // Create the root hash. We use a Map here, but it could be a JavaBean too.
        Map<String, Object> root = new HashMap<>();

        // Put string "date" into the root
        root.put("date", new Date());

        // Create the "latestProduct" hash. We use a JavaBean here, but it could be a Map too.
        ModelClass clazz = new ModelClass();
        clazz.setClassName("LatestProduct");
        clazz.setFileName("LatestProduct.java");
        clazz.setReadonly(false);
        clazz.setRestUrl("/products/latest");


        // and put it into the root
        root.put("latestProduct", clazz);

        return root;
    }
}
