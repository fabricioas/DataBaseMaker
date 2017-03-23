package br.com.fabrleadr.utils.sql.writer;

/**
 *
 * @author ltonietto
 */
public interface DDLOutput {

    public void write(String outputFile, String commands) throws Exception;
}
