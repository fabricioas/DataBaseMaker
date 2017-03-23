package br.com.fabrleadr;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.fabrleadr.utils.sql.ScriptGenerator;

@Controller
public class IndexController {

	@RequestMapping("/")
	@ResponseBody
	String home() throws Exception {
		ScriptGenerator.execute();
		return "Hello World!";
	}

}
