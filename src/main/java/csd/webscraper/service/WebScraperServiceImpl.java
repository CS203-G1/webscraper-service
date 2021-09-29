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
import csd.webscraper.repository.CovidDataRepostiory;
import csd.webscraper.utils.WebScraperUtils;
import io.github.bonigarcia.wdm.WebDriverManager;

@Service
public class WebScraperServiceImpl implements WebScraperService {
    @Value("${app.url}")
    private String url;

    private static final Logger LOGGER = LogManager.getLogger(WebScraperServiceImpl.class);
    private CovidDataRepostiory covidDataRepostiory;

    @Autowired
    public WebScraperServiceImpl(CovidDataRepostiory covidDataRepostiory) {
        this.covidDataRepostiory = covidDataRepostiory;
    }

    @Scheduled(fixedDelay = 30000)
    public void scrapeData() {
        LOGGER.info("------ STARTING TO SCRAPE " + url);

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(WebScraperUtils.getChromeOptions());

        driver.get(url);
        List<WebElement> elements = driver.findElements(By.className("sfContentBlock"));
        CovidData covidData = new CovidData();

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
        covidDataRepostiory.save(covidData);
        LOGGER.info("------ SUCCESSFULLY SCRAPED DATA");
        LOGGER.info("------ SHUTTING DOWN SELENIUM");
        driver.quit();
    }
}
