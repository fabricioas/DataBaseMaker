package br.com.dbm.core.sql.parser;

import br.com.dbm.core.sql.parser.xml.XmlStructureParser;

/**
 *
 * @author ltonietto
 */
public class StructureParserFactory {
	public static StructureParser createInstance(String xml,EnumParser tipoParser) throws Exception {
		if( EnumParser.XML.equals(tipoParser) ){
			return new XmlStructureParser();
		}
		
		throw new IllegalArgumentException("Parser Inválido");
	}
}
