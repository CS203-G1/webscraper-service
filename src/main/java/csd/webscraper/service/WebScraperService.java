package csd.webscraper.service;

import org.openqa.selenium.WebDriver;

import csd.webscraper.model.CovidData;

public interface WebScraperService {
    void scrapeData();
    void scrapeMohData(CovidData covidData, WebDriver driver);
    void scrapeGovData(CovidData covidData, WebDriver driver);
    void scrapeCaseData(CovidData covidData, WebDriver driver);
}
