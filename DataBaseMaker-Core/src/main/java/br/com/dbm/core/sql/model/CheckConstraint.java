package br.com.dbm.core.sql.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ltonietto
 */
public class CheckConstraint extends Constraint {

    private List<String> values;

    public CheckConstraint(String name) {
        super(name);
        values = new ArrayList();
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public void addValue(String value) {
        values.add(value);
    }

    public String buildListOfValues() {
        StringBuilder listOfValues = new StringBuilder();
        String stringDelimiter = "";
        String aux = "";
        for (String value : values) {
            if (!value.contains("'")) {
                stringDelimiter = "'";
            }
            listOfValues.append(aux).append(stringDelimiter).append(value).append(stringDelimiter);
            aux = ", ";
        }
        return listOfValues.toString();
    }
}
