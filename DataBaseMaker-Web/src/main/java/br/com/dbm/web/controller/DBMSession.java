package br.com.dbm.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import br.com.dbm.domain.DDLOutput;

@Component
@SessionScope
public class DBMSession {
	private Map<String,DDLOutput> mapDDLOutput = new HashMap<>();
	
	
	public String addDDLOutput(DDLOutput ddlOutput){
		String key = new Integer(ddlOutput.hashCode()).toString();
		mapDDLOutput.put(key, ddlOutput);
		return key;
	}
	
	public DDLOutput getDDLOutput(String key){
		return mapDDLOutput.remove(key);
	}
}
