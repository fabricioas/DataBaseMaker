package br.com.dbm.utils.sql.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author ltonietto
 */
public class Metadata {

    private String tableName;
    private String tableComment;
    private String tableOwner;
    private String sequenceName;
    private String triggerName;
    private String triggerComment;
    private TableAttribute pk;
    private List<TableAttribute> fks;

    private HashMap<String, TableAttribute> attributes;

    public Metadata() {
        attributes = new HashMap();
        fks = new ArrayList();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public String getTableOwner() {
        return tableOwner;
    }

    public void setTableOwner(String tableOwner) {
        this.tableOwner = tableOwner;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerComment() {
        return triggerComment;
    }

    public void setTriggerComment(String triggerComment) {
        this.triggerComment = triggerComment;
    }

    public HashMap<String, TableAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, TableAttribute> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(TableAttribute attr) {
        attr.setAttrOrder(attributes.size() + 1);
        attributes.put(attr.getAttrName(), attr);
        Constraint constraint = attr.getConstraint();
        if (constraint != null) {
            if (constraint instanceof PrimaryKey) {
                pk = attr;
            } else if (constraint instanceof ForeignKey) {
                fks.add(attr);
            }
        }
    }

    public TableAttribute getPk() {
        return pk;
    }

    public void setPk(TableAttribute pk) {
        this.pk = pk;
    }

    public List<TableAttribute> getFks() {
        return fks;
    }

    public void setFks(List<TableAttribute> fks) {
        this.fks = fks;
    }

}
