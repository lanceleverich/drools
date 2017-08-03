package org.drools.pmml.pmml_4_2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.dmg.pmml.pmml_4_2.descr.PMML;
import org.dmg.pmml.pmml_4_2.descr.Scorecard;
import org.dmg.pmml.pmml_4_2.descr.TreeModel;
import org.kie.api.io.Resource;

/**
 * Created by lleveric on 7/14/17.
 */
public class PMMLModelFactory {

    private static PMMLModelFactory ourInstance = new PMMLModelFactory();

    public static PMMLModelFactory getInstance() {
        return ourInstance;
    }

    private PMMLModelFactory() {
    }


    public List<PMMLModel> createPmmlModels(PMMLUnit ownerUnit) {
        List<PMMLModel> pmmlUnits = new ArrayList<>();
        ownerUnit.getPMML().getAssociationModelsAndBaselineModelsAndClusteringModels()
                .forEach( s -> {
                    if ( s instanceof TreeModel ) {
                        PMMLTreeModel unit = new PMMLTreeModel("TreeModel"+pmmlUnits.size(), ownerUnit, (TreeModel)s );
                        pmmlUnits.add(unit);
                    } else if ( s instanceof Scorecard ) {
                    	PMMLScorecardModel unit = new PMMLScorecardModel("Scorecard"+pmmlUnits.size(), ownerUnit, (Scorecard)s );
                    	pmmlUnits.add(unit);
                    }
                });
        return pmmlUnits;
    }
}
