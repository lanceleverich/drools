package org.kie.pmml.api;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.RuleUnitExecutor;

public interface ModelInitializer {

    public void initializeModelSession(KieSession ksession);

    public void initializeModelRuleUnitExecutor(RuleUnitExecutor executor, String datasourceName);
}
