package main;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import crawler.Crawler;
import excel.Excel;
import excel.ExcelEntry;
import exceptions.CrawlerException;
import sokos.SideMenuItem;
import sokos.Sokos;

public class Main {
	private static final Logger logger = LogManager.getLogger("CrawlerLogger");
	
	public static void main(String[] args) throws CrawlerException,InterruptedException {
		Path excelFolder = Paths.get("C:\\Users\\sunde\\Desktop\\excels");
		Crawler crawler = new Crawler(logger);
		Sokos sokos = new Sokos();
		crawler.openSokos();
		for (SideMenuItem menuItem : sokos.getSideMenuItems()) {
			Excel excel = new Excel(
					excelFolder,
					menuItem.getExcelTitle(),
					logger
				);
			if (excel.fileExists()) {
				logger.info("Found Excel file for " + menuItem.getSideBarTitle() + ". Continuing to the next one...");
				continue;
			} 
			crawler.openCategory(menuItem);
			for (String subMenuTitle : menuItem.getSubMenuTitles()) {
				excel.createHeaderRow(subMenuTitle);
				crawler.openSubMenu(subMenuTitle);
				for (String itemTitle : crawler.getItemTitles()) {
					crawler.openItem(itemTitle);
					HashMap<String, double[]> nutritionData = crawler.scrapeNutritionInformation(itemTitle);
					crawler.navigateBackToSubMenu(subMenuTitle);
					ExcelEntry entry = new ExcelEntry(itemTitle, nutritionData);
					excel.append(entry);
					logger.info("Finished handling menu item: " + itemTitle);
				}
				logger.info("Finished handling sub menu: " + subMenuTitle);
			}
			excel.close();
			logger.info("Finished handling category: " + menuItem.getSideBarTitle());
		}
		crawler.quit();
		logger.info("Run finished successfully!");
		logger.info("The Excel files can be found at: " + excelFolder.toString());
	}
}
