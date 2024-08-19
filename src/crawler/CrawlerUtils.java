package crawler;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class CrawlerUtils {
	/*
	 * A separate class for handling some of the dirty stuff,
	 * making the actual Crawler class a bit more readable.
	 */
	void closePossibleDefaultBrowserPrompt(WebDriver driver, Logger logger) {
		logger.info("Checking if the 'Valitse hakukone' promp opens...");
		System.out.println("hello from crawlerutils");
		//"//cr-radio-button[@aria-label='Google']//div[@id='button']"
	}
}
