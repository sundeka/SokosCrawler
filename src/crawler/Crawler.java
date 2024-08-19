package crawler;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class Crawler {
	private Logger logger;
	private WebDriver driver;
	private Wait<WebDriver> wait;
	private CrawlerUtils utils;
	
	public Crawler(Logger logger) {
		this.logger = logger;
		this.driver = initDriver();
		this.utils = new CrawlerUtils();
		this.logger.info("Crawler object initialized.");
	}
	
	private WebDriver initDriver() {
		ChromeOptions options = new ChromeOptions();
		options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.DISMISS_AND_NOTIFY);
		options.addArguments("--disable-notifications");
        options.addArguments("--no-first-run");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-extensions");
		WebDriver driver = new ChromeDriver(options);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		return driver;
	}
	
	public void openSokos() {
		/**
		 * Navigates to the "Tuotteet" page and waits for it to fully load
		 */
		this.driver.get("https://www.s-kaupat.fi/tuotteet/");
		this.utils.closePossibleDefaultBrowserPrompt(this.driver, this.logger);
	}
}
