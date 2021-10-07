package csd.webscraper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import csd.webscraper.service.WebScraperService;

@Controller
public class CovidDataController {
    private WebScraperService webScraperService;

    @Autowired
    public CovidDataController(WebScraperService webScraperService) {
        this.webScraperService = webScraperService;
    }

    @GetMapping("/healthcheck")
    public ResponseEntity<String> getHealthCheck() {
        return new ResponseEntity<String>("Webscraper service is healthy", HttpStatus.OK);
    }

    @GetMapping("/scrape")
    public ResponseEntity<String> manualScrape() {
        webScraperService.scrapeData();
        return new ResponseEntity<String>("", HttpStatus.OK);
    }
}
