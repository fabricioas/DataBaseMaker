package br.com.fabrleadr.utils.sql.writer;

/**
 *
 * @author ltonietto
 */
public class DDLOutputFactory {

    public static final int OUTPUT_TYPE_FILE = 0;

    public static DDLOutput createInstance(int outputType) throws Exception {
        switch (outputType) {
            case OUTPUT_TYPE_FILE:
                return new DDLFileWriter();
            default:
                throw new IllegalArgumentException("" + outputType);
        }
    }
}
