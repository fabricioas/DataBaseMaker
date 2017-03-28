package br.com.dbm.core.sql.parser;

import java.util.List;

import br.com.dbm.core.sql.model.Metadata;

/**
 *
 * @author ltonietto
 */
public interface StructureParser {
    public List<Metadata> readMetadata(String filename) throws Exception;
}
