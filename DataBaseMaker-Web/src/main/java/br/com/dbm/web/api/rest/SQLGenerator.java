package br.com.dbm.web.api.rest;

import br.com.dbm.core.sql.builder.DDLBuilder;
import br.com.dbm.core.sql.model.Metadata;
import br.com.dbm.core.sql.parser.EnumParser;
import br.com.dbm.core.sql.parser.StructureParser;
import br.com.dbm.core.sql.parser.StructureParserFactory;
import br.com.dbm.core.sql.writer.DDLOutputFactory;
import br.com.dbm.domain.DDLOutput;

/**
 *
 * @author ltonietto
 */
public class SQLGenerator {

    /**
     * @param args the command line arguments
     */
    public static DDLOutput execute(String xml,String fileName) throws Exception {
        StructureParser parser = StructureParserFactory.createInstance(xml,EnumParser.XML);
        Metadata metadata = parser.readMetadata(xml);
        StringBuilder commands = DDLBuilder.buildCommands(metadata);
        DDLOutput output = DDLOutputFactory.createInstance(DDLOutputFactory.OUTPUT_TYPE_FILE);
        output.write(fileName, commands);
        return output;
    }

}
