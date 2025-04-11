package com.vm.insight;

import java.net.InetAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VMInfoController {

	@Value("${git.commit.id.abbrev:unknown}")
	private String commitHash;

	@GetMapping("/")
	public String index(Model model) {
		String hostname = "unknown";
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			// Handle error
		}

		model.addAttribute("hostname", hostname);
		model.addAttribute("commitHash", commitHash);
		return "index";
	}
}
