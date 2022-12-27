package io.coronavirustracker.service;


import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.coronavirustracker.models.locationStats;
import jakarta.annotation.PostConstruct;

@Service
public class CoronaVirusService {
	
	private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

	public List<locationStats> getAllStats() {
		return allStats;
	}

	private List<locationStats> allStats = new ArrayList<>();
	
	@PostConstruct
	@Scheduled(cron = "* * 1 * * *")
	public void fetch_data() throws IOException, InterruptedException {
		
		List<locationStats> newStats = new ArrayList<>();
		
		HttpClient client =  HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(VIRUS_DATA_URL))
				.build();
		HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
//		System.out.println(httpResponse.body());
		StringReader input = new StringReader(httpResponse.body());
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(input);
		for (CSVRecord record : records) {
			locationStats locationStat = new locationStats();
			locationStat.setState(record.get("Province/State"));
			locationStat.setCountry(record.get("Country/Region"));
			int totalCases = Integer.parseInt(record.get(record.size()-1));
			int prevCases  = Integer.parseInt(record.get(record.size()-2));
		    locationStat.setLatestTotalCases(totalCases);
		    locationStat.setDiffFromPrevDay(totalCases - prevCases);
		    newStats.add(locationStat);
		}
		this.allStats = newStats;
	}

}
