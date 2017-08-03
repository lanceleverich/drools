package org.drools.pmml.model;

import java.util.EnumSet;
import java.util.stream.Collectors;

public class EnumFieldMetaData extends FieldMetaData<String> {
	public EnumFieldMetaData(String fieldName, Class<? extends Object> parentClass, Enum<?> validValues){
		super(fieldName,parentClass);
		EnumSet<?> eset = EnumSet.allOf(validValues.getDeclaringClass());
		setValidValues(eset.stream().map(es -> es.name()).collect(Collectors.toList()));
	}
}
