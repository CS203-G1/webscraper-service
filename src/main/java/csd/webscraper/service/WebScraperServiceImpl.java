package csd.webscraper.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import csd.webscraper.exception.WebElementNotFoundException;
import csd.webscraper.model.CovidData;
import csd.webscraper.repository.CovidDataRepository;
import csd.webscraper.utils.WebScraperUtils;
import io.github.bonigarcia.wdm.WebDriverManager;

@Service
public class WebScraperServiceImpl implements WebScraperService {
    private final static String MOH_URL = "https://www.moh.gov.sg/covid-19/statistics";
    private final static String GOV_URL = "https://www.gov.sg/COVID-19";
    private final static String CASE_URL = "https://www.worldometers.info/coronavirus/country/singapore/";

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

        // scrapeMohData(covidData, driver);
        scrapeGovData(covidData, driver);
        // scrapeCaseData(covidData, driver);

        // covidDataRepository.save(covidData);
        LOGGER.info("------ SAVED MODEL IN DB");

        LOGGER.info("------ SHUTTING DOWN SELENIUM");
        driver.quit();
    }

    public void scrapeMohData(CovidData covidData, WebDriver driver) {
        LOGGER.info("------ STARTING TO SCRAPE " + MOH_URL);

        driver.get(MOH_URL);
        
        try {
            String header = driver.findElement(By.xpath("//*[@id=\"ContentPlaceHolder_contentPlaceholder_C030_Col00\"]/div/div/table/tbody/tr[1]/td")).getText();
            String value = driver.findElement(By.xpath("//*[@id=\"ContentPlaceHolder_contentPlaceholder_C030_Col00\"]/div/div/table/tbody/tr[2]/td")).getText();

            if (WebScraperUtils.isCovidData(header)) {
                WebScraperUtils.updateModel(covidData, header, Integer.parseInt(value));
            }
        } catch (WebElementNotFoundException e) {
            // LOGGER.warn(e.getMessage());
            // Consume the error here since it's lazy rendering
        } catch (IndexOutOfBoundsException e) {
            // Consume error for data that we do not wish to store
        } catch (NumberFormatException e) {
            // Consume error for data that we do not wish to store
        } catch (Exception e) {
            LOGGER.warn("[UNEXPECTED ERROR]: " + e.getMessage());
        }

        LOGGER.info("------ SUCCESSFULLY SCRAPED " + MOH_URL);
    }

    public void scrapeGovData(CovidData covidData, WebDriver driver) {
        /**
         * Data returned from localCases:
         * 1. Total new cases
         * 2. <numOfNewCases>
         * 3. Community
         * 4. Dormitory
         * 5. Imported
         * 6. <numOfCommunity>
         * 7. <numOfDormitory>
         * 8. <numOfImported>
         * 9. Hospitalised
         * 10. Requires oxygen supplementation
         * 11. <numOfHospitaled>
         * 12. <numOfRequireOxygen>
         * 13. In Intensive Care Unit
         * 14. Number of deaths^
         * 15. <numOfICU>
         * 16. <numOfDeaths>
         */
        LOGGER.info("------ STARTING TO SCRAPE " + GOV_URL);

        driver.get(GOV_URL);

        WebElement localCases = driver.findElement(By.id("localcases"));
        List<WebElement> localCasesTableData = localCases.findElements(By.tagName("td"));

        // Remove redunant last element inside table
        // (i.e. Excludes deaths unrelated to COVID-19 complications)
        localCasesTableData.remove(localCasesTableData.size() - 1);

        // Convert WebElements into list of headers and values to store in db
        List<String> headers = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        for (WebElement element: localCasesTableData) {
            String currentElement = element.getText().strip().replace(",", "");
            try {
                values.add(Integer.parseInt(currentElement));
            } catch (NumberFormatException e) {
                // If element can't be parsed, it's considered a header
                headers.add(currentElement);
            }
        }

        try {
            for (int i = 0; i < headers.size(); i++) {
                String header = headers.get(i);
                if (WebScraperUtils.isCovidData(header)) {
                    WebScraperUtils.updateModel(covidData, header, values.get(i));
                }
            }
        } catch (WebElementNotFoundException e) {
            LOGGER.warn(e.getMessage());
        } catch (Exception e) {
            LOGGER.warn("[UNEXPECTED ERROR]: " + e.getMessage());
        }

        // Clear array list instead of instantiating new one
        headers.clear();
        values.clear();

        WebElement vaccineData = driver.findElement(By.id("vaccinedata"));
        WebElement vaccineDataSibling = vaccineData.findElement(By.xpath("following-sibling::*"));
        List<WebElement> vaccineDataTableData = vaccineDataSibling.findElements(By.tagName("td"));

        for (WebElement element: vaccineDataTableData) {
            String currentElement = element.getText().strip().replace(",", "");
            
            // Corner case where some table data contains (i.e. 85% of population)
            if (currentElement.contains("% of population")) {
                currentElement = currentElement.split("\s|\n|\r")[0];
            }
            System.out.println("BEFORE: " + element.getText() + " | AFTER:"+ currentElement + "---");

            try {
                values.add(Integer.parseInt(currentElement));
            } catch (NumberFormatException e) {
                // If element can't be parsed, it's considered a header
                System.out.println("INT ERROR: " + currentElement);
                headers.add(currentElement);
            }
        }
        System.out.println("-------------------");
        for (int i = 0; i < values.size(); i++) {
            System.out.println(values.get(i) + " | " + headers.get(i));
        }

        try {
            
        } catch (IndexOutOfBoundsException e) {
            // Consume error for data that we do not wish to store
        } catch (NumberFormatException e) {
            // Consume error for data that we do not wish to store
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }

        LOGGER.info("------ SUCCESSFULLY SCRAPED " + GOV_URL);
    }

    public void scrapeCaseData(CovidData covidData, WebDriver driver) {
        LOGGER.info("------ STARTING TO SCRAPE " + CASE_URL);

        driver.get(CASE_URL);

        List<WebElement> elements = driver.findElements(By.className("maincounter-number"));

        try {
            WebScraperUtils.updateModel(covidData, "Total covid cases", Integer.parseInt(elements.get(0).getText().replace(",", "")));
            WebScraperUtils.updateModel(covidData, "Total deaths", Integer.parseInt(elements.get(1).getText().replace(",", "")));
            WebScraperUtils.updateModel(covidData, "Total recovered", Integer.parseInt(elements.get(2).getText().replace(",", "")));
        } catch (IndexOutOfBoundsException e) {
            // Consume error for data that we do not wish to store
        } catch (NumberFormatException e) {
            // Consume error for data that we do not wish to store
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }

        LOGGER.info("------ SUCCESSFULLY SCRAPED " + CASE_URL);
    }
}
