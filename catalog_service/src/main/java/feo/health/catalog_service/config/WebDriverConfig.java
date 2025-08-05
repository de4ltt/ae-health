package feo.health.catalog_service.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class WebDriverConfig {

    @Autowired
    private Environment environment;

    private WebDriver webDriver;

    @PostConstruct
    public void init() {
        String chromeDriverPath = environment.getProperty("webdriver.chrome.driver");
        assert chromeDriverPath != null;
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) " +
                "Chrome/115.0.0.0 Safari/537.36");
        this.webDriver = new ChromeDriver(options);
    }

    @Bean
    public WebDriver webDriver() {
        return this.webDriver;
    }

    @PreDestroy
    public void destroy() {
        if (this.webDriver != null) this.webDriver.quit();
    }

}
