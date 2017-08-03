package org.drools.pmml.pmml_4_2;

/**
 * Created by lleveric on 7/17/17.
 */
public abstract class AbstractPMMLModel implements PMMLModel {
    private String modelId;
    private PMMLModelType modelType;

    AbstractPMMLModel(String modelId, PMMLModelType modelType) {
        this.modelId = modelId;
        this.modelType = modelType;
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


}
