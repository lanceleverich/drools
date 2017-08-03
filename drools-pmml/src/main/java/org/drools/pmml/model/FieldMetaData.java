package org.drools.pmml.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.stream.Collectors;

public class FieldMetaData<T extends Object> {
	private Class<? extends Object> parentClass;
	private String fieldName;
	private boolean valueMissing;
	private boolean valueInvalid;
	private Collection<T> validValues;
	
	public FieldMetaData(String fieldName, Class<? extends Object> parentClass) {
		this.valueMissing = false;
		this.valueInvalid = true;
		this.fieldName = fieldName;
		this.parentClass = parentClass;
		this.validValues = null;
	}
	
	public FieldMetaData(String fieldName, Class<? extends Object> parentClass, Collection<T> validValues) {
		this.valueMissing = false;
		this.valueInvalid = true;
		this.fieldName = fieldName;
		this.parentClass = parentClass;
		this.validValues = new ArrayList<T>(validValues);
	}
	
	public FieldMetaData(String fieldName, Class<? extends Object> parentClass, T[] validValues) {
		this.valueMissing = false;
		this.valueInvalid = true;
		this.fieldName = fieldName;
		this.parentClass = parentClass;
		this.validValues = new ArrayList<T>(Arrays.asList(validValues));
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public boolean isValueMissing() {
		return valueMissing;
	}
	public void setValueMissing(boolean valueMissing) {
		this.valueMissing = valueMissing;
	}
	public boolean isValueInvalid() {
		return valueInvalid;
	}
	public void setValueInvalid(boolean valueInvalid) {
		this.valueInvalid = valueInvalid;
	}
	public Class getParentClass() {
		return this.parentClass;
	}
	public Collection<T> getValidValues() {
		return this.validValues.stream().collect(Collectors.toList());
	}
	protected void setValidValues(Collection<T> validValues) {
		this.validValues = new ArrayList<>(validValues);
	}
}
