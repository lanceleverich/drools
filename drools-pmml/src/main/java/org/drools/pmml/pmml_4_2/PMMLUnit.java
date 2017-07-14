package org.drools.pmml.pmml_4_2;

import org.dmg.pmml.pmml_4_2.descr.PMML;
import org.kie.api.io.Resource;

/**
 * Created by lleveric on 7/12/17.
 */
public interface PMMLUnit {
    public String getModelId();
    public PMMLModelType getModelType();
    public Resource getModelResource();
    public String getTheory(PMML pmml);
    public String getModelDataType(PMML pmml);
}
