package crawler;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import exceptions.CrawlerException;
import ocr.OcrFunctions;

public class Crawler {
	private Logger logger;
	private WebDriver driver;
	private Wait<WebDriver> wait;
	private OcrFunctions ocr;
	
	public Crawler(Logger logger) {
		this.logger = logger;
		this.driver = initDriver();
		this.ocr = new OcrFunctions();
		this.logger.info("Crawler object initialized.");
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
		setWait(new WebDriverWait(driver, Duration.ofSeconds(10)));
		return driver;
	}
	
	//
	// Getters / setters
	//
	
	public Wait<WebDriver> getWait() {
		return wait;
	}

	public void setWait(Wait<WebDriver> wait) {
		this.wait = wait;
	}
	
	//
	// Public API
	//
	
	/**
	 * Navigates to the "Tuotteet" page and waits for it to fully load
	 * @return boolean
	 * @throws CrawlerException
	 */
	public boolean openSokos() throws CrawlerException {
		this.driver.get("https://www.s-kaupat.fi/tuotteet/");
		try {
			this.ocr.closePossibleDefaultBrowserPrompt(this.logger);
			this.ocr.closeCookieSettingsPrompt(this.logger);
			if (elementIsPresent("//span[text()='Tuotteet']")) {
				this.logger.info("S-Kaupat page opened successfully.");
				return true;
			}
			throw new Exception("Expected element was not found with the provided XPath!");
		} catch (Exception e) {
			this.logger.error("Unable to detect sidebar!");
			throw new CrawlerException(e.getMessage());
		}
	}
	
	//
	// Private API
	//
	
	/**
	 * A quick wrapper for detecting if an element is present in the current state of DOM.
	 * @param xpath
	 * @return boolean
	 */
	private boolean elementIsPresent(String xpath) {
		this.logger.info("Finding element with XPath: " + xpath);
		try {
            WebElement element = this.driver.findElement(By.xpath(xpath));
            return element != null;
        } catch (NoSuchElementException e) {
            return false;
        }
	}
	
	/**
	 * When looping the sidebar elements, always check if the sidebar is open already.
	 * @return boolean
	 */
	private boolean sidebarIsOpen() {
		setWait(new WebDriverWait(driver, Duration.ofSeconds(5)));
		boolean isPresent = elementIsPresent("//div[@data-test-id='product-navigation-category']");
		setWait(new WebDriverWait(driver, Duration.ofSeconds(10)));
		if (isPresent) {
			this.logger.info("Sidebar is open.");
		} else {
			this.logger.info("Sidebar is closed.");
		}
		return isPresent;
	}
}
