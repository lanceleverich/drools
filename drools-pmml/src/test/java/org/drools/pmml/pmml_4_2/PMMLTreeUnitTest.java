package org.drools.pmml.pmml_4_2;

import org.dmg.pmml.pmml_4_2.descr.PMML;
import org.drools.core.io.impl.ClassPathResource;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.kie.api.io.Resource;

/**
 * Created by lleveric on 7/14/17.
 */
public class PMMLTreeUnitTest {
    private static final String SIMPLE_TREE_TEST = "org/drools/pmml/pmml_4_2/test_tree_simple.xml";
    private Resource testResource;

    @Before
    public void setup() {
        testResource = new ClassPathResource(SIMPLE_TREE_TEST);
    }

    @Test
    public void simpleTreeTest() {
        PMMLTreeUnit unit = new PMMLTreeUnit("test",testResource);
        PMML testObj = unit.getPmmlFromResource();
        String dataType = unit.getModelDataType(testObj);
        System.out.println(dataType);
    }
}
