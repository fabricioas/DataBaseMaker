package br.com.dbm.core.sql.parser.csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import br.com.dbm.core.sql.model.CheckConstraint;
import br.com.dbm.core.sql.model.Constraint;
import br.com.dbm.core.sql.model.ForeignKey;
import br.com.dbm.core.sql.model.Metadata;
import br.com.dbm.core.sql.model.TableAttribute;
import br.com.dbm.core.sql.parser.StructureParser;


/**
 *
 * @author ltonietto
 */
public class CsvStructureParser implements StructureParser {
    
    public static final int METADATA_TABLE_NAME = 1;
    public static final int METADATA_TABLE_COMMENT = 8;
    public static final int METADATA_TABLE_OWNER = 0;
    public static final int METADATA_TRIGGER_NAME = 11;
    public static final int METADATA_TRIGGER_COMMENT = 12;
    public static final int METADATA_ATTR_CONSTRAINT = 9;
    public static final int METADATA_ATTR_CONSTRAINT_VALUE = 10;
    public static final int METADATA_ATTR_NAME = 2;
    public static final int METADATA_ATTR_TYPE = 3;
    public static final int METADATA_ATTR_SIZE = 4;
    public static final int METADATA_ATTR_DECIMAL = 5;
    public static final int METADATA_ATTR_COMMENT = 6;
    public static final int METADATA_ATTR_MANDATORY = 7;

    @Override
    public List<Metadata> readMetadata(String filename) throws Exception {
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        Metadata metadata = new Metadata();
        ArrayList<Metadata> list = new ArrayList<>();
        String fields[] = line.split(";");
        metadata.setTableName(fields[1]);
        metadata.setTableComment(fields[8]);
        metadata.setTableOwner(fields[0]);
        int col = 1;
        do {
            if ((fields.length > METADATA_ATTR_CONSTRAINT_VALUE) && (fields[METADATA_ATTR_CONSTRAINT].startsWith("PK"))) {
                metadata.setSequenceName(fields[METADATA_ATTR_CONSTRAINT_VALUE]);
            }
            if (fields.length > METADATA_TRIGGER_NAME) {
                metadata.setTriggerName(fields[METADATA_TRIGGER_NAME]);
                metadata.setTriggerComment(fields[METADATA_TRIGGER_COMMENT]);
            }

            TableAttribute attr = buildAttribute(fields);
            metadata.addAttribute(attr);

            line = br.readLine();
            if (line == null) {
                break;
            }
            if ("".equals(line)) {
                continue;
            }
            fields = line.split(";");
        } while (true);
        br.close();
        list.add(metadata);
        return list;
    }

    private static TableAttribute buildAttribute(String[] fields) throws Exception {
        TableAttribute attr = new TableAttribute();
        attr.setAttrName(fields[METADATA_ATTR_NAME]);
        attr.setAttrType(fields[METADATA_ATTR_TYPE]);
        if (!"".equals(fields[METADATA_ATTR_SIZE])) {
            attr.setAttrSize(Integer.valueOf(fields[METADATA_ATTR_SIZE]));
        }
        if (!"".equals(fields[METADATA_ATTR_DECIMAL])) {
            attr.setAttrDecimalSize(Integer.valueOf(fields[METADATA_ATTR_DECIMAL]));
        }
        if (fields.length > METADATA_ATTR_CONSTRAINT) {
        	Constraint constraint = Constraint.createInstance(fields[METADATA_ATTR_CONSTRAINT]);
            String rule = fields[METADATA_ATTR_CONSTRAINT_VALUE];
            if(constraint instanceof ForeignKey){
                ForeignKey fk = (ForeignKey)constraint;
                String ref[] = rule.split(",");
                fk.setReferenceTable(ref[0]);
                fk.setReferenceAttribute(ref[1]);
            } else if (constraint instanceof CheckConstraint){
                CheckConstraint ck = (CheckConstraint) constraint;
                String vals[] = rule.split(",");
                for (String val : vals) {
                    ck.addValue(val);
                }
            }
            attr.setConstraint(constraint);
        }
        attr.setAttrComment(fields[METADATA_ATTR_COMMENT]);
        attr.setMandatory("S".equals(fields[METADATA_ATTR_MANDATORY]));
        return attr;
    }
}
