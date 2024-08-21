package crawler;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
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
import sokos.SideMenuItem;

public class Crawler {
	private Logger logger;
	private WebDriver driver;
	private Wait<WebDriver> wait;
	private OcrFunctions ocr;
	
	public Crawler(Logger logger) {
		this.logger = logger;
		driver = initDriver();
		ocr = new OcrFunctions();
		logger.info("Crawler object initialized.");
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
		driver.get("https://www.s-kaupat.fi/tuotteet/");
		try {
			ocr.closePossibleDefaultBrowserPrompt(logger);
			ocr.closeCookieSettingsPrompt(logger);
			if (elementIsPresent("//span[text()='Tuotteet']")) {
				logger.info("S-Kaupat page opened successfully.");
				return true;
			}
			throw new Exception("Expected element was not found with the provided XPath!");
		} catch (Exception e) {
			logger.error("Unable to detect sidebar!");
			throw new CrawlerException(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param menuItem
	 * @throws CrawlerException
	 */
	public void openCategory(SideMenuItem menuItem) throws CrawlerException {
		logger.info("Opening " + menuItem.getSideBarTitle() + "...");
		if (!sidebarIsOpen()) {
			logger.info("Opening side bar...");
			WebElement sideBarToggle = driver.findElement(By.xpath("//a[@href='/tuotteet'][@data-test-id='local-nav-products-container']"));
			sideBarToggle.click();
			logger.info("Sidebar opened.");
		}
		logger.info("Finding sidebar item with XPath: " + menuItem.getXpath());
		try {
			wait_for_presence_and_click(menuItem.getXpath());
		} catch (CrawlerException e) {
			logger.warn("Unable to find category on the first try. Checking if scrolling down will fix...");
			scroll("//div[@data-test-id='product-navigation-category']", 200);
			wait_for_presence_and_click(menuItem.getXpath());
		}
		logger.info("Category opened. Waiting for confirmation...");
		wait_for_visibility("//a[@data-test-id='product-navigation-subcategory-child-item']");
		logger.info("Category successfully opened!");
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
		logger.info("Finding element with XPath: " + xpath);
		try {
            WebElement element = driver.findElement(By.xpath(xpath));
            return element != null;
        } catch (NoSuchElementException e) {
            return false;
        }
	}
	
	/**
	 * 
	 * @param xpath
	 * @throws CrawlerException
	 */
	private void wait_for_visibility(String xpath) throws CrawlerException {
		logger.info("Checking if element with XPath '" + xpath + "' is clickable...");
		try {
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			if (element == null) {
				throw new CrawlerException("Element with XPath '" + xpath + "' was not visible!");
			}
			logger.info("Found element with XPath '" + xpath + "'!");
		} catch (ElementClickInterceptedException e) {
			throw new CrawlerException("Element with XPath '" + xpath + "' was not visible!");
		}
	}
	
	/**
	 * 
	 * @param xpath
	 * @throws CrawlerException
	 */
	private void wait_for_presence_and_click(String xpath) throws CrawlerException {
		logger.info("Checking if element with XPath '" + xpath + "' is clickable...");
		try {
			WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
			if (element == null) {
				throw new CrawlerException("Could not find the presence of element " + xpath);
			}
			element.click();
		} catch (ElementClickInterceptedException e) {
			throw new CrawlerException("Could not find the presence of element " + xpath);
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
			logger.info("Sidebar is open.");
		} else {
			logger.info("Sidebar is closed.");
		}
		return isPresent;
	}
	
	/**
	 * Scroll up/down using JavaScript.
	 * @param xpath
	 * @param amount: positive values scroll down, negative values scroll up
	 */
	private void scroll(String xpath, int amount) {
		WebElement sidebar = driver.findElement(By.xpath(xpath));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollTop += " + amount + ";", sidebar);

	}
}
