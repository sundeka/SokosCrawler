package main;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import crawler.Crawler;
import excel.Excel;
import exceptions.CrawlerException;
import sokos.SideMenuItem;
import sokos.Sokos;

public class Main {
	private static final Logger logger = LogManager.getLogger("CrawlerLogger");
	
	public static void main(String[] args) throws CrawlerException,InterruptedException {
		Crawler crawler = new Crawler(logger);
		Sokos sokos = new Sokos();
		Excel excel = new Excel("/path/here");
		crawler.openSokos();
		for (SideMenuItem menuItem : sokos.getSideMenuItems()) {
			if (excel.fileExists(menuItem.getExcelTitle())) {
				logger.info("Found Excel file for " + menuItem.getSideBarTitle() + ". Continuing to the next one...");
				continue;
			}
			crawler.openCategory(menuItem);
			for (String subMenuTitle : menuItem.getSubMenuTitles()) {
				crawler.openSubMenu(subMenuTitle);
				for (String itemTitle : crawler.getItemTitles()) {
					crawler.openItem(itemTitle);
					HashMap<String, double[]> nutritionData = crawler.scrapeNutritionInformation(itemTitle);
					crawler.navigateBackToSubMenu(subMenuTitle);
					excel.append(menuItem.getExcelTitle(), itemTitle, nutritionData);
					logger.info("Finished handling menu item: " + itemTitle);
				}
				logger.info("Finished handling sub menu: " + subMenuTitle);
			}
			logger.info("Finished handling category: " + menuItem.getSideBarTitle());
		}
		crawler.quit();
		logger.info("Run finished successfully!");
		logger.info("The Excel files can be found at: " + excel.getPath().toString());
	}
}
