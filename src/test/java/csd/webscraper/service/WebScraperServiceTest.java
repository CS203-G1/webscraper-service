package csd.webscraper.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import csd.webscraper.utils.UrlUtils;
import csd.webscraper.utils.WebScraperUtils;
import io.github.bonigarcia.wdm.WebDriverManager;

public class WebScraperServiceTest {
    private static final Logger LOGGER = LogManager.getLogger(WebScraperServiceTest.class);
    private static WebDriver driver;

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setupTest() {
        driver = new ChromeDriver(WebScraperUtils.getChromeOptions());
    }

    // @AfterAll
    // public static void tearDown() {
    //     LOGGER.info("------ TEARING DOWN WEB DRIVER");
    //     if (driver != null) driver.quit();
    // }

    // @Test
    // public void scrapeMohData_TableHeadersExist_ScrapeSuccess() {
    //     driver.get(UrlUtils.getMohUrl());
    //     String header = driver.findElement(By.xpath("//*[@id=\"ContentPlaceHolder_contentPlaceholder_C030_Col00\"]/div/div/table/tbody/tr[1]/td/span/strong")).getText();

    //     assertEquals(header, "Total Swabs Tested");
    // }

    // @Test
    // public void scrapeGovData_TableHeadersExist_ScrapeSuccess() {
    //     driver.get(UrlUtils.getGovUrl());
    //     List<String> headers = new ArrayList<>();

    //     // Local Cases
    //     WebElement localCases = driver.findElement(By.id("localcases"));
    //     List<WebElement> localCasesTableData = localCases.findElements(By.tagName("td"));

    //     for (WebElement localCasesElement: localCasesTableData) {
    //         try {
    //             if (WebScraperUtils.isCovidData(localCasesElement.getText())) {
    //                 headers.add(localCasesElement.getText());
    //             }
    //         } catch (WebElementNotFoundException e) {
    //             // Consume this exception since there bound to be redundant data when we call
    //             // getText()
    //         } catch (NoSuchElementException e) {
    //             System.out.println(e.getMessage());
    //         }
    //     }

    //     // Vaccine Data
    //     WebElement vaccineData = driver.findElement(By.id("vaccinedata"));
    //     WebElement vaccineDataSibling = vaccineData.findElement(By.xpath("following-sibling::*"));
    //     List<WebElement> vaccineDataTableData = vaccineDataSibling.findElements(By.tagName("td"));

    //     for (WebElement vaccineDataElement: vaccineDataTableData) {
    //         try {
    //             if (WebScraperUtils.isCovidData(vaccineDataElement.getText())) {
    //                 headers.add(vaccineDataElement.getText());
    //             }
    //         } catch (WebElementNotFoundException e) {
    //             // Consume this exception since there bound to be redundant data when we call
    //             // getText()
    //         }
    //     }

    //     // There should be a total of 11 headers inside the array
    //     assertEquals(headers.size(), 11);
    // }
}
