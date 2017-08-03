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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.dmg.pmml.pmml_4_2.descr.MiningSchema;
import org.dmg.pmml.pmml_4_2.descr.PMML;
import org.dmg.pmml.pmml_4_2.descr.TreeModel;

public class PMMLTreeModel extends AbstractPMMLModel {

    public static final PMMLModelType treeModelType = PMMLModelType.TREE;
    private TreeModel model;
    private PMMLUnit ownerUnit;

    PMMLTreeModel(String modelId, PMMLUnit ownerUnit, TreeModel model) {
        super(modelId, treeModelType);
        this.model = model;
        this.ownerUnit = ownerUnit;
    }

    @Override
    public String getMiningSchemaPojo() {
        List<MiningSchema> schemas = model.getExtensionsAndNodesAndMiningSchemas().stream()
                .filter(s -> s instanceof MiningSchema)
                .map(s -> (MiningSchema)s)
                .collect(Collectors.toList());
        if (schemas != null && !schemas.isEmpty()) {
            schemas.forEach(schema -> {
                schema.getMiningFields().forEach(mf -> {

                });
            });
        }
        return null;
    }

    @Override
    public String getOutputPojo() {
        return null;
    }
}
