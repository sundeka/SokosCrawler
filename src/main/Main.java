package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import crawler.Crawler;

public class Main {
	private static final Logger logger = LogManager.getLogger("CrawlerLogger");
	private static WebDriver driver = new ChromeDriver();
	
	public static void main(String[] args) {
		System.out.println("Hello");
		Crawler crawler = new Crawler(logger, driver);
		// Sokos sokos = new Sokos()
		crawler.openSokos();
		// ArrayList<SideMenuItem> menuItems = sokos.getSideMenuItems()
		// for (SideMenuItem menuItem : menuItems) { }
	}
}
