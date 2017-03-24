package br.com.dbm.utils.sql.parser;

import br.com.dbm.utils.sql.model.Metadata;

/**
 *
 * @author ltonietto
 */
public interface StructureParser {
    public Metadata readMetadata(String filename) throws Exception;
}
