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
import csd.webscraper.utils.UrlUtils;
import csd.webscraper.utils.WebScraperUtils;
import io.github.bonigarcia.wdm.WebDriverManager;

@Service
public class WebScraperServiceImpl implements WebScraperService {
    private static final Logger LOGGER = LogManager.getLogger(WebScraperServiceImpl.class);
    private CovidDataRepository covidDataRepository;

    @Autowired
    public WebScraperServiceImpl(CovidDataRepository covidDataRepository) {
        this.covidDataRepository = covidDataRepository;
    }

    @Override
    @Scheduled(cron = "@midnight")
    public void scrapeData() {
        CovidData covidData = new CovidData();
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(WebScraperUtils.getChromeOptions());

        scrapeMohData(covidData, driver);
        scrapeGovData(covidData, driver);
        scrapeCaseData(covidData, driver);
        scrapePopulationData(covidData, driver);

        System.out.println(covidData);

        covidDataRepository.save(covidData);
        LOGGER.info("------ SAVED MODEL IN DB");

        LOGGER.info("------ SHUTTING DOWN SELENIUM");
        driver.quit();
    }

    /**
     * This method only scrapes for the following data:
     * 1. Total swabs tested
     * 
     * @param covidData Model that contains the scraped data that will be stored in db
     * @param driver Selenium webdriver that is used to scrape data
     */
    @Override
    public void scrapeMohData(CovidData covidData, WebDriver driver) {
        LOGGER.info("------ STARTING TO SCRAPE " + UrlUtils.getMohUrl());

        driver.get(UrlUtils.getMohUrl());
        
        try {
            String header = driver.findElement(By.xpath("//*[@id=\"ContentPlaceHolder_contentPlaceholder_C030_Col00\"]/div/div/table/tbody/tr[1]/td/span/strong")).getText();
            String value = driver.findElement(By.xpath("//*[@id=\"ContentPlaceHolder_contentPlaceholder_C030_Col00\"]/div/div/table/tbody/tr[2]/td/strong/span/strong")).getText();

            if (WebScraperUtils.isCovidData(header)) {
                WebScraperUtils.updateModel(covidData, header, Integer.parseInt(value.replace(",", "")));
            }
        } catch (WebElementNotFoundException e) {
            LOGGER.warn("------ " + e.getMessage());
        } catch (NumberFormatException e) {
            // Consume error for data that we do not wish to store
        } catch (Exception e) {
            LOGGER.warn("------ UNEXPECTED ERROR: " + e.getMessage());
        }

        LOGGER.info("------ SUCCESSFULLY SCRAPED " + UrlUtils.getMohUrl());
    }

    /**
     * This method scrapes the following data:
     * 1. New cases
     * 2. New community cases
     * 3. New dormitory cases
     * 4. New imported cases
     * 5. Total number of hospitalised
     * 6. Total number of people that require oxygen supplementation
     * 7. Total number of people that are in Intensive Care Unit
     * 8. Total number of deaths
     * 9. Total number of doses administrated
     * 10. Total number of people who received at least one dose
     * 11. Total number of people that completed full regime
     * 
     * @param covidData Model that contains the scraped data that will be stored in db
     * @param driver Selenium webdriver that is used to scrape data
     */
    @Override
    public void scrapeGovData(CovidData covidData, WebDriver driver) {
        LOGGER.info("------ STARTING TO SCRAPE " + UrlUtils.getGovUrl());

        driver.get(UrlUtils.getGovUrl());

        // Convert WebElements into list of headers and values to store in db
        List<String> headers = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        updateLocalCasesData(driver, headers, values);
        updateVaccineData(driver, headers, values);

        for (int i = 0; i < headers.size(); i++) {
            try {
                String header = headers.get(i);
                if (WebScraperUtils.isCovidData(header)) {
                    WebScraperUtils.updateModel(covidData, header, values.get(i));
                }
            } catch(WebElementNotFoundException e) {
                LOGGER.warn("------ " + e.getMessage());
            } catch (Exception e) {
                LOGGER.warn("------ UNEXPECTED ERROR: " + e.getMessage());
            }
        }

        LOGGER.info("------ SUCCESSFULLY SCRAPED " + UrlUtils.getGovUrl());
    }

