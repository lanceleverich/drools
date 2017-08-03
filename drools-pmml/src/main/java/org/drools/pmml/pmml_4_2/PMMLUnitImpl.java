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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.dmg.pmml.pmml_4_2.descr.PMML;
import org.drools.pmml.model.DataDictionaryField;
import org.kie.api.io.Resource;
import org.mvel2.integration.impl.MapVariableResolverFactory;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.SimpleTemplateRegistry;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRegistry;
import org.mvel2.templates.TemplateRuntime;

public class PMMLUnitImpl implements PMMLUnit {
    private static String DATA_DICTIONARY_OBJ_TEMPLATE = "/org/drools/pmml/pmml_4_2/templates/global/dataDefinition/dataDictionaryObject.mvel";
    private static String DATA_DICTIONARY_RULES_TEMPLATE = "/org/drools/pmml/pmml_4_2/templates/global/dataDefinition/dataDictionaryRules.mvel";
    protected PMML pmml;
    protected List<PMMLModel> pmmlModels;
    protected String dataDictionaryPojo;
    private Map<String,DataDictionaryField> dictionaryFieldsMap;


    private static TemplateRegistry templateRegistry;

    protected PMMLUnitImpl() {
        this.pmml = null;
    }

    public PMMLUnitImpl(PMML pmml) {
        this.pmml = pmml;
    }


    public PMMLUnitImpl(Resource resource) {
        try {
            JAXBContext context = JAXBContext.newInstance(PMML.class.getPackage().getName());
            Unmarshaller unmarshaller = context.createUnmarshaller();
            pmml = (PMML)unmarshaller.unmarshal(resource.getInputStream());
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getDataDictionaryPojo() {
        if (dataDictionaryPojo == null) {
        	generateDataDictionaryPojo();
        }
        return dataDictionaryPojo;
    }
    
    
    
    public String getDataDictionaryMeta() {
    	if (dataDictionaryPojo == null) {
    		generateDataDictionaryPojo();
    		
    	}
    	return null;
    }

    @Override
    public List<PMMLModel> getModels() {
        if (pmmlModels == null) {
            initModels();
        }
        return pmmlModels;
    }

    @Override
    public PMML getPMML() {
        return this.pmml;
    }
    
    public Collection<DataDictionaryField> getDictionaryFields() {
    	generateDataDictionaryFieldsMap();
    	return new ArrayList<DataDictionaryField>(dictionaryFieldsMap.values());
    }
    
    public Optional<DataDictionaryField> getDictionaryField(String fieldName) {
    	DataDictionaryField field = null;
		generateDataDictionaryFieldsMap();
		if (dictionaryFieldsMap.containsKey(fieldName)) {
			field = dictionaryFieldsMap.get(fieldName);
		}
		return field != null ? Optional.of(field):Optional.empty();
    }
    
    public Map<String, DataDictionaryField> getDictionaryFieldsMap() {
    	generateDataDictionaryFieldsMap();
    	return new HashMap<>(dictionaryFieldsMap);
    }
    
    public Map<String, DataDictionaryField> getDictionaryFieldsMap(Collection<String> fieldNames) {
    	generateDataDictionaryFieldsMap();
    	Map<String, DataDictionaryField> retMap = dictionaryFieldsMap.entrySet().stream()
    			.filter(entry -> fieldNames.contains(entry.getKey()))
    			.collect(Collectors.toMap(en -> en.getKey(), ent -> ent.getValue()));
    	return retMap;
    }
    

    private void initModels() {
        if (pmmlModels == null) {
            pmmlModels = PMMLModelFactory.getInstance().createPmmlModels(this);
        }
    }
    
    private void generateDataDictionaryMeta() {
    	TemplateRegistry registry = getTemplateRegistry();
    	
    }
    
    private void generateDataDictionaryFieldsMap() {
    	if (dictionaryFieldsMap == null) {
	        List<DataDictionaryField> dataFieldsList = pmml.getDataDictionary().getDataFields()
	                .stream().map(df -> new DataDictionaryField(df))
	                .collect(Collectors.toList());
	        dictionaryFieldsMap = dataFieldsList.stream()
	        		.collect(Collectors.toMap((DataDictionaryField ddf) -> ddf.getName(), (DataDictionaryField ddf) -> ddf));
    	}
    }
    
    private void generateDataDictionaryPojo() {
        TemplateRegistry registry = getTemplateRegistry();
        generateDataDictionaryFieldsMap();
        Map<String, Object> vars = new HashMap<>();
        vars.put("packageName",
                 "org.drools.pmml.dataObject");
        vars.put("className",
                 "PmmlDataDictionary");
        vars.put("dataFields",
                 dictionaryFieldsMap.values());
        vars.put("imports",
                 new ArrayList<>());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TemplateRuntime.execute(registry.getNamedTemplate("dataDictionaryObject"),
                                null,
                                new MapVariableResolverFactory(vars),
                                baos);
        dataDictionaryPojo = new String(baos.toByteArray());
    }
    
    private TemplateRegistry getTemplateRegistry() {
    	if (templateRegistry == null) {
    		templateRegistry = new SimpleTemplateRegistry();
    		InputStream inputStream = PMMLUnitImpl.class.getResourceAsStream(DATA_DICTIONARY_OBJ_TEMPLATE);
    		CompiledTemplate ct = TemplateCompiler.compileTemplate(inputStream);
    		templateRegistry.addNamedTemplate("dataDictionaryObject", ct);
    	}
    	return templateRegistry;
    }
}
