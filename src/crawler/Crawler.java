package crawler;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class Crawler {
	private Logger logger;
	private WebDriver driver;
	
	public Crawler(Logger logger, WebDriver driver) {
		this.logger = logger;
		this.driver = driver;
		this.logger.info("Crawler object initialized.");
	}
	
	public void openSokos() {
		/**
		 * Navigates to the "Tuotteet" page and waits for it to fully load
		 */
		this.driver.get("https://www.s-kaupat.fi/tuotteet/");
		//
	}
}
