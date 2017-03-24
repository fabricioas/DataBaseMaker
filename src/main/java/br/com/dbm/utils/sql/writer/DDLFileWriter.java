package br.com.dbm.utils.sql.writer;

import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author ltonietto
 */
public class DDLFileWriter implements DDLOutput {

    @Override
    public void write(String outputFile, String commands) throws Exception {
        FileWriter fw = new FileWriter(outputFile);
        PrintWriter pw = new PrintWriter(fw);
        pw.println(commands);
        pw.close();
    }

}