    public void updateLocalCasesData(WebDriver driver, List<String> headers, List<Integer> values) {
        WebElement localCases = driver.findElement(By.id("localcases"));
        List<WebElement> localCasesTableData = localCases.findElements(By.tagName("td"));

        // Remove redunant element inside table
        // (i.e. Excludes deaths unrelated to COVID-19 complications)
        localCasesTableData.remove(localCasesTableData.size() - 1);

        for (WebElement element: localCasesTableData) {
            String currentElement = element.getText().strip().replace(",", "");
            try {
                values.add(Integer.parseInt(currentElement));
            } catch (NumberFormatException e) {     // If element can't be parsed, it's considered a header
                headers.add(currentElement);
            }
        }
    }

    public void updateVaccineData(WebDriver driver, List<String> headers, List<Integer> values) {
        WebElement vaccineData = driver.findElement(By.id("vaccinedata"));
        WebElement vaccineDataSibling = vaccineData.findElement(By.xpath("following-sibling::*"));
        List<WebElement> vaccineDataTableData = vaccineDataSibling.findElements(By.tagName("td"));

        for (WebElement element: vaccineDataTableData) {
            String currentElement = element.getText().strip().replace(",", "");
            
            // Corner case where some table data contains (i.e. 85% of population)
            if (currentElement.contains("% of population")) {
                currentElement = currentElement.split("\s|\n|\r")[0];
            }

            try {
                values.add(Integer.parseInt(currentElement));
            } catch (NumberFormatException e) {     // If element can't be parsed, it's considered a header
                headers.add(currentElement);
            }
        }
    }

    /**
     * This method scrapes the following data:
     * 1. Total covid cases
     * 2. Total recovered cases
     * 
     * @param covidData Model that contains the scraped data that will be stored in db
     * @param driver Selenium webdriver that is used to scrape data
     */
    @Override
    public void scrapeCaseData(CovidData covidData, WebDriver driver) {
        LOGGER.info("------ STARTING TO SCRAPE " + UrlUtils.getCaseUrl());

        driver.get(UrlUtils.getCaseUrl());

        List<WebElement> elements = driver.findElements(By.id("maincounter-wrap"));
        elements.remove(elements.size() - 1);
        elements.remove(1);

        for (WebElement element: elements) {
            try {
                String[] data = element.getText().split(":");

                if (WebScraperUtils.isCovidData(data[0])) {
                    WebScraperUtils.updateModel(covidData, data[0], Integer.parseInt(data[1].replaceAll(",|\n", "")));
                }
            } catch(WebElementNotFoundException e) {
                LOGGER.warn("------ " + e.getMessage());
            } catch (IndexOutOfBoundsException e) {
                // Consume error for data that we do not wish to store
            } catch (NumberFormatException e) {
                // Consume error for data that we do not wish to store
            } catch (Exception e) {
                LOGGER.warn("------ UNEXPECTED ERROR: " + e.getMessage());
            }
        }

        LOGGER.info("------ SUCCESSFULLY SCRAPED " + UrlUtils.getCaseUrl());
    }

    /**
     * This method scrapes Singapore's population data
     * 
     * @param covidData Model that contains the scraped data that will be stored in db
     * @param driver Selenium webdriver that is used to scrape data
     */
    @Override
    public void scrapePopulationData(CovidData covidData, WebDriver driver) {
        LOGGER.info("------ STARTING TO SCRAPE " + UrlUtils.getPopulationUrl());

        driver.get(UrlUtils.getPopulationUrl());

        WebElement populationData = driver.findElement(By.className("maincounter-number"));

        try {
            int population = Integer.parseInt(populationData.getText().replaceAll(",", ""));
            covidData.setTotalPopulation(population);
        } catch (NumberFormatException e) {
            // Consume error for data that we do not wish to store
        } catch (Exception e) {
            LOGGER.warn("------ UNEXPECTED ERROR: " + e.getMessage());
        }

        LOGGER.info("------ SUCCESSFULLY SCRAPED " + UrlUtils.getPopulationUrl());
    }
}
