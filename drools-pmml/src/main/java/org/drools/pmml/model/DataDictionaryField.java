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

package org.drools.pmml.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.dmg.pmml.pmml_4_2.descr.DataField;
import org.dmg.pmml.pmml_4_2.descr.OPTYPE;
import org.dmg.pmml.pmml_4_2.descr.Value;
import org.drools.core.util.StringUtils;

public class DataDictionaryField implements java.io.Serializable {
    private static final long serialVersionUID = 19630331;

    private String type;    // name of the class for the data item
    private String name;    // name of the data item
    private OPTYPE opType;
    private Collection<String> validValues;

    public DataDictionaryField(DataField field) {
        this.type = getDataTypeForField(field).orElse(Object.class).getSimpleName();
        this.name = nonBlanksName(field.getName());
        this.opType = field.getOptype();
        if (opType == OPTYPE.CATEGORICAL
        		&& field.getValues() != null
        		&& !field.getValues().isEmpty()) {
        	validValues = field.getValues().stream()
        			.map(t -> mapValueToString.apply(t))
        			.collect(Collectors.toList());
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapitalizedName() {
        return StringUtils.ucFirst(name);
    }

    public Collection<String> getValidValues() {
    	ArrayList<String> returnValue;
    	synchronized(this.validValues) {
    		returnValue = new ArrayList<>(this.validValues);
    	}
    	return returnValue;
    }
    
    protected synchronized boolean addValidValue(String value) {
    	return this.validValues.add(value);
    }
    
    protected synchronized boolean removeValidValue(String value) {
    	return this.validValues.remove(value);
    }
    
    private Function<Value, String> mapValueToString = (value) -> {
    	return value.getValue();
    };
    
    /**
     * Removes blanks and creates camel-case version of names that consist of multiple words
     * @param name
     * @return
     */
    private String nonBlanksName(String name) {
    	String[] tokens = name.split(" ");
    	int numTokens = tokens.length;
    	if (numTokens == 1) return name;
    	StringBuilder bldr = new StringBuilder(tokens[0]);
    	for (int count = 1; count < numTokens; count++) {
    		bldr.append(StringUtils.ucFirst(tokens[count]));
    	}
    	return bldr.toString();
    }
    
    private Optional<Class<?>> getDataTypeForField(DataField field) {
        Optional<Class<?>> retVal = Optional.empty();
        switch (field.getDataType()) {
            case BOOLEAN:
                retVal = Optional.of(Boolean.class);
                break;
            case DATE:
            case DATE_TIME:
            case TIME:
                retVal = Optional.of(Date.class);
                break;
            case DOUBLE:
                retVal = Optional.of(Double.class);
                break;
            case FLOAT:
                retVal = Optional.of(Float.class);
                break;
            case INTEGER:
                retVal = Optional.of(Integer.class);
                break;
            case STRING:
                retVal = Optional.of(String.class);
                break;
            default:
                retVal = Optional.of(Object.class);
                break;
        }
        return retVal;
    }

}
