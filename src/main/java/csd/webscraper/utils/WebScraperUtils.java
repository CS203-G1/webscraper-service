package csd.webscraper.utils;

import java.util.List;

import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import csd.webscraper.model.CovidData;

@Component
public class WebScraperUtils {
    private static final List<String> COVID_DATA = List.of(
        "Total new cases", "Local cases", "Imported cases",
        "Hospitalised", "Hospitalised (ICU)", "Require Oxygen Supplementation", "Deaths^", "Total Swabs Tested",
        "Average Daily Number of Swabs Tested Over The Past Week", "Total Swabs Per 1,000,000 Total Population",
        "Total Doses Administrated", "Received at least one dose", "Completed full regimen"
    );

    public static ChromeOptions getChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--headless");

        return chromeOptions;
    }

    // This method checks if the header of the data inside the website corresponds
    // to the one we're looking for
    public static boolean isCovidData(String header) {
        return COVID_DATA.contains(header);
    }

    public static void updateModel(CovidData covidData, String header, int value) {
        switch (header) {
            case "Total new cases":
                covidData.setNewCases(value);
                break;
            case "Local cases":
                covidData.setNewLocalCases(value);
                break;
            case "Imported cases":
                covidData.setNewImportedCases(value);
                break;
            case "Hospitalised":
                covidData.setHospitalised(value);
                break;
            case "Hospitalised (ICU)":
                covidData.setHospitalisedICU(value);
                break;
            case "Require Oxygen Supplementation":
                covidData.setRequireOxygen(value);
                break;
            case "Deaths^":
                covidData.setDeaths(value);
                break;
            case "Total Swabs Tested":
                covidData.setTotalSwab(value);
                break;
            case "Average Daily Number of Swabs Tested Over The Past Week":
                covidData.setAverageDailySwabPerWeek(value);
                break;
            case "Total Swabs Per 1,000,000 Total Population":
                covidData.setTotalSwabPerMillion(value);
                break;
            case "Total Doses Administrated":
                covidData.setTotalVaccinationDoses(value);
                break;
            case "Received at least one dose":
                covidData.setAtLeastOneDose(value);
                break;
            case "Completed full regimen":
                covidData.setCompletedFullRegimen(value);
                break;
            default:
                break;
        }
    }
}
