package com.vm.insight;

import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
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

		String containerHostname = "unknown";
		try {
			containerHostname = InetAddress.getLocalHost().getHostName();
		} catch (Exception ignored) {
		}

		String vmHostname = "unknown";
		try {
			vmHostname = Files.readString(Path.of("/vm_hostname")).trim();
		} catch (Exception e) {
		}

		model.addAttribute("containerHostname", containerHostname);
		model.addAttribute("vmHostname", vmHostname);
		model.addAttribute("commitHash", commitHash);
		return "index";
	}
}
