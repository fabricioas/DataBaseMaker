package br.com.dbm.utils.sql;

import javax.swing.JOptionPane;

import br.com.dbm.utils.sql.builder.DDLBuilder;
import br.com.dbm.utils.sql.model.Metadata;
import br.com.dbm.utils.sql.parser.StructureParser;
import br.com.dbm.utils.sql.parser.StructureParserFactory;
import br.com.dbm.utils.sql.writer.DDLOutput;
import br.com.dbm.utils.sql.writer.DDLOutputFactory;

/**
 *
 * @author ltonietto
 */
public class SQLGenerator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String inputFile;
        if (args.length == 0) {
            inputFile = JOptionPane.showInputDialog("Arquivo (CSV ou XML) com metadados");
        } else {
            inputFile = args[0];
        }
        StructureParser parser = StructureParserFactory.createInstance(inputFile);
        Metadata metadata = parser.readMetadata(inputFile);
        StringBuilder commands = DDLBuilder.buildCommands(metadata);
        DDLOutput output = DDLOutputFactory.createInstance(DDLOutputFactory.OUTPUT_TYPE_FILE);
	String outputFile = inputFile.substring(0, inputFile.lastIndexOf('.'))+".sql";
        output.write(outputFile, commands.toString());
    }

}
