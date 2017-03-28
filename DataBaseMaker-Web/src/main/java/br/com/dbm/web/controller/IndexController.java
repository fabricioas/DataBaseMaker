package br.com.dbm.web.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.dbm.domain.DDLOutput;
import br.com.dbm.web.api.rest.Documento;
import br.com.dbm.web.api.rest.SQLGenerator;

@Controller
public class IndexController {

	@RequestMapping("/")
	@ResponseBody
	public String home() throws Exception {
		return "Hello World!";
	}

	@RequestMapping(value="/generate", produces = "application/octet-stream")
	@ResponseBody
	public ResponseEntity<StringBuilder> generateddl(Documento data) throws Exception {
//		validateGeracaoDDL(data);
		DDLOutput output = SQLGenerator.execute(data.getXmlInput(),data.getFileName());
		return ResponseEntity
	            .ok()
	            .contentLength(output.getCommands().length())
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
