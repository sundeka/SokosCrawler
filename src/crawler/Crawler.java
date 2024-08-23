package crawler;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import exceptions.CrawlerException;
import io.opentelemetry.api.internal.Utils;
import ocr.OcrFunctions;
import sokos.SideMenuItem;

public class Crawler {
	private Logger logger;
	private WebDriver driver;
	private Wait<WebDriver> wait;
	private OcrFunctions ocr;
	private CrawlerStringUtils stringUtils;
	
	public Crawler(Logger logger) {
		this.logger = logger;
		driver = initDriver();
		ocr = new OcrFunctions();
		stringUtils = new CrawlerStringUtils();
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
	 * Open a food category from the sidebar.
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
	 * Once a category is selected from the side bar,
	 * this method opens one of the sub menus (with green text). 
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
			scroll("//div[@data-test-id='product-navigation-subcategory']", 1000);
			waitForPresenceAndClick(xpath);
		}
		waitForVisibility("//article[@data-product-id]");
		logger.info("Sub menu successfully opened!");
		Thread.sleep(1000);
	}
	
	/**
	 * Read the 8 top-most food items for looping.
	 * @return
	 * @throws InterruptedException
	 */
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
	
	/**
	 * Open the item "card" and wait for it to load.
	 * @param title
	 * @throws CrawlerException
	 * @throws InterruptedException
	 */
	public void openItem(String title) throws CrawlerException, InterruptedException {
		String xpath;
		if (title == null) {
			// In case a sub menu has less than 8 items, 
			// the remaining titles will have a null value.
			// In this case, we can just skip them.
			return;
		}
		xpath = "//a//span[text()='" + title + "']";
		waitForPresenceAndClick(xpath);
		Thread.sleep(3000);
		xpath = "//div[@data-test-id='product-page-container']";
		elementIsPresent(xpath);
		logger.info("The item view for '" + title + "' should be open now.");
	}
	
	/**
	 * Return the nutrition information in raw form as parsed from the HTML tree.
	 * @param itemTitle
	 * @return
	 * @throws CrawlerException
	 * @throws InterruptedException
	 */
	public HashMap<String, double[]> scrapeNutritionInformation(String itemTitle) throws CrawlerException, InterruptedException {
		String xpath = "//details[@data-test-id='nutrients-info']";
		try {
			waitForPresenceAndClick(xpath);
			Thread.sleep(3000);

		} catch (CrawlerException e) {
			logger.info("No nutrition information found for " + itemTitle);
			Thread.sleep(3000);
			return new HashMap<String, double[]>();
		}
		// Wait for the nutritent list to load properly
		xpath = "//div[@data-test-id='nutrients-info-per-unit-content']";
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
		setWait(new WebDriverWait(driver, Duration.ofSeconds(1)));
		HashMap<String, double[]> data = parseNutritionalInfo(); 
		setWait(new WebDriverWait(driver, Duration.ofSeconds(10)));
		return data;
	}
		
	
	/**
	 * Scroll back up and return to the list of products.
	 * @param category
	 */
	public void navigateBackToSubMenu(String category) throws CrawlerException, InterruptedException {
		String xpath;
		xpath = "//div[@data-test-id='product-page-container']";
		scrollToTopOfPage();
		Thread.sleep(1000);
		xpath = "//li//a[text()='" + category + "']";
		waitForPresenceAndClick(xpath);
		Thread.sleep(3000);
		xpath = "//article[@data-test-id='product-card']";
		waitForVisibility(xpath);
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
		} catch (ElementNotInteractableException | NoSuchElementException e) {
			throw new CrawlerException("Element with XPath '" + xpath + "' was not visible!");
		}
	}
	
	/**
	 * Use EC for more robust waiting for an element, combined with a click.
	 * A common operation.
	 * @param xpath
	 * @throws CrawlerException
	 */
	private void waitForPresenceAndClick(String xpath) throws CrawlerException {
		try {
			Thread.sleep(4000);
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
		} catch (ElementNotInteractableException | TimeoutException | NoSuchElementException e) {
			throw new CrawlerException("Could not find the presence of element " + xpath);
		}
	}
		
		
	/**
	 * Open the "Tuotteet" sidebar.
	 * Do nothing if it is already open.
	 */
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
	 * Parse the raw text from the nutrition info list using regex and convert to HashMap for easier handling
	 * @return
	 */
	private HashMap<String, double[]> parseNutritionalInfo() {
		// Initialize HashMap where values will be stored
		HashMap<String, double[]> attributes = new HashMap<>();
		attributes.put("Energiaa", null);
		attributes.put("Rasvaa", null);
		attributes.put("Rasvaa, josta tyydyttyneitä rasvoja", null);
		attributes.put("Hiilihydraattia", null);
		attributes.put("Ravintokuitua", null);
		attributes.put("Proteiinia", null);
		attributes.put("Suolaa", null);
			
		// Iterate rows
		for (String attribute : attributes.keySet()) {
			// Initialize required XPaths
			String commonXpath = "//div[@class='cell'][text()='"+ attribute + "']";
			String perHundredGramsXpath = commonXpath + "/following-sibling::div";
			String recommendedIntakeXpath = perHundredGramsXpath + "/following-sibling::div";
				
			// Find elements
			String perHundredGramsText;
			String recommendedIntakeText;
			try {
				perHundredGramsText = driver.findElement(By.xpath(perHundredGramsXpath)).getText();
				recommendedIntakeText = driver.findElement(By.xpath(recommendedIntakeXpath)).getText();
			} catch (NoSuchElementException e) {
				logger.info("Attribute '" + attribute + "' was not present. Skipping...");
				double[] result = {0.0, 0.0};
				attributes.put(attribute, result);
				continue;
			}
			
			// Parse accordingly
			double recommendedIntake = this.stringUtils.convertToDouble(Attribute.RECOMMENDED_INTAKE, recommendedIntakeText);
			if (attribute.equals("Energiaa")) {
				double kcal = this.stringUtils.convertToDouble(Attribute.KCAL, perHundredGramsText);
				double[] result = {kcal, recommendedIntake};
				attributes.put(attribute, result);
			} else {
				double grams = this.stringUtils.convertToDouble(Attribute.GRAMS, perHundredGramsText);
				double[] result = {grams, recommendedIntake};
				attributes.put(attribute, result);
			}
		}		
		return attributes;
	}
	
	/**
	 * Scroll up/down using JavaScript.
	 * @param xpath
	 * @param amount: positive values scroll down, negative values scroll up
	 */
	private void scroll(String xpath, int amount) {
		WebElement sidebar = driver.findElement(By.xpath(xpath));
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    js.executeScript("arguments[0].scrollTop += arguments[1];", sidebar, amount);
	}
	
	/**
	 * Workaround for cases where scrolling does not work.
	 */
	private void scrollToTopOfPage() {
		Actions actions = new Actions(driver);
        actions.sendKeys(Keys.HOME).perform();
	}
}
