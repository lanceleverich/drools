package org.kie.pmml.api;

import org.drools.ruleunit.RuleUnitExecutor;

public interface PMMLRuleExecutor {

    public int executeRules(RuleUnitExecutor executor, PMMLRuleUnit ruleUnit);
}
