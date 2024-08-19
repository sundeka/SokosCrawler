package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import crawler.Crawler;

public class Main {
	private static final Logger logger = LogManager.getLogger("CrawlerLogger");
	
	public static void main(String[] args) {
		Crawler crawler = new Crawler(logger);
		// Sokos sokos = new Sokos()
		crawler.openSokos();
		// ArrayList<SideMenuItem> menuItems = sokos.getSideMenuItems()
		// for (SideMenuItem menuItem : menuItems) { }
	}
}
