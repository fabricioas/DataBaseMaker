package br.com.dbm.domain;

/**
 *
 * @author ltonietto
 */
public interface DDLOutput {

    void write(String outputFile, StringBuilder commands) throws Exception;
    
	StringBuilder getCommands();
	String getOutputFile();

}
