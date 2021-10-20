package csd.webscraper.utils;

import org.springframework.stereotype.Component;

@Component
public class UrlUtils {
    private final static String MOH_URL = "https://www.moh.gov.sg/covid-19/statistics";
    private final static String GOV_URL = "https://www.gov.sg/COVID-19";
    private final static String CASE_URL = "https://www.worldometers.info/coronavirus/country/singapore/";
    private final static String POPULATION_URL = "https://www.worldometers.info/world-population/singapore-population/";

    public static String getMohUrl() {
        return MOH_URL;
    }

    public static String getGovUrl() {
        return GOV_URL;
    }

    public static String getCaseUrl() {
        return CASE_URL;
    }

    public static String getPopulationUrl() {
        return POPULATION_URL;
    }
}
