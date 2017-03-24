package br.com.dbm.utils.sql.writer;

/**
 *
 * @author ltonietto
 */
public interface DDLOutput {

    public void write(String outputFile, String commands) throws Exception;
}
