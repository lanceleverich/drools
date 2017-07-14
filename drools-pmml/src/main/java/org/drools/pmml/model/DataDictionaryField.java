package org.drools.pmml.model;

import java.util.Date;
import java.util.Optional;

import org.dmg.pmml.pmml_4_2.descr.DataField;

/**
 * Created by lleveric on 7/14/17.
 */
public class DataDictionaryField implements java.io.Serializable {
    private static final long serialVersionUID = 19630331;

    private String type;    // name of the class for the data item
    private String name;    // name of the data item

    public DataDictionaryField(DataField field) {
        this.type = getDataTypeForField(field).orElse(Object.class).getName();
        this.name = field.getName();
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
