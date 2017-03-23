package br.com.fabrleadr.utils.sql.parser;

import br.com.fabrleadr.utils.sql.model.Metadata;

/**
 *
 * @author ltonietto
 */
public interface StructureParser {
    public Metadata readMetadata(String filename) throws Exception;
}
