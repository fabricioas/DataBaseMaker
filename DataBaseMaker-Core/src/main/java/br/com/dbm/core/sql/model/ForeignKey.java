package br.com.dbm.core.sql.model;

/**
 *
 * @author ltonietto
 */
public class ForeignKey extends Constraint {
    
    private String referenceTable;
    private String referenceAttribute;

    public ForeignKey(String name) {
        super(name);
    }
    
    public String getReferenceTable() {
        return referenceTable;
    }

    public void setReferenceTable(String referenceTable) {
        this.referenceTable = referenceTable;
    }

    public String getReferenceAttribute() {
        return referenceAttribute;
    }

    public void setReferenceAttribute(String referenceAttribute) {
        this.referenceAttribute = referenceAttribute;
    }

}
