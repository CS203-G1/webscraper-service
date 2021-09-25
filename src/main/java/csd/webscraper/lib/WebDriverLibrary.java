package csd.webscraper.lib;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.bonigarcia.wdm.WebDriverManager;

@Configuration
public class WebDriverLibrary {

    @Bean
    @ConditionalOnProperty(name = "browser", havingValue = "Chrome")
    public WebDriver getChromeDriver() {
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver();
    }
}
