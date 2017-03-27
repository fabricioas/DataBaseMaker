package br.com.dbm.core.sql.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import br.com.dbm.core.sql.model.CheckConstraint;
import br.com.dbm.core.sql.model.ForeignKey;
import br.com.dbm.core.sql.model.Metadata;
import br.com.dbm.core.sql.model.TableAttribute;

/**
 *
 * @author ltonietto
 */
public class DDLBuilder {

    public static StringBuilder buildCommands(Metadata metadata) throws Exception {
        StringBuilder commands = new StringBuilder();
        commands.append(buildSequence(metadata)).append("\n");
        commands.append(buildTable(metadata)).append("\n");
        commands.append(buildTableComment(metadata.getTableOwner(), metadata.getTableName(), metadata.getTableComment())).append("\n");
        commands.append(buildPK(metadata)).append("\n");
        commands.append(buildFKs(metadata)).append("\n");
        Collection<TableAttribute> attrs = metadata.getAttributes().values();
        List<TableAttribute> allAttrs = new ArrayList(attrs);
        Collections.sort(allAttrs);
        for (TableAttribute attr : allAttrs) {
            commands.append(buildAttributeComment(metadata.getTableOwner(), metadata.getTableName(), attr.getAttrName(), attr.getAttrComment())).append("\n");
        }
        commands.append(buildGrantsAndSynonyms(metadata)).append("\n");
        if (metadata.getTriggerName() != null) {
            commands.append(buildTrigger(metadata)).append("\n");
        }

        return commands;
    }

    public static StringBuilder buildTable(Metadata metadata) {
//      CREATE TABLE USER_OWNER.TABELA (
//           OID_BLOCO            NUMBER(38) NOT NULL,
//           NOM_BLOCO            VARCHAR2(128) NOT NULL,
//           FLG_EXCLUIR          VARCHAR2(1) NOT NULL  CONSTRAINT CK_BLO_FLGEXCLUIR CHECK(FLG_EXCLUIR IN('N', 'S')),
//             FLG_REPETIDO VARCHAR2 (1) NOT NULL,
//           COD_VALOR_BRMS       VARCHAR2(512) NULL ,
//             COD_CHAVE_BRMS VARCHAR2(128) NULL ,
//             OID_FORMULARIO NUMBER(38) NULL ,
//             NUM_ORDEM NUMBER(10) NOT NULL 
//      );
        StringBuilder command = new StringBuilder("CREATE TABLE ");
        command.append(metadata.getTableOwner()).append(".").append(metadata.getTableName()).append("(\n");
        Collection<TableAttribute> attrs = metadata.getAttributes().values();
        List<TableAttribute> allAttrs = new ArrayList(attrs);
        Collections.sort(allAttrs);
        String aux = "";
        for (TableAttribute attr : allAttrs) {
            command.append(aux)
                    .append('\t').append(attr.getAttrName())
                    .append("                              ".substring(attr.getAttrName().length() - 1))
                    .append(buildAttrType(attr));
            aux = ",\n";
        }
        command.append("\n);\n");
        return command;
    }

    public static StringBuilder buildAttrType(TableAttribute attr) {
        StringBuilder attrType = new StringBuilder(attr.getAttrType());
        if (attr.getAttrSize() != null) {
            attrType.append('(').append(attr.getAttrSize());
            if (attr.getAttrDecimalSize() != null) {
                attrType.append(',').append(attr.getAttrDecimalSize());
            }
            attrType.append(')');
        }
        if (attr.getDefaultValue() != null) {
//                 DEFAULT 'S' 
            attrType.append(" DEFAULT '").append(attr.getDefaultValue()).append("' ");
        }
        if (attr.getMandatory()) {
            attrType.append(" NOT NULL");
        }
        if (attr.hasInlineConstraint()) {
            // CONSTRAINT CK_NAME CHECK (NOME_ATRIBUTO IN ('S', 'N'))
            CheckConstraint ck = (CheckConstraint) attr.getConstraint();
            attrType.append(" CONSTRAINT ").append(ck.getName()).append(" CHECK (")
                    .append(attr.getAttrName())
                    .append(" IN (").append(ck.buildListOfValues()).append("))");
        }

        return attrType;
    }

    public static StringBuilder buildTableComment(String objectOwner, String objectName, String objectComment) throws Exception {
        StringBuilder command = new StringBuilder("COMMENT ON TABLE ");
        command.append(objectOwner).append('.').append(objectName).append(" IS '").append(objectComment).append("';\n");
        return command;
    }

    public static StringBuilder buildAttributeComment(String objectOwner, String objectName, String attrName, String objectComment) throws Exception {
        StringBuilder command = new StringBuilder("COMMENT ON COLUMN ");
        command.append(objectOwner).append('.').append(objectName).append('.').append(attrName).append(" IS '").append(objectComment).append("';\n");
        return command;
    }

    public static StringBuilder buildSequence(Metadata metadata) throws Exception {
//        CREATE SEQUENCE USER_OWNER.NOME_SEQUENCE
//          INCREMENT BY 1
//          START WITH 1
//          NOMAXVALUE
//          NOMINVALUE
//          NOCYCLE
//          NOCACHE
//          NOORDER;
        StringBuilder command = new StringBuilder("CREATE SEQUENCE ");
        command.append(metadata.getTableOwner()).append('.').append(metadata.getSequenceName())
                .append("\n\tINCREMENT BY 1\n")
                .append("\tSTART WITH 1\n")
                .append("\tNOMAXVALUE\n")
                .append("\tNOMINVALUE\n")
                .append("\tNOCYCLE\n")
                .append("\tNOCACHE\n")
                .append("\tNOORDER;\n");
        ;
        return command;
    }

