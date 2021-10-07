package csd.webscraper.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import csd.webscraper.model.CovidData;
import csd.webscraper.repository.CovidDataRepository;
import csd.webscraper.utils.WebScraperUtils;
import io.github.bonigarcia.wdm.WebDriverManager;

@Service
public class WebScraperServiceImpl implements WebScraperService {
    private final static String mohUrl = "https://www.moh.gov.sg/covid-19/statistics";
    private final static String govUrl = "https://www.gov.sg/COVID-19";

    private static final Logger LOGGER = LogManager.getLogger(WebScraperServiceImpl.class);
    private CovidDataRepository covidDataRepository;

    @Autowired
    public WebScraperServiceImpl(CovidDataRepository covidDataRepository) {
        this.covidDataRepository = covidDataRepository;
    }

    @Scheduled(cron = "@midnight")
    public void scrapeData() {
        CovidData covidData = new CovidData();
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(WebScraperUtils.getChromeOptions());

        scrapeMohData(covidData, driver);
        scrapeGovData(covidData, driver);

        // covidDataRepository.save(covidData);
        System.out.println(covidData);

        LOGGER.info("------ SHUTTING DOWN SELENIUM");
        driver.quit();
    }

    public void scrapeMohData(CovidData covidData, WebDriver driver) {
        LOGGER.info("------ STARTING TO SCRAPE " + mohUrl);

        driver.get(mohUrl);
        List<WebElement> elements = driver.findElements(By.className("sfContentBlock"));

        for (int i = 0; i < elements.size(); i++) {
            try {
                WebElement element = elements.get(i);
                String[] cellData = element.getText().split("\r\n|\n");

                String header = cellData[0].trim();
                int value = Integer.parseInt(cellData[1].replaceAll("~|,", ""));

                if (WebScraperUtils.isCovidData(header)) {
                    WebScraperUtils.updateModel(covidData, header, value);
                }
            } catch (IndexOutOfBoundsException e) {
                // Consume error for data that we do not wish to store
            } catch (NumberFormatException e) {
                // Consume error for data that we do not wish to store
            }
        }

        LOGGER.info("------ SUCCESSFULLY SCRAPED " + mohUrl);
    }


    public void scrapeGovData(CovidData covidData, WebDriver driver) {
        LOGGER.info("------ STARTING TO SCRAPE " + govUrl);

        driver.get(govUrl);

        WebElement vaccineData = driver.findElement(By.id("vaccinedata"));
        WebElement vaccineDataSibling = vaccineData.findElement(By.xpath("following-sibling::*"));
        List<WebElement> vaccineDataElements = vaccineDataSibling.findElements(By.tagName("td"));

        WebElement caseSummary = driver.findElement(By.id("casesummary"));
        WebElement caseSummarySibling = caseSummary.findElement(By.xpath("following-sibling::*"));
        List<WebElement> caseSummaryElements = caseSummarySibling.findElements(By.tagName("td"));

        // System.out.println(vaccineDataElements.get(0).getText());
        // System.out.println(vaccineDataElements.get(2).getText());
        // System.out.println(vaccineDataElements.get(1).getText());
        // System.out.println(vaccineDataElements.get(3).getText().split("\n")[0]);
        // System.out.println(vaccineDataElements.get(4).getText());
        // System.out.println(vaccineDataElements.get(5).getText().split("\n")[0]);

        try {
            System.out.println(vaccineDataElements.get(2).getText() + " | " + vaccineDataElements.get(3).getText().split("\r\n|\s")[0]);
            WebScraperUtils.updateModel(covidData, vaccineDataElements.get(0).getText(), Integer.parseInt(vaccineDataElements.get(2).getText()));
            WebScraperUtils.updateModel(covidData, vaccineDataElements.get(1).getText(), Integer.parseInt(vaccineDataElements.get(3).getText().split("\r\n")[0]));
            WebScraperUtils.updateModel(covidData, vaccineDataElements.get(4).getText(), Integer.parseInt(vaccineDataElements.get(5).getText().split("\r\n")[0]));
            WebScraperUtils.updateModel(covidData, caseSummaryElements.get(0).getText(), Integer.parseInt(caseSummaryElements.get(2).getText()));
            WebScraperUtils.updateModel(covidData, caseSummaryElements.get(1).getText(), Integer.parseInt(caseSummaryElements.get(3).getText()));
            WebScraperUtils.updateModel(covidData, caseSummaryElements.get(4).getText(), Integer.parseInt(caseSummaryElements.get(5).getText()));
        }  catch (IndexOutOfBoundsException e) {
            // Consume error for data that we do not wish to store
        } catch (NumberFormatException e) {
            // Consume error for data that we do not wish to store
        }

        LOGGER.info("------ SUCCESSFULLY SCRAPED " + govUrl);
    }
}
