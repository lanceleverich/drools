package org.drools.pmml.pmml_4_2;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dmg.pmml.pmml_4_2.descr.PMML;
import org.drools.pmml.model.DataDictionaryField;
import org.kie.api.io.Resource;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.SimpleTemplateRegistry;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRegistry;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.templates.res.Node;

/**
 * Created by lleveric on 7/12/17.
 */
public abstract class AbstractPMMLUnit implements PMMLUnit {
    private String modelId;
    private PMMLModelType modelType;
    private Resource modelResource;

    private static TemplateRegistry templateRegistry;

    protected AbstractPMMLUnit() {
        this.modelId = "";
        this.modelType = PMMLModelType.UNKNOWN;
        this.modelResource = null;
    }

    protected AbstractPMMLUnit(String modelId) {
        this.modelId = modelId;
        this.modelType = PMMLModelType.UNKNOWN;
        this.modelResource = null;
    }

    protected AbstractPMMLUnit(String modelId, PMMLModelType modelType) {
        this.modelId = modelId;
        this.modelType = modelType;
        this.modelResource = null;
    }

    protected AbstractPMMLUnit(String modelId, PMMLModelType modelType, Resource modelResource) {
        this.modelId = modelId;
        this.modelType = modelType;
        this.modelResource = modelResource;
    }

    @Override
    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    @Override
    public PMMLModelType getModelType() {
        return modelType;
    }

    public void setModelType(PMMLModelType modelType) {
        this.modelType = modelType;
    }

    @Override
    public Resource getModelResource() {
        return modelResource;
    }

    public void setModelResource(Resource modelResource) {
        this.modelResource = modelResource;
    }



    @Override
    public String getModelDataType(PMML pmml) {
        if (templateRegistry == null) {
            templateRegistry = new SimpleTemplateRegistry();
            CompiledTemplate ct = TemplateCompiler.compileTemplate(
                    AbstractPMMLUnit.class.getResourceAsStream("dataDictionary.mvel"),
                    (Map<String, Class<? extends Node>>)null);
            templateRegistry.addNamedTemplate("dataDictionaryObject", ct);
        }
        List<DataDictionaryField> dataFieldsMap = pmml.getDataDictionary().getDataFields()
                .stream().map(df -> new DataDictionaryField(df))
                .collect(Collectors.toList());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TemplateRuntime.execute(templateRegistry.getNamedTemplate("dataDictionaryObject"), baos);
        StringBuilder builder = new StringBuilder();
        return builder.toString();
    }
}
