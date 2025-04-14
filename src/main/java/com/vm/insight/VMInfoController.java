package com.vm.insight;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
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

		// Extract the VM's IP address (for Host-Only Network)
		String vmIpAddress = "unknown";
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaces.nextElement();

				// Check if the interface is likely to be the Host-Only Network interface
				// Adjust this based on your VM software and specific network names, e.g., "vboxnet0" for VirtualBox
				if (networkInterface.getName().startsWith("vboxnet") || networkInterface.getName().startsWith("ens")) {
					Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
					while (inetAddresses.hasMoreElements()) {
						InetAddress inetAddress = inetAddresses.nextElement();
						// Check if it is a valid, non-loopback address
						if (!inetAddress.isLoopbackAddress() && inetAddress.isSiteLocalAddress()) {
							vmIpAddress = inetAddress.getHostAddress();
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			// Handle the error, possibly log it
		}

		model.addAttribute("containerHostname", containerHostname);
		model.addAttribute("vmHostname", vmHostname);
		model.addAttribute("commitHash", commitHash);
		model.addAttribute("vmIpAddress", vmIpAddress);  // Add the IP address to the model
		return "index";
	}
}
