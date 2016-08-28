package com.intere.generator.metadata;

import java.util.ArrayList;

/**
 * Created by internicolae on 8/12/16.
 */
public class CustomClass extends ModelClass {

    private String customName;

    public boolean hasCustomName() {
        return null != customName && !customName.isEmpty();
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public CustomClass(ModelClass original, String customName) {
        super();

        setUniqueId(original.getUniqueId());
        setClassName(original.getClassName());
        setFileName(original.getFileName());
        setTestClassName(original.getTestClassName());
        setViewClassName(original.getViewClassName());
        setSingleControllerName(original.getSingleControllerName());
        setListControllerName(original.getListControllerName());
        setServiceClassName(original.getServiceClassName());
        setRestServiceClassName(original.getRestServiceClassName());
        setReadonly(original.getReadonly());
        setNamespace(original.getNamespace());
        setHasSubClasses(original.getHasSubClasses());
        setRestUrl(original.getRestUrl());
        setProperty(new ArrayList<>(original.getProperty()));
        setImports(new ArrayList<>(original.getImports()));
        setSummaryProperties(new ArrayList<>(original.getSummaryProperties()));

        setCustomName(customName);
    }


}
