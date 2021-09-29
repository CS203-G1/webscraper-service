package csd.webscraper.utils;

import java.util.List;

import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import csd.webscraper.model.CovidData;

@Component
public class WebScraperUtils {
    private static final List<String> COVID_DATA = List.of("Hospitalised", "Hospitalised (ICU)",
            "Require Oxygen Supplementation", "Deaths^", "Total Swabs Tested",
            "Average Daily Number of Swabs Tested Over The Past Week", "Total Swabs Per 1,000,000 Total Population");

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
            default:
                break;
        }
    }
}
