/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.pmml.pmml_4_2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.drools.core.io.impl.ClassPathResource;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.io.Resource;

public class PMMLTreeUnitTest {
    private static final String SIMPLE_TREE_TEST = "org/drools/pmml/pmml_4_2/test_tree_simple.xml";
    private Resource testResource;

    @Before
    public void setup() {
        testResource = new ClassPathResource(SIMPLE_TREE_TEST);
    }

    @Test
    public void simpleTreeTest() {
        PMMLUnit unit = new PMMLUnitImpl(testResource);

        List<PMMLModel> models = unit.getModels();
        assertEquals(1,models.size());
        assertTrue(models.get(0)  instanceof PMMLTreeModel);

        PMMLTreeModel model = (PMMLTreeModel)models.get(0);
        String dataType = unit.getDataDictionaryPojo();
        System.out.println(dataType);
    }
}
