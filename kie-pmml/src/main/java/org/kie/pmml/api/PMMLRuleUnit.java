package org.kie.pmml.api;


public interface PMMLRuleUnit {

    public ModelInitializer getModelInitializer();

    public ModelApplier getModelApplier();

    public PMMLRuleExecutor getRuleExecutor();
}
