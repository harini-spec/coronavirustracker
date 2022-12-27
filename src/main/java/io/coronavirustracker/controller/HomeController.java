package io.coronavirustracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.coronavirustracker.models.locationStats;
import io.coronavirustracker.service.CoronaVirusService;

@Controller
public class HomeController {
	
	@Autowired
	CoronaVirusService coronaVirusService;
	
	@GetMapping("/")
	public String home(Model model) {
		List<locationStats> allStats = coronaVirusService.getAllStats();
		int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
		int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
		model.addAttribute("locationStats", allStats);
		model.addAttribute("totalReportedCases", totalReportedCases);
		model.addAttribute("totalNewCases", totalNewCases);
		return "index";
	}

}
