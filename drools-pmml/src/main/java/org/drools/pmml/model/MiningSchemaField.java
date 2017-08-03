package org.drools.pmml.model;

import org.dmg.pmml.pmml_4_2.descr.FIELDUSAGETYPE;
import org.dmg.pmml.pmml_4_2.descr.MISSINGVALUETREATMENTMETHOD;
import org.dmg.pmml.pmml_4_2.descr.OPTYPE;
import org.dmg.pmml.pmml_4_2.descr.OUTLIERTREATMENTMETHOD;
import org.drools.pmml.pmml_4_2.PMML4Field;

/**
 * Created by lleveric on 7/17/17.
 */
public class MiningSchemaField<T> implements PMML4Field {
    private FIELDUSAGETYPE fieldUsageType;
    private OPTYPE operationType;
    private OUTLIERTREATMENTMETHOD outlierTreatmentMethod;
    private MISSINGVALUETREATMENTMETHOD missingValueTreatmentMethod;
    private String name;
    private T value;


    @Override
    public String getContext() {
        return null;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public boolean isMissing() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }
}
