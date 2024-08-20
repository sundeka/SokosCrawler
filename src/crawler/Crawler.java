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

import exceptions.CrawlerException;
import ocr.OcrFunctions;

import org.openqa.selenium.support.ui.ExpectedConditions;

public class Crawler {
	private Logger logger;
	private WebDriver driver;
	private Wait<WebDriver> wait;
	private CrawlerUtils utils;
	private OcrFunctions ocr;
	
	public Crawler(Logger logger) {
		this.logger = logger;
		this.driver = initDriver();
		this.utils = new CrawlerUtils();
		this.ocr = new OcrFunctions();
		this.logger.info("Crawler object initialized.");
	}
	
	public Wait<WebDriver> getWait() {
		return wait;
	}

	public void setWait(Wait<WebDriver> wait) {
		this.wait = wait;
	}
	
	private WebDriver initDriver() {
		ChromeOptions options = new ChromeOptions();
		options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.DISMISS_AND_NOTIFY);
		options.addArguments("--disable-notifications");
		options.addArguments("--no-first-run");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-extensions");
        options.addArguments("--start-maximized");
		WebDriver driver = new ChromeDriver(options);
		this.setWait(new WebDriverWait(driver, Duration.ofSeconds(10)));
		return driver;
	}
	
	public boolean openSokos() throws CrawlerException {
		/**
		 * Navigates to the "Tuotteet" page and waits for it to fully load
		 */
		this.driver.get("https://www.s-kaupat.fi/tuotteet/");
		try {
			this.ocr.closePossibleDefaultBrowserPrompt(this.logger);
			this.ocr.closeCookieSettingsPrompt(this.logger);
			//return driver find some sidebar element
		} catch (Exception e) {
			this.logger.error("Unable to detect sidebar!");
			throw new CrawlerException(e.getMessage());
		}
		return false;
	}
}
