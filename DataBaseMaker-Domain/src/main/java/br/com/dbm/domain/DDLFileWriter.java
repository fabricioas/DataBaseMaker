package br.com.dbm.domain;

/**
 *
 * @author ltonietto
 */
public class DDLFileWriter implements DDLOutput {
	private StringBuilder commands;
	private String outputFile;
	
    @Override
    public void write(String outputFile, StringBuilder commands) throws Exception {
    		this.commands = commands;
    		this.outputFile = outputFile;
    }
    
	public StringBuilder getCommands() {
		return commands;
	}
	public String getOutputFile() {
		return outputFile;
	}
    
    

}
