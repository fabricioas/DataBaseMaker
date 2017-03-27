package br.com.dbm.core.sql.parser;

import br.com.dbm.core.sql.model.Metadata;

/**
 *
 * @author ltonietto
 */
public interface StructureParser {
    public Metadata readMetadata(String filename) throws Exception;
}
