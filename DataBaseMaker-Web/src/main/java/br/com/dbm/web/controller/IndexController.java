package br.com.dbm.web.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.dbm.domain.DDLOutput;
import br.com.dbm.web.api.rest.Documento;
import br.com.dbm.web.api.rest.SQLGenerator;

@Controller
public class IndexController {

	@Autowired
	private DBMSession dbmSession;
	
	@RequestMapping("/")
	@ResponseBody
	public String home() throws Exception {
		return "Hello World!";
	}

	@RequestMapping(value="/generate")
	@ResponseBody
	public String generateddl(Documento data) throws Exception {
		validateGeracaoDDL(data);
		DDLOutput output = SQLGenerator.execute(data.getXmlInput(),data.getFileName());		
		return dbmSession.addDDLOutput(output);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/download/{key}", produces = "application/octet-stream")
	@ResponseBody
	public ResponseEntity<StringBuilder> download(@PathVariable("key") String key) throws Exception {
		DDLOutput output = dbmSession.getDDLOutput(key);
		return ResponseEntity
	            .ok()
	            .contentLength(output.getCommands().length())
	            .header("Content-Disposition", "attachment; filename=\""+ output.getOutputFile() +"\"")
	            .contentType(
	                    MediaType.parseMediaType("application/octet-stream"))
	            .body(output.getCommands());
	}

	private void validateGeracaoDDL(Documento data){
		if( StringUtils.isEmpty(data.getFileName()) ){
			throw new RuntimeException("Nome do arquivo deve ser preenchudo");
		}
		if( StringUtils.isEmpty(data.getXmlInput()) ){
			throw new RuntimeException("XML deve ser preenchudo");
		}
	}
}