    public static StringBuilder buildPK(Metadata metadata) throws Exception {
        /*
         CREATE UNIQUE INDEX USER_OWNER.PK_TABELA ON USER_OWNER.TABELA
         (CHAVE_PRIMARIA   ASC);

         ALTER TABLE USER_OWNER.TABELA
         ADD CONSTRAINT PK_TABELA PRIMARY KEY (CHAVE_PRIMARIA);
         */
        StringBuilder command = new StringBuilder("CREATE UNIQUE INDEX ");
        TableAttribute pk = metadata.getPk();
        command.append(metadata.getTableOwner()).append('.').append(pk.getConstraint().getName()).append(" ON ")
                .append(metadata.getTableOwner()).append('.').append(metadata.getTableName()).append(" (")
                .append(pk.getAttrName()).append(" ASC);\n\n");

        command.append("ALTER TABLE ").append(metadata.getTableOwner()).append('.').append(metadata.getTableName())
                .append(" ADD CONSTRAINT ").append(pk.getConstraint().getName()).append(" PRIMARY KEY (").append(pk.getAttrName()).append(");\n");
        ;

        return command;
    }

    public static StringBuilder buildGrantsAndSynonyms(Metadata metadata) {
        String targetUser = metadata.getTableOwner().toUpperCase().replaceFirst("OWNER", "RUN");
        StringBuilder command = new StringBuilder();

//      CREATE OR REPLACE SYNONYM USER_RUN.NOME_SEQUENCE FOR USER_OWNER.NOME_SEQUENCE;
//      CREATE OR REPLACE SYNONYM USER_RUN.TABELA FOR USER_OWNER.TABELA;
        command.append("CREATE OR REPLACE SYNONYM ").append(targetUser).append('.').append(metadata.getSequenceName())
                .append(" FOR ").append(metadata.getTableOwner()).append('.').append(metadata.getSequenceName()).append(";\n\n");
        command.append("CREATE OR REPLACE SYNONYM ").append(targetUser).append('.').append(metadata.getTableName())
                .append(" FOR ").append(metadata.getTableOwner()).append('.').append(metadata.getTableName()).append(";\n\n");

//      GRANT SELECT ON USER_OWNER.NOME_SEQUENCE TO USER_RUN;
//      GRANT SELECT, INSERT, UPDATE, DELETE ON USER_OWNER.TABELA TO USER_RUN;
        command.append("GRANT SELECT ON ")
                .append(metadata.getTableOwner()).append('.').append(metadata.getSequenceName())
                .append(" TO ").append(targetUser).append(";\n\n");
        command.append("GRANT SELECT, INSERT, UPDATE, DELETE ON ").append(metadata.getTableOwner()).append('.').append(metadata.getTableName())
                .append(" TO ").append(targetUser).append(";\n\n");

        return command;
    }

    public static StringBuilder buildTrigger(Metadata metadata) {
        StringBuilder command = new StringBuilder("CREATE OR REPLACE TRIGGER ");
//        CREATE OR REPLACE TRIGGER USER_OWNER.NOME_TRIGGER
//        BEFORE INSERT
//        ON USER_OWNER.TABELA
//
//        for each row
//              BEGIN
//                      IF :NEW.CHAVE_PRIMARIA IS NULL THEN
//                      SELECT USER_OWNER.NOME_SEQUENCE.NEXTVAL
//                           INTO :NEW.CHAVE_PRIMARIA FROM DUAL;
//              END IF;
//        END;
//        /
        command.append(metadata.getTableOwner()).append('.').append(metadata.getTriggerName());
        command.append("\n\tBEFORE INSERT\n\tON ").append(metadata.getTableOwner()).append('.').append(metadata.getTableName());
        TableAttribute pk = metadata.getPk();
        command.append("\n\tfor each row\n\t\tBEGIN\n\t\tIF :NEW.").append(pk.getAttrName()).append(" IS NULL THEN");
        command.append("\n\t\t\tSELECT ").append(metadata.getTableOwner()).append('.').append(metadata.getSequenceName()).append(".NEXTVAL");
        command.append("\n\t\t\t\tINTO :NEW.").append(pk.getAttrName()).append(" FROM DUAL;");
        command.append("\n\tEND IF;\nEND;\n/\n");
        return command;
    }

    private static StringBuilder buildFKs(Metadata metadata) {
        StringBuilder command = new StringBuilder();
//      ALTER TABLE FORMALISTICA_CONSORCIO_OWNER.PROCESSO_CONTEMPLACAO_VISTORIA
//         ADD (CONSTRAINT FK_PCMVIS_PRCCMP FOREIGN KEY (OID_PROCESSO_CONTEMPLACAO) 
//         REFERENCES FORMALISTICA_CONSORCIO_OWNER.PROCESSO_CONTEMPLACAO (OID_PROCESSO_CONTEMPLACAO) ON DELETE CASCADE);
        List<TableAttribute> fks = metadata.getFks();
        for (TableAttribute attr : fks) {
            ForeignKey fk = (ForeignKey) attr.getConstraint();
            command.append("ALTER TABLE ");
            command.append(metadata.getTableOwner()).append(".").append(metadata.getTableName())
                    .append(" ADD (CONSTRAINT ").append(fk.getName()).append(" FOREIGN KEY (")
                    .append(attr.getAttrName()).append(") REFERENCES ").append(metadata.getTableOwner())
                    .append('.').append(fk.getReferenceTable()).append('(').append(fk.getReferenceAttribute()).append(") ON DELETE CASCADE);\n\n");
        }
        return command;
    }

}
