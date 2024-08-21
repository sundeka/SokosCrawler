package crawler;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
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
	
	public void quit() {
		driver.quit();
	}
	
	/**
	 * Navigates to the "Tuotteet" page and waits for it to fully load
	 * @return boolean
	 * @throws CrawlerException
	 */
	public void openSokos() throws CrawlerException {
		driver.get("https://www.s-kaupat.fi/tuotteet/");
		try {
			ocr.closePossibleDefaultBrowserPrompt(logger);
			ocr.closeCookieSettingsPrompt(logger);
			if (elementIsPresent("//a[@data-test-id='local-nav-products-container']")) {
				logger.info("S-Kaupat page opened successfully.");
				return;
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
		openSideBar();
		logger.info("Finding sidebar item with XPath: " + menuItem.getXpath());
		try {
			waitForPresenceAndClick(menuItem.getXpath());
		} catch (CrawlerException e) {
			logger.warn("Unable to find category on the first try. Checking if scrolling down will fix...");
			scroll("//div[@data-test-id='product-navigation-category']", 200);
			waitForPresenceAndClick(menuItem.getXpath());
		}
		logger.info("Category opened. Waiting for confirmation...");
		waitForVisibility("//a[@data-test-id='product-navigation-subcategory-child-item']");
		logger.info("Category successfully opened!");
	}
	
	/**
	 * 
	 * @param title
	 * @throws CrawlerException
	 */
	public void openSubMenu(String title) throws CrawlerException, InterruptedException {
		openSideBar();
		String xpath = "//a[@data-test-id='product-navigation-subcategory-item-title'][text()='" + title + "']";
		logger.info("Finding sub menu with XPath '" + xpath + "'...");
		try {
			waitForPresenceAndClick(xpath);
		} catch (CrawlerException e) {
			logger.warn("Unable to find sub menu on the first try. Checking if scrolling down will fix...");
			scroll("//div[@data-test-id='product-navigation-subcategory']", 200);
			waitForPresenceAndClick(xpath);
		}
		waitForVisibility("//article[@data-product-id]");
		logger.info("Sub menu successfully opened!");
		Thread.sleep(5000);
	}
	
	public String[] getItemTitles() throws InterruptedException{
		String[] titles = new String[8];
		Thread.sleep(5000);
		List<WebElement> elements = wait.until(
				ExpectedConditions.visibilityOfAllElementsLocatedBy(
						By.xpath("//a[@data-test-id='product-card__productName']")
						)
				);
		for (int i=0;i<elements.size();i++) {
			if (i==8) {
				break;
			}
			titles[i] = elements.get(i).getText();
		}
		return titles;
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
	private void waitForVisibility(String xpath) throws CrawlerException {
		logger.info("Checking if element with XPath '" + xpath + "' is clickable...");
		try {
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			if (element == null) {
				throw new CrawlerException("Element with XPath '" + xpath + "' was not visible!");
			}
			logger.info("Found element with XPath '" + xpath + "'!");
		} catch (ElementNotInteractableException e) {
			throw new CrawlerException("Element with XPath '" + xpath + "' was not visible!");
		}
	}
	
	/**
	 * 
	 * @param xpath
	 * @throws CrawlerException
	 */
	private void waitForPresenceAndClick(String xpath) throws CrawlerException {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			throw new CrawlerException(e.getMessage());
		}
		logger.info("Checking if element with XPath '" + xpath + "' is clickable...");
		try {
			WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
			if (element == null) {
				throw new CrawlerException("Could not find the presence of element " + xpath);
			}
			element.click();
		} catch (ElementNotInteractableException e) {
			throw new CrawlerException("Could not find the presence of element " + xpath);
		}
	}
		
		
	
	private void openSideBar() {
		if (!sidebarIsOpen()) {
			logger.info("Opening side bar...");
			WebElement sideBarToggle = driver.findElement(By.xpath("//a[@href='/tuotteet'][@data-test-id='local-nav-products-container']"));
			sideBarToggle.click();
			logger.info("Sidebar opened.");
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
