package br.com.dbm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

	@RequestMapping("/")
	@ResponseBody
	String home() throws Exception {
		return "Hello World!";
	}

}
