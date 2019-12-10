package org.kie.pmml.api;

import java.util.List;

import org.kie.api.KieBase;
import org.kie.api.pmml.PMML4Result;
import org.kie.api.pmml.PMMLRequestData;
import org.kie.api.pmml.PMMLRuleUnit;

public interface ModelApplier {

    public List<PMML4Result> applyModel(PMMLRequestData request, KieBase kbase, PMMLRuleUnit ruleUnit);

}
