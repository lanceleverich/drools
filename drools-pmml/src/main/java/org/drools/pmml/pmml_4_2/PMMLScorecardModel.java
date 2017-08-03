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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dmg.pmml.pmml_4_2.descr.MiningSchema;
import org.dmg.pmml.pmml_4_2.descr.Scorecard;
import org.drools.pmml.model.DataDictionaryField;
import org.mvel2.integration.impl.MapVariableResolverFactory;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.SimpleTemplateRegistry;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRegistry;
import org.mvel2.templates.TemplateRuntime;

public class PMMLScorecardModel extends AbstractPMMLModel {
	private static final PMMLModelType scorecardModelType = PMMLModelType.SCORECARD;
	private static final String MINING_TEMPLATE = "/org/drools/pmml/pmml_4_2/templates/global/dataDefinition/dataDictionaryObject.mvel";
	private static TemplateRegistry templateRegistry;
	private Scorecard model;
	private PMMLUnit owner;
	
	public PMMLScorecardModel(String modelId, PMMLUnit ownerUnit, Scorecard model) {
		super(modelId, scorecardModelType);
		this.model = model;
		this.owner = ownerUnit;
	}
	
	@Override
	public String getMiningSchemaPojo() {
		String output = null;
		MiningSchema schema = model.getExtensionsAndCharacteristicsAndMiningSchemas().stream()
				.filter(obj -> obj instanceof MiningSchema)
				.findFirst()
				.map(obj -> (MiningSchema)obj)
				.orElse(null);
		
		if (schema != null) {
			List<String> schemaFieldsList = schema.getMiningFields().stream()
					.map(fld -> fld.getName())
					.collect(Collectors.toList());
			Map<String, DataDictionaryField> fieldsMap = ((PMMLUnitImpl)owner).getDictionaryFieldsMap(schemaFieldsList);
			if (fieldsMap != null) {
				TemplateRegistry registry = getTemplateRegistry();
		        Map<String, Object> vars = new HashMap<>();
		        vars.put("packageName",
		                 "org.drools.pmml.dataObject");
		        vars.put("className",
		                 "PmmlDataDictionary");
		        vars.put("dataFields",
		                 fieldsMap.values());
		        vars.put("imports",
		                 new ArrayList<>());
		        ByteArrayOutputStream baos = new ByteArrayOutputStream();
		        TemplateRuntime.execute(registry.getNamedTemplate("dataDictionaryObject"),
		                                null,
		                                new MapVariableResolverFactory(vars),
		                                baos);
		        output = new String(baos.toByteArray());
			}
		}
		return output;
	}

	@Override
	public String getOutputPojo() {
		// TODO Auto-generated method stub
		return null;
	}

	private TemplateRegistry getTemplateRegistry() {
		if (templateRegistry == null) {
    		templateRegistry = new SimpleTemplateRegistry();
    		InputStream inputStream = PMMLUnitImpl.class.getResourceAsStream(MINING_TEMPLATE);
    		CompiledTemplate ct = TemplateCompiler.compileTemplate(inputStream);
    		templateRegistry.addNamedTemplate("dataDictionaryObject", ct);
		}
		return templateRegistry;
	}
}
