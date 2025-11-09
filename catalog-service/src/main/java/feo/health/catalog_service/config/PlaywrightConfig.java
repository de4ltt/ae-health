package feo.health.catalog_service.config;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Configuration
public class PlaywrightConfig {

    private volatile Playwright playwright;
    private volatile Browser browser;

    @Bean
    public Browser browser() {
        if (playwright == null) {
            synchronized (this) {
                if (playwright == null) {
                    this.playwright = Playwright.create();
                    this.browser = playwright.chromium().launch(
                            new BrowserType.LaunchOptions()
                                    .setHeadless(true)
                                    .setArgs(List.of("--no-sandbox", "--disable-gpu"))
                    );
                }
            }
        }
        return browser;
    }

    @Bean
    public BrowserContext browserContext(Browser browser) {
        return browser.newContext();
    }

    @PreDestroy
    public void close() {
        if (browser != null) {
            closeQuietly(browser::close);
        }
        if (playwright != null) {
            closeQuietly(playwright::close);
        }
    }

    @Async
    private void closeQuietly(Runnable closeAction) {
        try {
            closeAction.run();
        } catch (Exception ignored) {}
    }
}