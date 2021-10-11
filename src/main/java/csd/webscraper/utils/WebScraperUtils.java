package csd.webscraper.utils;

import java.util.List;

import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import csd.webscraper.exception.WebElementNotFoundException;
import csd.webscraper.model.CovidData;

@Component
public class WebScraperUtils {
    private static final List<String> COVID_DATA = List.of(
        "Local cases", "Imported cases", "Total covid cases", "Total recovered", "Total deaths",
        "Total Swabs Tested",

        // Table headers from www.gov.sg #casesummary table
        "Total new cases", "Community", "Dormitory", "Imported", "Hospitalised", "Requires oxygen supplementation", "In Intensive Care Unit", "Number of deaths^", "Hospitalised",

        // Table headers from www.gov.sg #vaccinedata table
        "Total Doses Administrated", "Received at least one dose", "Completed full regimen"
        );

    public static ChromeOptions getChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--start-maximized");

        return chromeOptions;
    }

    // This method checks if the header of the data inside the website corresponds
    // to the one we're looking for
    public static boolean isCovidData(String header) throws WebElementNotFoundException {
        if (COVID_DATA.contains(header)) {
            return true;
        }
        throw new WebElementNotFoundException(header);
    }

    public static void updateModel(CovidData covidData, String header, int value) {
        switch (header) {
            // From www.gov.sg case summary table
            case "Total new cases":
                covidData.setNewCases(value);
                break;
            case "Community":
                covidData.setNewCommunityCases(value);
                break;
            case "Dormitory":
                covidData.setNewDormitoryCases(value);
                break;
            case "Imported":
                covidData.setNewImportedCases(value);
                break;
            case "Hospitalised":
                covidData.setHospitalised(value);
                break;
            case "In Intensive Care Unit":
                covidData.setHospitalisedICU(value);
                break;
            case "Require Oxygen supplementation":
                covidData.setRequireOxygen(value);
                break;
            case "Total deaths":
                covidData.setTotalDeaths(value);
                break;
            // TBC
            case "Total Swabs Tested":
                covidData.setTotalSwab(value);
                break;
            // From www.gov.sg case vaccine table
            case "Total Doses Administrated":
                covidData.setTotalVaccinationDoses(value);
                break;
            case "Received at least one dose":
                covidData.setAtLeastOneDose(value);
                break;
            case "Completed full regimen":
                covidData.setCompletedFullRegimen(value);
                break;
            case "Total covid cases":
                covidData.setTotalCovidCases(value);
            case "Total recovered":
                covidData.setTotalRecovered(value);
            default:
                break;
        }
    }
}
